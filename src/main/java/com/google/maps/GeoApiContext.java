/*
 * Copyright 2014 The Java Client for Google Maps Services Authors. All Rights Reserved.
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
import com.google.maps.internal.ExceptionResult;
import com.google.maps.internal.OkHttpPendingResult;
import com.google.maps.internal.RateLimitExecutorService;
import com.google.maps.internal.UrlSigner;

import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The entry point for making requests against the Google Geo APIs.
 */
public class GeoApiContext {
  private static final String VERSION = "@VERSION@";  // Populated by the build script
  private static final String DEFAULT_HOST = "https://maps.googleapis.com";
  private static final String USER_AGENT = "GoogleGeoApiClientJava/" + VERSION;
  private static final int DEFAULT_QUERIES_PER_SECOND = 10;
  private static final int DEFAULT_BACKOFF_TIMEOUT_MILLIS = 60 * 1000; // 60s

  private String host = DEFAULT_HOST;
  private String apiKey;
  private String clientId;
  private UrlSigner urlSigner;
  private OkHttpClient client = new OkHttpClient();

  private static Logger log = Logger.getLogger(GeoApiContext.class.getName());
  private long errorTimeout = DEFAULT_BACKOFF_TIMEOUT_MILLIS;

  /**
   * Construct a {@code GeoApiContext} with default Queries Per Second rate limit.
   */
  public GeoApiContext() {
    this(DEFAULT_QUERIES_PER_SECOND);
  }

  /**
   * Construct a {@code GeoApiContext} with {@code queriesPerSecond} rate limit.
   */
  public GeoApiContext(int queriesPerSecond) {
    this(queriesPerSecond, (int) ((1.0 / (2.0 * queriesPerSecond)) * 1000.0));
  }

  /**
   * Construct a {@code GeoApiContext} with {@code queriesPerSecond} rate limit, and a minimum delay
   * between queries of {@code minimumDelay}.
   */
  public GeoApiContext(int queriesPerSecond, int minimumDelay) {
    log.log(Level.INFO, "Configuring rate limit at QPS: " + queriesPerSecond + ", minimum delay "
        + minimumDelay + "ms between requests");
    client.setDispatcher(new Dispatcher(
        new RateLimitExecutorService(queriesPerSecond, minimumDelay)));
  }

  <T, R extends ApiResponse<T>> PendingResult<T> get(Class<R> clazz, String path,
      Map<String, String> params) {
    StringBuilder query = new StringBuilder();

    for (Map.Entry<String, String> param : params.entrySet()) {
      query.append('&').append(param.getKey()).append("=");
      try {
        query.append(URLEncoder.encode(param.getValue(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        return new ExceptionResult<>(e);
      }
    }
    return getWithPath(clazz, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES, path,
        query.toString());
  }

  <T, R extends ApiResponse<T>> PendingResult<T> get(Class<R> clazz, String path,
      String... params) {
    return get(clazz, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES, path, params);
  }

  <T, R extends ApiResponse<T>> PendingResult<T> get(Class<R> clazz,
      FieldNamingPolicy fieldNamingPolicy, String path, String... params) {
    if (params.length % 2 != 0) {
      throw new IllegalArgumentException("Params must be matching key/value pairs.");
    }

    StringBuilder query = new StringBuilder();

    for (int i = 0; i < params.length; i++) {
      query.append('&').append(params[i]).append('=');
      i++;

      // URL-encode the parameter.
      try {
        query.append(URLEncoder.encode(params[i], "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        return new ExceptionResult<>(e);
      }
    }

    return getWithPath(clazz, fieldNamingPolicy, path, query.toString());
  }

  private <T, R extends ApiResponse<T>> PendingResult<T> getWithPath(Class<R> clazz,
      FieldNamingPolicy fieldNamingPolicy, String path, String encodedPath) {
    checkContext();
    if (!encodedPath.startsWith("&")) {
      throw new IllegalArgumentException("encodedPath must start with &");
    }

    StringBuilder url = new StringBuilder(path);
    if (clientId != null) {
      url.append("?client=").append(clientId);
    } else {
      url.append("?key=").append(apiKey);
    }
    url.append(encodedPath);

    if (clientId != null) {
      try {
        String signature = urlSigner.getSignature(url.toString());
        url.append("&signature=").append(signature);
      } catch (Exception e) {
        return new ExceptionResult<>(e);
      }
    }

    Request req = new Request.Builder()
        .get()
        .header("User-Agent", USER_AGENT)
        .url(host + url).build();

    log.log(Level.INFO, "Request: {0}", host + url);

    return new OkHttpPendingResult<>(req, client, clazz, fieldNamingPolicy, errorTimeout);
  }

  private void checkContext() {
    if (urlSigner == null && apiKey == null) {
      throw new IllegalStateException(
          "Must provide either API key or Maps for Business credentials.");
    }
    if (urlSigner == null && !apiKey.startsWith("AIza")) {
      throw new IllegalStateException("Invalid API key.");
    }
  }

  /**
   * Override the base URL of the API endpoint. Useful only for testing.
   * @param baseUrl  The URL to use, without a trailing slash, e.g. https://maps.googleapis.com
   */
  GeoApiContext setBaseUrl(String baseUrl) {
    host = baseUrl;
    return this;
  }

  public GeoApiContext setApiKey(String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  public GeoApiContext setEnterpriseCredentials(String clientId, String cryptographicSecret) {
    this.clientId = clientId;
    this.urlSigner = new UrlSigner(cryptographicSecret);
    return this;
  }

  /**
   * Sets the default connect timeout for new connections. A value of 0 means no timeout.
   *
   * @see java.net.URLConnection#setConnectTimeout(int)
   */
  public GeoApiContext setConnectTimeout(long timeout, TimeUnit unit) {
    client.setConnectTimeout(timeout, unit);
    return this;
  }

  /**
   * Sets the default read timeout for new connections. A value of 0 means no timeout.
   *
   * @see java.net.URLConnection#setReadTimeout(int)
   */
  public GeoApiContext setReadTimeout(long timeout, TimeUnit unit) {
    client.setReadTimeout(timeout, unit);
    return this;
  }

  /**
   * Sets the default write timeout for new connections. A value of 0 means no timeout.
   */
  public GeoApiContext setWriteTimeout(long timeout, TimeUnit unit) {
    client.setWriteTimeout(timeout, unit);
    return this;
  }

  /**
   * Sets the time limit for which retry-able errors will be retried. Defaults to 60 seconds. Set
   * to zero to disable.
   */
  public GeoApiContext setRetryTimeout(long timeout, TimeUnit unit) {
    this.errorTimeout = unit.toMillis(timeout);
    return this;
  }
}
