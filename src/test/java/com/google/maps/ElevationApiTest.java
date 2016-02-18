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

import com.google.maps.errors.InvalidRequestException;
import com.google.maps.errors.RequestDeniedException;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;

@Category(MediumTests.class)
public class ElevationApiTest {

  private MockWebServer server = new MockWebServer();
  private GeoApiContext context = new GeoApiContext().setApiKey("AIzaFakeKey");

  private void setMockBaseUrl() {
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());
  }

  @After
  public void tearDown() throws Exception {
    // Need to shut the server down here as we're using expected exceptions
    server.shutdown();
  }

  @Test(expected = InvalidRequestException.class)
  public void testGetByPointThrowsInvalidRequestExceptionFromResponse() throws Exception {
    // Queue up an invalid response
    MockResponse errorResponse = new MockResponse();
    errorResponse.setBody(""
        + "{\n"
        + "   \"routes\" : [],\n"
        + "   \"status\" : \"INVALID_REQUEST\"\n"
        + "}");
    server.enqueue(errorResponse);
    server.play();

    setMockBaseUrl();
    // This should throw the InvalidRequestException
    ElevationApi.getByPoint(context, new LatLng(0, 0)).await();
  }

  @Test(expected = RequestDeniedException.class)
  public void testGetByPointsThrowsRequestDeniedExceptionFromResponse() throws Exception {
    // Queue up an invalid response
    MockResponse errorResponse = new MockResponse();
    errorResponse.setBody(""
        + "{\n"
        + "   \"routes\" : [],\n"
        + "   \"status\" : \"REQUEST_DENIED\",\n"
        + "   \"errorMessage\" : \"Can't do the thing\"\n"
        + "}");
    server.enqueue(errorResponse);
    server.play();

    setMockBaseUrl();
    // This should throw the RequestDeniedException
    ElevationApi.getByPoints(context, new EncodedPolyline(Collections.singletonList(new LatLng(0, 0)))).await();
  }
}
