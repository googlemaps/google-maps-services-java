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

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.maps.GeoApiContext.RequestHandler;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.ExceptionsAllowedToRetry;
import com.google.maps.internal.GaePendingResult;
import com.google.maps.internal.HttpHeaders;
import com.google.maps.metrics.RequestMetrics;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A strategy for handling URL requests using Google App Engine's URL Fetch API.
 *
 * @see com.google.maps.GeoApiContext.RequestHandler
 */
public class GaeRequestHandler implements GeoApiContext.RequestHandler {
  private static final Logger LOG = LoggerFactory.getLogger(GaeRequestHandler.class.getName());
  private final URLFetchService client = URLFetchServiceFactory.getURLFetchService();

  /* package */ GaeRequestHandler() {}

  @Override
  public <T, R extends ApiResponse<T>> PendingResult<T> handle(
      String hostName,
      String url,
      String userAgent,
      String experienceIdHeaderValue,
      Class<R> clazz,
      FieldNamingPolicy fieldNamingPolicy,
      long errorTimeout,
      Integer maxRetries,
      ExceptionsAllowedToRetry exceptionsAllowedToRetry,
      RequestMetrics metrics) {
    FetchOptions fetchOptions = FetchOptions.Builder.withDeadline(10);
    HTTPRequest req;
    try {
      req = new HTTPRequest(new URL(hostName + url), HTTPMethod.POST, fetchOptions);
      if (experienceIdHeaderValue != null) {
        req.setHeader(
            new HTTPHeader(HttpHeaders.X_GOOG_MAPS_EXPERIENCE_ID, experienceIdHeaderValue));
      }
    } catch (MalformedURLException e) {
      LOG.error("Request: {}{}", hostName, url, e);
      throw (new RuntimeException(e));
    }

    return new GaePendingResult<>(
        req,
        client,
        clazz,
        fieldNamingPolicy,
        errorTimeout,
        maxRetries,
        exceptionsAllowedToRetry,
        metrics);
  }

  @Override
  public <T, R extends ApiResponse<T>> PendingResult<T> handlePost(
      String hostName,
      String url,
      String payload,
      String userAgent,
      String experienceIdHeaderValue,
      Class<R> clazz,
      FieldNamingPolicy fieldNamingPolicy,
      long errorTimeout,
      Integer maxRetries,
      ExceptionsAllowedToRetry exceptionsAllowedToRetry,
      RequestMetrics metrics) {
    FetchOptions fetchOptions = FetchOptions.Builder.withDeadline(10);
    HTTPRequest req = null;
    try {
      req = new HTTPRequest(new URL(hostName + url), HTTPMethod.POST, fetchOptions);
      req.setHeader(new HTTPHeader("Content-Type", "application/json; charset=utf-8"));
      if (experienceIdHeaderValue != null) {
        req.setHeader(
            new HTTPHeader(HttpHeaders.X_GOOG_MAPS_EXPERIENCE_ID, experienceIdHeaderValue));
      }
      req.setPayload(payload.getBytes(UTF_8));
    } catch (MalformedURLException e) {
      LOG.error("Request: {}{}", hostName, url, e);
      throw (new RuntimeException(e));
    }

    return new GaePendingResult<>(
        req,
        client,
        clazz,
        fieldNamingPolicy,
        errorTimeout,
        maxRetries,
        exceptionsAllowedToRetry,
        metrics);
  }

  @Override
  public void shutdown() {
    // do nothing
  }

  /** Builder strategy for constructing {@code GaeRequestHandler}. */
  public static class Builder implements GeoApiContext.RequestHandler.Builder {

    @Override
    public Builder connectTimeout(long timeout, TimeUnit unit) {
      throw new RuntimeException("connectTimeout not implemented for Google App Engine");
    }

    @Override
    public Builder readTimeout(long timeout, TimeUnit unit) {
      throw new RuntimeException("readTimeout not implemented for Google App Engine");
    }

    @Override
    public Builder writeTimeout(long timeout, TimeUnit unit) {
      throw new RuntimeException("writeTimeout not implemented for Google App Engine");
    }

    @Override
    public Builder queriesPerSecond(int maxQps) {
      throw new RuntimeException("queriesPerSecond not implemented for Google App Engine");
    }

    @Override
    public Builder proxy(Proxy proxy) {
      throw new RuntimeException("setProxy not implemented for Google App Engine");
    }

    @Override
    public Builder proxyAuthentication(String proxyUserName, String proxyUserPassword) {
      throw new RuntimeException("setProxyAuthentication not implemented for Google App Engine");
    }

    @Override
    public RequestHandler build() {
      return new GaeRequestHandler();
    }
  }
}
