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
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.OkHttpPendingResult;
import com.google.maps.internal.RateLimitExecutorService;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import java.util.logging.Logger;


/**
 * A strategy for handling URL requests using OkHttp.
 *
 * @see com.google.maps.GeoApiContext.RequestHandler
 */
public class OkHttpRequestHandler implements GeoApiContext.RequestHandler {
  private static final Logger LOG = Logger.getLogger(OkHttpRequestHandler.class.getName());

  private final OkHttpClient client = new OkHttpClient();
  private final RateLimitExecutorService rateLimitExecutorService;

  public OkHttpRequestHandler() {
    rateLimitExecutorService = new RateLimitExecutorService();
    client.setDispatcher(new Dispatcher(rateLimitExecutorService));
  }

  @Override
  public <T, R extends ApiResponse<T>> PendingResult<T> handle(String hostName, String url, String userAgent, Class<R> clazz, FieldNamingPolicy fieldNamingPolicy, long errorTimeout) {
    Request req = new Request.Builder()
        .get()
        .header("User-Agent", userAgent)
        .url(hostName + url).build();

    LOG.log(Level.INFO, "Request: {0}", hostName + url);

    return new OkHttpPendingResult<T, R>(req, client, clazz, fieldNamingPolicy, errorTimeout);
  }

  @Override
  public void setConnectTimeout(long timeout, TimeUnit unit) {
    client.setConnectTimeout(timeout, unit);
  }

  @Override
  public void setReadTimeout(long timeout, TimeUnit unit) {
    client.setReadTimeout(timeout, unit);
  }

  @Override
  public void setWriteTimeout(long timeout, TimeUnit unit) {
    client.setWriteTimeout(timeout, unit);
  }

  @Override
  public void setQueriesPerSecond(int maxQps) {
    rateLimitExecutorService.setQueriesPerSecond(maxQps);
  }

  @Override
  public void setQueriesPerSecond(int maxQps, int minimumInterval) {
    rateLimitExecutorService.setQueriesPerSecond(maxQps, minimumInterval);
  }

  @Override
  public void setProxy(Proxy proxy) {
    client.setProxy(proxy);
  }

}
