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
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.ExceptionResult;
import com.google.maps.internal.UrlSigner;

import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The entry point for making requests against the Google Geo APIs.
 */
public class GeoApiContext {
  private static final String VERSION = "@VERSION@";  // Populated by the build script
  private static final String USER_AGENT = "GoogleGeoApiClientJava/" + VERSION;
  private static final int DEFAULT_BACKOFF_TIMEOUT_MILLIS = 60 * 1000; // 60s

  private String baseUrlOverride;
  private String apiKey;
  private String clientId;
  private UrlSigner urlSigner;
  private String channel;
  private RequestHandler requestHandler;


  /**
   * RequestHandler is the service provider interface that enables requests to be handled via
   * switchable back ends. There are supplied implementations of this interface for both
   * OkHttp and Google App Engine's URL Fetch API.
   *
   * @see OkHttpRequestHandler
   * @see GaeRequestHandler
   */
  public interface RequestHandler {
    <T, R extends ApiResponse<T>> PendingResult<T> handle(String hostName, String url, String userAgent, Class<R> clazz, FieldNamingPolicy fieldNamingPolicy, long errorTimeout);
    void setConnectTimeout(long timeout, TimeUnit unit);
    void setReadTimeout(long timeout, TimeUnit unit);
    void setWriteTimeout(long timeout, TimeUnit unit);
    void setQueriesPerSecond(int maxQps);
    void setQueriesPerSecond(int maxQps, int minimumInterval);
    void setProxy(Proxy proxy);
  }

  private static final Logger LOG = Logger.getLogger(GeoApiContext.class.getName());
  private long errorTimeout = DEFAULT_BACKOFF_TIMEOUT_MILLIS;

  /**
   * Construct a GeoApiContext with OkHttp.
   */
  public GeoApiContext() {
    this(new OkHttpRequestHandler());
  }

  /**
   * Construct a GeoApiContext with the specified strategy for handling requests.
   *
   * @see OkHttpRequestHandler
   * @see GaeRequestHandler
   *
   * @param requestHandler How to handle URL requests to the Google Maps APIs.
   */
  public GeoApiContext(RequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }

