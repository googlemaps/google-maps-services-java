/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.google.maps;

import static com.google.maps.TestUtils.findLastThreadByName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.google.maps.android.Context;
import com.google.maps.android.PackageInfo;
import com.google.maps.android.PackageManager;
import com.google.maps.errors.OverQueryLimitException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.HttpHeaders;
import com.google.maps.model.GeocodingResult;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.MockedStatic;

@Category(MediumTests.class)
public class GeoApiContextTest {

  private MockWebServer server;
  private GeoApiContext.Builder builder;

  @Before
  public void Setup() {
    server = new MockWebServer();
    builder = new GeoApiContext.Builder().apiKey("AIza...").queryRateLimit(500);
  }

  @After
  @SuppressWarnings("CatchAndPrintStackTrace")
  public void Teardown() {
    try {
      server.shutdown();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setMockBaseUrl() {
    builder.baseUrlOverride("http://127.0.0.1:" + server.getPort());
  }

  @Test
  public void testIncludedAndroidAuthHeaders() throws IOException, InterruptedException {
    // Set up a mock request
    ApiResponse<?> fakeResponse = mock(ApiResponse.class);
    Map<String, List<String>> params = new HashMap<>();
    params.put("key", Collections.singletonList("value"));
    String packageName = "com.test.test.test";

    Context mockContext = mock(Context.class);
    when(mockContext.getPackageName()).thenReturn(packageName);
    MockedStatic<Context> mockedStaticContext = mockStatic(Context.class);
    mockedStaticContext.when(Context::getApplicationContext).thenReturn(mockContext);

    PackageInfo mockPi = mock(PackageInfo.class);
    when(mockPi.signingSignature()).thenReturn(null);

    PackageManager mockPm = mock(PackageManager.class);
    when(mockPm.getPackageInfo(packageName, 64)).thenReturn(mockPi);
    when(mockContext.getPackageManager()).thenReturn(mockPm);

    // Set up the fake web server
    builder = new GeoApiContext.Builder().apiKey("AIza...");
    server.enqueue(new MockResponse());
    server.start();
    setMockBaseUrl();

    // Build & execute the request using our context
    GeoApiContext context = builder.build();
    context.get(new ApiConfig("/"), fakeResponse.getClass(), params).awaitIgnoreError();

    // Read the headers
    server.shutdown();
    RecordedRequest request = server.takeRequest();
    Headers headers = request.getHeaders();

    assertEquals(packageName, headers.get(HttpHeaders.X_ANDROID_PACKAGE));
    assertNull(headers.get(HttpHeaders.X_ANDROID_CERT));

    mockedStaticContext.close();
  }

  @Test
  public void testNotIncludedAndroidAuthHeaders() throws IOException, InterruptedException {
    // Set up a mock request
    ApiResponse<?> fakeResponse = mock(ApiResponse.class);
    Map<String, List<String>> params = new HashMap<>();
    params.put("key", Collections.singletonList("value"));

    // Set up the fake web server
    server.enqueue(new MockResponse());
    server.start();
    setMockBaseUrl();

    // Build & execute the request using our context
    GeoApiContext context = builder.build();
    context.get(new ApiConfig("/"), fakeResponse.getClass(), params).awaitIgnoreError();

    // Read the headers
    server.shutdown();
    RecordedRequest request = server.takeRequest();
    Headers headers = request.getHeaders();

    assertNull(headers.get(HttpHeaders.X_ANDROID_PACKAGE));
    assertNull(headers.get(HttpHeaders.X_ANDROID_CERT));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetIncludesDefaultUserAgent() throws Exception {
    // Set up a mock request
    ApiResponse<Object> fakeResponse = mock(ApiResponse.class);
    String path = "/";
    Map<String, List<String>> params = new HashMap<>();
    params.put("key", Collections.singletonList("value"));

    // Set up the fake web server
    server.enqueue(new MockResponse());
    server.start();
    setMockBaseUrl();

    // Build & execute the request using our context
    builder.build().get(new ApiConfig(path), fakeResponse.getClass(), params).awaitIgnoreError();

    // Read the headers
    server.shutdown();
    RecordedRequest request = server.takeRequest();
    Headers headers = request.getHeaders();
    boolean headerFound = false;
    for (String headerName : headers.names()) {
      if (headerName.equals("User-Agent")) {
        headerFound = true;
        String headerValue = headers.get(headerName);
        assertTrue(
            "User agent not in correct format",
            headerValue.matches("GoogleGeoApiClientJava/[^\\s]+"));
      }
    }

    assertTrue("User agent header not present", headerFound);
  }

  @Test
  public void testErrorResponseRetries() throws Exception {
    // Set up mock responses
    MockResponse errorResponse = createMockBadResponse();
    MockResponse goodResponse = createMockGoodResponse();

    server.enqueue(errorResponse);
    server.enqueue(goodResponse);
    server.start();

    // Build the context under test
    setMockBaseUrl();

    // Execute
    GeocodingResult[] result =
        builder.build().get(new ApiConfig("/"), GeocodingApi.Response.class, "k", "v").await();
    assertEquals(1, result.length);
    assertEquals(
        "1600 Amphitheatre Parkway, Mountain View, CA 94043, USA", result[0].formattedAddress);

    server.shutdown();
  }

  @Test(expected = IOException.class)
  public void testSettingMaxRetries() throws Exception {
    MockResponse errorResponse = createMockBadResponse();
    MockResponse goodResponse = createMockGoodResponse();

    // Set up the fake web server
    server.enqueue(errorResponse);
    server.enqueue(errorResponse);
    server.enqueue(errorResponse);
    server.enqueue(goodResponse);
    server.start();
    setMockBaseUrl();

    // This should limit the number of retries, ensuring that the success response is NOT returned.
    builder.maxRetries(2);

    builder.build().get(new ApiConfig("/"), GeocodingApi.Response.class, "k", "v").await();
  }

  private MockResponse createMockGoodResponse() {
    MockResponse response = new MockResponse();
    response.setResponseCode(200);
    response.setBody(
        "{\n"
            + "   \"results\" : [\n"
            + "      {\n"
            + "         \"address_components\" : [\n"
            + "            {\n"
            + "               \"long_name\" : \"1600\",\n"
            + "               \"short_name\" : \"1600\",\n"
            + "               \"types\" : [ \"street_number\" ]\n"
            + "            }\n"
            + "         ],\n"
            + "         \"formatted_address\" : \"1600 Amphitheatre Parkway, Mountain View, "
            + "CA 94043, USA\",\n"
            + "         \"geometry\" : {\n"
            + "            \"location\" : {\n"
            + "               \"lat\" : 37.4220033,\n"
            + "               \"lng\" : -122.0839778\n"
            + "            },\n"
            + "            \"location_type\" : \"ROOFTOP\",\n"
            + "            \"viewport\" : {\n"
            + "               \"northeast\" : {\n"
            + "                  \"lat\" : 37.4233522802915,\n"
            + "                  \"lng\" : -122.0826288197085\n"
            + "               },\n"
            + "               \"southwest\" : {\n"
            + "                  \"lat\" : 37.4206543197085,\n"
            + "                  \"lng\" : -122.0853267802915\n"
            + "               }\n"
            + "            }\n"
            + "         },\n"
            + "         \"types\" : [ \"street_address\" ]\n"
            + "      }\n"
            + "   ],\n"
            + "   \"status\" : \"OK\"\n"
            + "}");

    return response;
  }

  private MockResponse createMockBadResponse() {
    MockResponse response = new MockResponse();
    response.setStatus("HTTP/1.1 500 Internal server error");
    response.setBody("Uh-oh. Server Error.");

    return response;
  }

  @Test(expected = IOException.class)
  public void testRetryCanBeDisabled() throws Exception {
    // Set up 2 mock responses, an error that shouldn't be retried and a success
    MockResponse errorResponse = new MockResponse();
    errorResponse.setStatus("HTTP/1.1 500 Internal server error");
    errorResponse.setBody("Uh-oh. Server Error.");
    server.enqueue(errorResponse);

    MockResponse goodResponse = new MockResponse();
    goodResponse.setResponseCode(200);
    goodResponse.setBody("{\n   \"results\" : [],\n   \"status\" : \"ZERO_RESULTS\"\n}");
    server.enqueue(goodResponse);

    server.start();
    setMockBaseUrl();

    // This should disable the retry, ensuring that the success response is NOT returned
    builder.disableRetries();

    // We should get the error response here, not the success response.
    builder.build().get(new ApiConfig("/"), GeocodingApi.Response.class, "k", "v").await();
  }

  @Test
  public void testRetryEventuallyReturnsTheRightException() throws Exception {
    MockResponse errorResponse = new MockResponse();
    errorResponse.setStatus("HTTP/1.1 500 Internal server error");
    errorResponse.setBody("Uh-oh. Server Error.");

    // Enqueue some error responses.
    for (int i = 0; i < 10; i++) {
      server.enqueue(errorResponse);
    }
    server.start();

    // Wire the mock web server to the context
    setMockBaseUrl();
    builder.retryTimeout(5, TimeUnit.SECONDS);

    try {
      builder.build().get(new ApiConfig("/"), GeocodingApi.Response.class, "k", "v").await();
    } catch (IOException ioe) {
      // Ensure the message matches the status line in the mock responses.
      assertEquals("Server Error: 500 Internal server error", ioe.getMessage());
      return;
    }
    fail("Internal server error was expected but not observed.");
  }

  @Test
  public void testQueryParamsHaveOrderPreserved() throws Exception {
    // This test is important for APIs (such as the speed limits API) where multiple parameters
    // must be provided with the same name with order preserved.

    MockResponse response = new MockResponse();
    response.setResponseCode(200);
    response.setBody("{}");

    server.enqueue(response);
    server.start();

    setMockBaseUrl();
    builder
        .build()
        .get(new ApiConfig("/"), GeocodingApi.Response.class, "a", "1", "a", "2", "a", "3")
        .awaitIgnoreError();

    server.shutdown();
    RecordedRequest request = server.takeRequest();
    String path = request.getPath();
    assertTrue(path.contains("a=1&a=2&a=3"));
  }

  @Test
  public void testToggleIfExceptionIsAllowedToRetry() throws Exception {
    // Enqueue some error responses, although only the first should be used because the response's
    // exception is not allowed to be retried.
    MockResponse overQueryLimitResponse = new MockResponse();
    overQueryLimitResponse.setStatus("HTTP/1.1 400 Internal server error");
    overQueryLimitResponse.setBody(TestUtils.retrieveBody("OverQueryLimitResponse.json"));
    server.enqueue(overQueryLimitResponse);
    server.enqueue(overQueryLimitResponse);
    server.enqueue(overQueryLimitResponse);
    server.start();

    builder.retryTimeout(1, TimeUnit.MILLISECONDS);
    builder.maxRetries(10);
    builder.setIfExceptionIsAllowedToRetry(OverQueryLimitException.class, false);

    setMockBaseUrl();

    try {
      builder
          .build()
          .get(new ApiConfig("/"), GeocodingApi.Response.class, "any-key", "any-value")
          .await();
    } catch (OverQueryLimitException e) {
      assertEquals(1, server.getRequestCount());
      return;
    }

    fail("OverQueryLimitException was expected but not observed.");
  }

  @Test
  public void testSingleExperienceId() {
    final String experienceId = "experienceId";
    final GeoApiContext context = builder.experienceId(experienceId).build();
    assertEquals(experienceId, context.getExperienceId());
  }

  @Test
  public void testMultipleExperienceId() {
    final String experienceId1 = "experienceId1";
    final String experienceId2 = "experienceId2";
    final GeoApiContext context = builder.experienceId(experienceId1, experienceId2).build();
    assertEquals(experienceId1 + "," + experienceId2, context.getExperienceId());
  }

  @Test
  public void testNoExperienceId() {
    final GeoApiContext context = builder.build();
    assertNull(context.getExperienceId());
  }

  @Test
  public void testClearingExperienceId() {
    final String experienceId = "experienceId";
    final GeoApiContext context = builder.experienceId(experienceId).build();
    assertEquals(experienceId, context.getExperienceId());

    context.clearExperienceId();
    assertNull(context.getExperienceId());
  }

  @Test
  public void testExperienceIdIsInHeader() throws Exception {
    final String experienceId = "exp1";
    final RecordedRequest request = makeMockRequest(experienceId);
    assertEquals(experienceId, request.getHeader(HttpHeaders.X_GOOG_MAPS_EXPERIENCE_ID));
  }

  @Test
  public void testExperienceIdNotInHeader() throws Exception {
    final RecordedRequest request = makeMockRequest();
    final String value = request.getHeader(HttpHeaders.X_GOOG_MAPS_EXPERIENCE_ID);
    assertNull(value);
  }

  @Test
  public void testExperienceIdSample() {
    // [START maps_experience_id]
    final String experienceId = UUID.randomUUID().toString();

    // instantiate context with experience id
    final GeoApiContext context =
        new GeoApiContext.Builder().apiKey("AIza-Maps-API-Key").experienceId(experienceId).build();

    // clear the current experience id
    context.clearExperienceId();

    // set a new experience id
    final String otherExperienceId = UUID.randomUUID().toString();
    context.setExperienceId(experienceId, otherExperienceId);

    // make API request, the client will set the header
    // X-GOOG-MAPS-EXPERIENCE-ID: experienceId,otherExperienceId

    // get current experience id
    final String ids = context.getExperienceId();
    // [END maps_experience_id]

    assertEquals(experienceId + "," + otherExperienceId, ids);
  }

  @SuppressWarnings("unchecked")
  private RecordedRequest makeMockRequest(String... experienceId) throws Exception {
    // Set up a mock request
    ApiResponse<Object> fakeResponse = mock(ApiResponse.class);
    String path = "/";
    Map<String, List<String>> params = new HashMap<>();
    params.put("key", Collections.singletonList("value"));

    // Set up the fake web server
    server.enqueue(new MockResponse());
    server.start();
    setMockBaseUrl();

    // Build & execute the request using our context
    final GeoApiContext context = builder.experienceId(experienceId).build();
    context.get(new ApiConfig(path), fakeResponse.getClass(), params).awaitIgnoreError();

    // Read the header
    server.shutdown();
    return server.takeRequest();
  }

  @Test
  public void testShutdown() throws InterruptedException {
    GeoApiContext context = builder.build();
    final Thread delayThread = findLastThreadByName("RateLimitExecutorDelayThread");
    assertNotNull(
        "Delay thread should be created in constructor of RateLimitExecutorService", delayThread);
    assertTrue(
        "Delay thread should start in constructor of RateLimitExecutorService",
        delayThread.isAlive());
    // this is needed to make sure that delay thread has reached queue.take()
    delayThread.join(10);
    context.shutdown();
    delayThread.join(10);
    assertFalse(delayThread.isAlive());
  }
}
