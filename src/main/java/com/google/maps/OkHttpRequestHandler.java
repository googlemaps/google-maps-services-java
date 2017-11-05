/*
 * Copyright 2016 Google Inc. All rights reserved.
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

import com.google.gson.FieldNamingPolicy;
import com.google.maps.GeoApiContext.RequestHandler;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.ExceptionsAllowedToRetry;
import com.google.maps.internal.OkHttpPendingResult;
import com.google.maps.internal.RateLimitExecutorService;
import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Dispatcher;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

/**
 * A strategy for handling URL requests using OkHttp.
 *
 * @see com.google.maps.GeoApiContext.RequestHandler
 */
public class OkHttpRequestHandler implements GeoApiContext.RequestHandler {
  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private final OkHttpClient client;
  private final ExecutorService executorService;

  /* package */ OkHttpRequestHandler(OkHttpClient client, ExecutorService executorService) {
    this.client = client;
    this.executorService = executorService;
  }

  @Override
  public <T, R extends ApiResponse<T>> PendingResult<T> handle(
      String hostName,
      String url,
      String userAgent,
      Class<R> clazz,
      FieldNamingPolicy fieldNamingPolicy,
      long errorTimeout,
      Integer maxRetries,
      ExceptionsAllowedToRetry exceptionsAllowedToRetry) {
    Request req =
        new Request.Builder().get().header("User-Agent", userAgent).url(hostName + url).build();

    return new OkHttpPendingResult<T, R>(
        req, client, clazz, fieldNamingPolicy, errorTimeout, maxRetries, exceptionsAllowedToRetry);
  }

  @Override
  public <T, R extends ApiResponse<T>> PendingResult<T> handlePost(
      String hostName,
      String url,
      String payload,
      String userAgent,
      Class<R> clazz,
      FieldNamingPolicy fieldNamingPolicy,
      long errorTimeout,
      Integer maxRetries,
      ExceptionsAllowedToRetry exceptionsAllowedToRetry) {
    RequestBody body = RequestBody.create(JSON, payload);
    Request req =
        new Request.Builder()
            .post(body)
            .header("User-Agent", userAgent)
            .url(hostName + url)
            .build();

    return new OkHttpPendingResult<T, R>(
        req, client, clazz, fieldNamingPolicy, errorTimeout, maxRetries, exceptionsAllowedToRetry);
  }

  public void shutdown() {
    executorService.shutdown();
  }

  /** Builder strategy for constructing an {@code OkHTTPRequestHandler}. */
  public static class Builder implements GeoApiContext.RequestHandler.Builder {
    private final OkHttpClient.Builder builder;
    private final RateLimitExecutorService rateLimitExecutorService;
    private final Dispatcher dispatcher;

    public Builder() {
      builder = new OkHttpClient.Builder();
      rateLimitExecutorService = new RateLimitExecutorService();
      dispatcher = new Dispatcher(rateLimitExecutorService);
      builder.dispatcher(dispatcher);
    }

    @Override
    public void connectTimeout(long timeout, TimeUnit unit) {
      builder.connectTimeout(timeout, unit);
    }

    @Override
    public void readTimeout(long timeout, TimeUnit unit) {
      builder.readTimeout(timeout, unit);
    }

    @Override
    public void writeTimeout(long timeout, TimeUnit unit) {
      builder.writeTimeout(timeout, unit);
    }

    @Override
    public void queriesPerSecond(int maxQps) {
      dispatcher.setMaxRequests(maxQps);
      dispatcher.setMaxRequestsPerHost(maxQps);
      rateLimitExecutorService.setQueriesPerSecond(maxQps);
    }

    @Override
    public void proxy(Proxy proxy) {
      builder.proxy(proxy);
    }

    @Override
    public void proxyAuthentication(String proxyUserName, String proxyUserPassword) {
      final String userName = proxyUserName;
      final String password = proxyUserPassword;

      builder.proxyAuthenticator(
          new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
              String credential = Credentials.basic(userName, password);
              return response
                  .request()
                  .newBuilder()
                  .header("Proxy-Authorization", credential)
                  .build();
            }
          });
    }

    @Override
    public RequestHandler build() {
      OkHttpClient client = builder.build();
      return new OkHttpRequestHandler(client, rateLimitExecutorService);
    }
  }
}