  <T, R extends ApiResponse<T>> PendingResult<T> get(ApiConfig config, Class<? extends R> clazz,
                                                     Map<String, String> params) {
    if (channel != null && !channel.isEmpty() && !params.containsKey("channel")) {
      params.put("channel", channel);
    }

    StringBuilder query = new StringBuilder();

    for (Map.Entry<String, String> param : params.entrySet()) {
      query.append('&').append(param.getKey()).append("=");
      try {
        query.append(URLEncoder.encode(param.getValue(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        return new ExceptionResult<T>(e);
      }
    }

    return getWithPath(clazz, config.fieldNamingPolicy, config.hostName, config.path,
        config.supportsClientId, query.toString());
  }

  <T, R extends ApiResponse<T>> PendingResult<T> get(ApiConfig config, Class<? extends R> clazz,
                                                     String... params) {
    if (params.length % 2 != 0) {
      throw new IllegalArgumentException("Params must be matching key/value pairs.");
    }

    StringBuilder query = new StringBuilder();

    boolean channelSet = false;
    for (int i = 0; i < params.length; i++) {
      if (params[i].equals("channel")) {
        channelSet = true;
      }
      query.append('&').append(params[i]).append('=');
      i++;

      // URL-encode the parameter.
      try {
        query.append(URLEncoder.encode(params[i], "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        return new ExceptionResult<T>(e);
      }
    }

    // Channel can be supplied per-request or per-context. We prioritize it from the request, so if it's not provided there, provide it here
    if (!channelSet && channel != null && !channel.isEmpty()) {
      query.append("&channel=").append(channel);
    }

    return getWithPath(clazz, config.fieldNamingPolicy, config.hostName, config.path,
        config.supportsClientId, query.toString());
  }

  private <T, R extends ApiResponse<T>> PendingResult<T> getWithPath(Class<R> clazz,
                                                                     FieldNamingPolicy fieldNamingPolicy, String hostName, String path,
                                                                     boolean canUseClientId, String encodedPath) {
    checkContext(canUseClientId);
    if (!encodedPath.startsWith("&")) {
      throw new IllegalArgumentException("encodedPath must start with &");
    }

    StringBuilder url = new StringBuilder(path);
    if (canUseClientId && clientId != null) {
      url.append("?client=").append(clientId);
    } else {
      url.append("?key=").append(apiKey);
    }
    url.append(encodedPath);

    if (canUseClientId && clientId != null) {
      try {
        String signature = urlSigner.getSignature(url.toString());
        url.append("&signature=").append(signature);
      } catch (Exception e) {
        return new ExceptionResult<T>(e);
      }
    }

    if (baseUrlOverride != null) {
      hostName = baseUrlOverride;
    }

    return requestHandler.handle(hostName, url.toString(), USER_AGENT, clazz, fieldNamingPolicy, errorTimeout);
  }

  private void checkContext(boolean canUseClientId) {
    if (urlSigner == null && apiKey == null) {
      throw new IllegalStateException(
          "Must provide either API key or Maps for Work credentials.");
    } else if (!canUseClientId && apiKey == null) {
      throw new IllegalStateException(
          "API does not support client ID & secret - you must provide a key");
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
  GeoApiContext setBaseUrlForTesting(String baseUrl) {
    baseUrlOverride = baseUrl;
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
   * Sets the default channel for requests (can be overridden by requests).  Only useful for Google
   * Maps for Work clients.
   *
   * @param channel The channel to use for analytics
   */
  public GeoApiContext setChannel(String channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Sets the default connect timeout for new connections. A value of 0 means no timeout.
   *
   * @see java.net.URLConnection#setConnectTimeout(int)
   */
  public GeoApiContext setConnectTimeout(long timeout, TimeUnit unit) {
    requestHandler.setConnectTimeout(timeout, unit);
    return this;
  }

  /**
   * Sets the default read timeout for new connections. A value of 0 means no timeout.
   *
   * @see java.net.URLConnection#setReadTimeout(int)
   */
  public GeoApiContext setReadTimeout(long timeout, TimeUnit unit) {
    requestHandler.setReadTimeout(timeout, unit);
    return this;
  }

  /**
   * Sets the default write timeout for new connections. A value of 0 means no timeout.
   */
  public GeoApiContext setWriteTimeout(long timeout, TimeUnit unit) {
    requestHandler.setWriteTimeout(timeout, unit);
    return this;
  }

  /**
   * Sets the cumulative time limit for which retry-able errors will be retried. Defaults to 60
   * seconds. Set to zero to retry requests forever.
   */
  public GeoApiContext setRetryTimeout(long timeout, TimeUnit unit) {
    this.errorTimeout = unit.toMillis(timeout);
    return this;
  }

  /**
   * Sets the maximum number of queries that will be executed during a 1 second interval. The
   * default is 10. A minimum interval between requests will also be enforced, set to 1/(2 * {@code
   * maxQps}).
   */
  public GeoApiContext setQueryRateLimit(int maxQps) {
    requestHandler.setQueriesPerSecond(maxQps);
    return this;
  }

  /**
   * Sets the rate at which queries are executed.
   *
   * @param maxQps          The maximum number of queries to execute per second.
   * @param minimumInterval The minimum amount of time, in milliseconds, to pause between requests.
   *                        Note that this pause only occurs if the amount of time between requests
   *                        has not elapsed naturally.
   */
  public GeoApiContext setQueryRateLimit(int maxQps, int minimumInterval) {
    requestHandler.setQueriesPerSecond(maxQps, minimumInterval);
    return this;
  }

  /**
   * Sets the proxy for new connections.
   *
   * @param proxy The proxy to be used by the underlying HTTP client.
   */
  public GeoApiContext setProxy(Proxy proxy) {
    requestHandler.setProxy(proxy == null ? Proxy.NO_PROXY : proxy);
    return this;
  }
}
