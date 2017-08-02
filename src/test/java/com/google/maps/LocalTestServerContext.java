/*
 * Copyright 2017 Google Inc. All rights reserved.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;

/** Local test mock server for unit tests. */
public class LocalTestServerContext implements AutoCloseable {

  private final MockWebServer server;
  public final GeoApiContext context;
  private RecordedRequest request = null;
  private List<NameValuePair> params = null;

  LocalTestServerContext(String responseBody) throws IOException {
    this.server = new MockWebServer();
    MockResponse response = new MockResponse();
    response.setBody(responseBody);
    server.enqueue(response);
    server.start();

    this.context =
        new GeoApiContext.Builder()
            .apiKey("AIzaFakeKey")
            .baseUrlForTesting("http://127.0.0.1:" + server.getPort())
            .build();
  }

  private List<NameValuePair> parseQueryParamsFromRequestLine(String requestLine)
      throws URISyntaxException {
    // Extract the URL part from the HTTP request line
    String[] chunks = requestLine.split("\\s");
    String url = chunks[1];

    return URLEncodedUtils.parse(new URI(url), Charset.forName("UTF-8"));
  }

  private void takeRequest() throws InterruptedException {
    if (this.request == null) this.request = server.takeRequest();
  }

  public JSONObject requestBody() throws InterruptedException {
    this.takeRequest();

    return new JSONObject(request.getBody().readUtf8());
  }

  private List<NameValuePair> actualParams() throws InterruptedException, URISyntaxException {
    this.takeRequest();
    return parseQueryParamsFromRequestLine(request.getRequestLine());
  }

  public String path() throws InterruptedException {
    this.takeRequest();
    return request.getPath().split("\\?")[0];
  }

  void assertParamValue(String expectedValue, String paramName)
      throws URISyntaxException, InterruptedException {
    if (this.params == null) {
      this.params = this.actualParams();
    }
    boolean paramFound = false;
    for (NameValuePair pair : params) {
      if (pair.getName().equals(paramName)) {
        paramFound = true;
        assertEquals(expectedValue, pair.getValue());
      }
    }
    assertTrue(paramFound);
  }

  @Override
  public void close() {
    try {
      server.shutdown();
    } catch (IOException e) {
      System.err.println("Failed to close server: " + e);
    }
  }
}
