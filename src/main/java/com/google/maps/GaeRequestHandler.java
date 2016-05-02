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

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.GaePendingResult;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A strategy for handling URL requests using Google App Engine's URL Fetch API.
 *
 * @see com.google.maps.GeoApiContext.RequestHandler
 */
public class GaeRequestHandler implements GeoApiContext.RequestHandler {
  private static final Logger LOG = Logger.getLogger(GaeRequestHandler.class.getName());
  private final URLFetchService client = URLFetchServiceFactory.getURLFetchService();

  @Override
  public <T, R extends ApiResponse<T>> PendingResult<T> handle(String hostName, String url, String userAgent, Class<R> clazz, FieldNamingPolicy fieldNamingPolicy, long errorTimeout) {
    FetchOptions fetchOptions = FetchOptions.Builder.withDeadline(10);
    HTTPRequest req = null;
    try {
      req = new HTTPRequest(new URL(hostName + url), HTTPMethod.POST, fetchOptions);
    } catch (MalformedURLException e) {
      LOG.log(Level.SEVERE, String.format("Request: %s%s", hostName, url), e);
      throw(new RuntimeException(e));
    }

    return new GaePendingResult<T, R>(req, client, clazz, fieldNamingPolicy, errorTimeout);
  }

  @Override
  public void setConnectTimeout(long timeout, TimeUnit unit) {
    // TODO: Investigate if GAE URL Fetch Service supports setting connection timeout
    throw new RuntimeException("setConnectTimeout not implemented for Google App Engine");
  }

  @Override
  public void setReadTimeout(long timeout, TimeUnit unit) {
    // TODO: Investigate if GAE URL Fetch Service supports setting read timeout
    throw new RuntimeException("setReadTimeout not implemented for Google App Engine");
  }

  @Override
  public void setWriteTimeout(long timeout, TimeUnit unit) {
    // TODO: Investigate if GAE URL Fetch Service supports setting write timeout
    throw new RuntimeException("setWriteTimeout not implemented for Google App Engine");
  }

  @Override
  public void setQueriesPerSecond(int maxQps) {
    // TODO: Investigate if GAE URL Fetch Service supports setting qps
    throw new RuntimeException("setQueriesPerSecond not implemented for Google App Engine");
  }

  @Override
  public void setQueriesPerSecond(int maxQps, int minimumInterval) {
    // TODO: Investigate if GAE URL Fetch Service supports setting qps
    throw new RuntimeException("setQueriesPerSecond not implemented for Google App Engine");
  }

  @Override
  public void setProxy(Proxy proxy) {
    // TODO: Investigate if GAE URL Fetch Service supports setting proxy
    throw new RuntimeException("setProxy not implemented for Google App Engine");
  }
}
