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

import com.google.gson.FieldNamingPolicy;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.ExceptionResult;
import com.google.maps.internal.OkHttpPendingResult;
import com.google.maps.internal.RateLimitExecutorService;
import com.google.maps.internal.UrlSigner;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gavin.Lin
 */
public abstract class ApiContext {
  
  private static final String VERSION = "@VERSION@";  // Populated by the build script
  private static final String USER_AGENT = "GoogleGeoApiClientJava/" + VERSION;
  private static final int DEFAULT_BACKOFF_TIMEOUT_MILLIS = 60 * 1000; // 60s

  protected String host;
  protected String apiKey;
  protected String clientId;
  protected UrlSigner urlSigner;
  protected final OkHttpClient client = new OkHttpClient();
  protected final RateLimitExecutorService rateLimitExecutorService;

  private static Logger log = Logger.getLogger(ApiContext.class.getName());
  protected long errorTimeout = DEFAULT_BACKOFF_TIMEOUT_MILLIS;

  public ApiContext(String url) {
    rateLimitExecutorService = new RateLimitExecutorService();
    client.setDispatcher(new Dispatcher(rateLimitExecutorService));
    host = url;
  }

  public <T, R extends ApiResponse<T>> PendingResult<T> get(Class<R> clazz, String path,
          Map<String, String> params) {
    StringBuilder query = new StringBuilder();

    for (Map.Entry<String, String> param : params.entrySet()) {
      query.append('&').append(param.getKey()).append("=");
      try {
        query.append(URLEncoder.encode(param.getValue(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        return new ExceptionResult<T>(e);
      }
    }
    return getWithPath(clazz, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES, path,
            query.toString());
  }

  public <T, R extends ApiResponse<T>> PendingResult<T> get(Class<R> clazz, String path,
          String... params) {
    return get(clazz, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES, path, params);
  }

  public <T, R extends ApiResponse<T>> PendingResult<T> get(Class<R> clazz,
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
        return new ExceptionResult<T>(e);
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
        return new ExceptionResult<T>(e);
      }
    }

    Request req = new Request.Builder()
            .get()
            .header("User-Agent", USER_AGENT)
            .url(host + url).build();

    log.log(Level.INFO, "Request: {0}", host + url);

    return new OkHttpPendingResult<T, R>(req, client, clazz, fieldNamingPolicy, errorTimeout);
  }

  public <T, R extends ApiResponse<T>> PendingResult<T> post(Class<R> clazz, String path,
          Map<String, String> params, MediaType contentType, Object body) {
    StringBuilder query = new StringBuilder();

    for (Map.Entry<String, String> param : params.entrySet()) {
      query.append('&').append(param.getKey()).append("=");
      try {
        query.append(URLEncoder.encode(param.getValue(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        return new ExceptionResult<T>(e);
      }
    }

    RequestBody requestBody = RequestBody.create(contentType, body.toString());

    return postWithBody(clazz, path, query.toString(), requestBody);
  }

  private <T, R extends ApiResponse<T>> PendingResult<T> postWithBody(Class<R> clazz, String path,
          String encodedPath, RequestBody body) {
    checkContext();

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
        return new ExceptionResult<T>(e);
      }
    }

    Request req = new Request.Builder()
            .post(body)
            .header("User-Agent", USER_AGENT)
            .url(host + url).build();

    log.log(Level.INFO, "Request: {0}", host + url);

    return new OkHttpPendingResult<T, R>(req, client, clazz, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES, errorTimeout);
  }

  void checkContext() {
    if (urlSigner == null && apiKey == null) {
      throw new IllegalStateException(
              "Must provide either API key or Maps for Work credentials.");
    }
    if (urlSigner == null && !apiKey.startsWith("AIza")) {
      throw new IllegalStateException("Invalid API key.");
    }
  }

  /**
   * Override the base URL of the API endpoint. Useful only for testing.
   *
   * @param baseUrl The URL to use, without a trailing slash, e.g. https://maps.googleapis.com
   */
  protected abstract ApiContext setBaseUrl(String baseUrl);
  
  public abstract ApiContext setApiKey(String apiKey);

  /**
   * Sets the default connect timeout for new connections. A value of 0 means no timeout.
   *
   * @see java.net.URLConnection#setConnectTimeout(int)
   */
  public abstract ApiContext setConnectTimeout(long timeout, TimeUnit unit);

  public abstract ApiContext setEnterpriseCredentials(String clientId, String cryptographicSecret);

  /**
   * Sets the maximum number of queries that will be executed during a 1 second interval. The default is 10. A minimum
   * interval between requests will also be enforced, set to 1/(2 * {@code maxQps}).
   */
  public abstract ApiContext setQueryRateLimit(int maxQps);

  /**
   * Sets the rate at which queries are executed.
   *
   * @param maxQps The maximum number of queries to execute per second.
   * @param minimumInterval The minimum amount of time, in milliseconds, to pause between requests. Note that this pause
   * only occurs if the amount of time between requests has not elapsed naturally.
   */
  public abstract ApiContext setQueryRateLimit(int maxQps, int minimumInterval);

  /**
   * Sets the default read timeout for new connections. A value of 0 means no timeout.
   *
   * @see java.net.URLConnection#setReadTimeout(int)
   */
  public abstract ApiContext setReadTimeout(long timeout, TimeUnit unit);

  /**
   * Sets the time limit for which retry-able errors will be retried. Defaults to 60 seconds. Set to zero to disable.
   */
  public abstract ApiContext setRetryTimeout(long timeout, TimeUnit unit);

  /**
   * Sets the default write timeout for new connections. A value of 0 means no timeout.
   */
  public abstract ApiContext setWriteTimeout(long timeout, TimeUnit unit);

}
