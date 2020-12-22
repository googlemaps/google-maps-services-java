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
import com.google.maps.errors.ApiException;
import com.google.maps.errors.OverQueryLimitException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.ExceptionsAllowedToRetry;
import com.google.maps.internal.HttpHeaders;
import com.google.maps.internal.StringJoin;
import com.google.maps.internal.UrlSigner;
import com.google.maps.metrics.NoOpRequestMetricsReporter;
import com.google.maps.metrics.RequestMetrics;
import com.google.maps.metrics.RequestMetricsReporter;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The entry point for making requests against the Google Geo APIs.
 *
 * <p>Construct this object by using the enclosed {@link GeoApiContext.Builder}.
 *
 * <h3>GeoApiContexts should be shared</h3>
 *
 * GeoApiContext works best when you create a single GeoApiContext instance, or one per API key, and
 * reuse it for all your Google Geo API queries. This is because each GeoApiContext manages its own
 * thread pool, back-end client, and other resources.
 *
 * <p>When you are finished with a GeoApiContext object, you must call {@link #shutdown()} on it to
 * release its resources.
 */
public class GeoApiContext implements Closeable {

  private static final String VERSION = "@VERSION@"; // Populated by the build script
  private static final String USER_AGENT = "GoogleGeoApiClientJava/" + VERSION;
  private static final int DEFAULT_BACKOFF_TIMEOUT_MILLIS = 60 * 1000; // 60s

  private final RequestHandler requestHandler;
  private final String apiKey;
  private final String baseUrlOverride;
  private final String channel;
  private final String clientId;
  private final long errorTimeout;
  private final ExceptionsAllowedToRetry exceptionsAllowedToRetry;
  private final Integer maxRetries;
  private final UrlSigner urlSigner;
  private String experienceIdHeaderValue;
  private final RequestMetricsReporter requestMetricsReporter;

  /* package */
  GeoApiContext(
      RequestHandler requestHandler,
      String apiKey,
      String baseUrlOverride,
      String channel,
      String clientId,
      long errorTimeout,
      ExceptionsAllowedToRetry exceptionsAllowedToRetry,
      Integer maxRetries,
      UrlSigner urlSigner,
      RequestMetricsReporter requestMetricsReporter,
      String... experienceIdHeaderValue) {
    this.requestHandler = requestHandler;
    this.apiKey = apiKey;
    this.baseUrlOverride = baseUrlOverride;
    this.channel = channel;
    this.clientId = clientId;
    this.errorTimeout = errorTimeout;
    this.exceptionsAllowedToRetry = exceptionsAllowedToRetry;
    this.maxRetries = maxRetries;
    this.urlSigner = urlSigner;
    this.requestMetricsReporter = requestMetricsReporter;
    setExperienceId(experienceIdHeaderValue);
  }

  /**
   * standard Java API to reclaim resources
   *
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    shutdown();
  }

  /**
   * The service provider interface that enables requests to be handled via switchable back ends.
   * There are supplied implementations of this interface for both OkHttp and Google App Engine's
   * URL Fetch API.
   *
   * @see OkHttpRequestHandler
   * @see GaeRequestHandler
   */
  public interface RequestHandler {

    <T, R extends ApiResponse<T>> PendingResult<T> handle(
        String hostName,
        String url,
        String userAgent,
        String experienceIdHeaderValue,
        Class<R> clazz,
        FieldNamingPolicy fieldNamingPolicy,
        long errorTimeout,
        Integer maxRetries,
        ExceptionsAllowedToRetry exceptionsAllowedToRetry,
        RequestMetrics metrics);

    <T, R extends ApiResponse<T>> PendingResult<T> handlePost(
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
        RequestMetrics metrics);

    void shutdown();

    /** Builder pattern for {@code GeoApiContext.RequestHandler}. */
    interface Builder {

      Builder connectTimeout(long timeout, TimeUnit unit);

      Builder readTimeout(long timeout, TimeUnit unit);

      Builder writeTimeout(long timeout, TimeUnit unit);

      Builder queriesPerSecond(int maxQps);

      Builder proxy(Proxy proxy);

      Builder proxyAuthentication(String proxyUserName, String proxyUserPassword);

      RequestHandler build();
    }
  }

  /**
   * Sets the value for the HTTP header field name {@link HttpHeaders#X_GOOG_MAPS_EXPERIENCE_ID} to
   * be used on subsequent API calls. Calling this method with {@code null} is equivalent to calling
   * {@link #clearExperienceId()}.
   *
   * @param experienceId The experience ID if set, otherwise null
   */
  public void setExperienceId(String... experienceId) {
    if (experienceId == null || experienceId.length == 0) {
      experienceIdHeaderValue = null;
      return;
    }
    experienceIdHeaderValue = StringJoin.join(",", experienceId);
  }

  /** @return Returns the experience ID if set, otherwise, null */
  public String getExperienceId() {
    return experienceIdHeaderValue;
  }

  /**
   * Clears the experience ID if set the HTTP header field {@link
   * HttpHeaders#X_GOOG_MAPS_EXPERIENCE_ID} will be omitted from subsequent calls.
   */
  public void clearExperienceId() {
    experienceIdHeaderValue = null;
  }

  /**
   * Shut down this GeoApiContext instance, reclaiming resources. After shutdown() has been called,
   * no further queries may be done against this instance.
   */
  public void shutdown() {
    requestHandler.shutdown();
  }

  <T, R extends ApiResponse<T>> PendingResult<T> get(
      ApiConfig config, Class<? extends R> clazz, Map<String, List<String>> params) {
    if (channel != null && !channel.isEmpty() && !params.containsKey("channel")) {
      params.put("channel", Collections.singletonList(channel));
    }

    StringBuilder query = new StringBuilder();

    for (Map.Entry<String, List<String>> param : params.entrySet()) {
      List<String> values = param.getValue();
      for (String value : values) {
        query.append('&').append(param.getKey()).append("=");
        try {
          query.append(URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
          // This should never happen. UTF-8 support is required for every Java implementation.
          throw new IllegalStateException(e);
        }
      }
    }

    return getWithPath(
        clazz,
        config.fieldNamingPolicy,
        config.hostName,
        config.path,
        config.supportsClientId,
        query.toString(),
        requestMetricsReporter.newRequest(config.path));
  }

  <T, R extends ApiResponse<T>> PendingResult<T> get(
      ApiConfig config, Class<? extends R> clazz, String... params) {
    if (params.length % 2 != 0) {
      throw new IllegalArgumentException("Params must be matching key/value pairs.");
    }

    StringBuilder query = new StringBuilder();

    boolean channelSet = false;
    for (int i = 0; i < params.length; i += 2) {
      if (params[i].equals("channel")) {
        channelSet = true;
      }
      query.append('&').append(params[i]).append('=');

      // URL-encode the parameter.
      try {
        query.append(URLEncoder.encode(params[i + 1], "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        // This should never happen. UTF-8 support is required for every Java implementation.
        throw new IllegalStateException(e);
      }
    }

    // Channel can be supplied per-request or per-context. We prioritize it from the request,
    // so if it's not provided there, provide it here
    if (!channelSet && channel != null && !channel.isEmpty()) {
      query.append("&channel=").append(channel);
    }

    return getWithPath(
        clazz,
        config.fieldNamingPolicy,
        config.hostName,
        config.path,
        config.supportsClientId,
        query.toString(),
        requestMetricsReporter.newRequest(config.path));
  }

  <T, R extends ApiResponse<T>> PendingResult<T> post(
      ApiConfig config, Class<? extends R> clazz, Map<String, List<String>> params) {

    checkContext(config.supportsClientId);

    StringBuilder url = new StringBuilder(config.path);
    if (config.supportsClientId && clientId != null) {
      url.append("?client=").append(clientId);
    } else {
      url.append("?key=").append(apiKey);
    }

    if (config.supportsClientId && urlSigner != null) {
      String signature = urlSigner.getSignature(url.toString());
      url.append("&signature=").append(signature);
    }

    String hostName = config.hostName;
    if (baseUrlOverride != null) {
      hostName = baseUrlOverride;
    }

    return requestHandler.handlePost(
        hostName,
        url.toString(),
        params.get("_payload").get(0),
        USER_AGENT,
        experienceIdHeaderValue,
        clazz,
        config.fieldNamingPolicy,
        errorTimeout,
        maxRetries,
        exceptionsAllowedToRetry,
        requestMetricsReporter.newRequest(config.path));
  }

  private <T, R extends ApiResponse<T>> PendingResult<T> getWithPath(
      Class<R> clazz,
      FieldNamingPolicy fieldNamingPolicy,
      String hostName,
      String path,
      boolean canUseClientId,
      String encodedPath,
      RequestMetrics metrics) {
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

    if (canUseClientId && urlSigner != null) {
      String signature = urlSigner.getSignature(url.toString());
      url.append("&signature=").append(signature);
    }

    if (baseUrlOverride != null) {
      hostName = baseUrlOverride;
    }

    return requestHandler.handle(
        hostName,
        url.toString(),
        USER_AGENT,
        experienceIdHeaderValue,
        clazz,
        fieldNamingPolicy,
        errorTimeout,
        maxRetries,
        exceptionsAllowedToRetry,
        metrics);
  }

  private void checkContext(boolean canUseClientId) {
    if (urlSigner == null && apiKey == null) {
      throw new IllegalStateException("Must provide either API key or Maps for Work credentials.");
    } else if (!canUseClientId && apiKey == null) {
      throw new IllegalStateException(
          "API does not support client ID & secret - you must provide a key");
    }
    if (urlSigner == null && !apiKey.startsWith("AIza")) {
      throw new IllegalStateException("Invalid API key.");
    }
  }

  /** The Builder for {@code GeoApiContext}. */
  public static class Builder {

    private RequestHandler.Builder builder;

    private String apiKey;
    private String baseUrlOverride;
    private String channel;
    private String clientId;
    private long errorTimeout = DEFAULT_BACKOFF_TIMEOUT_MILLIS;
    private ExceptionsAllowedToRetry exceptionsAllowedToRetry = new ExceptionsAllowedToRetry();
    private Integer maxRetries;
    private UrlSigner urlSigner;
    private RequestMetricsReporter requestMetricsReporter = new NoOpRequestMetricsReporter();
    private String[] experienceIdHeaderValue;

    /** Builder pattern for the enclosing {@code GeoApiContext}. */
    public Builder() {
      requestHandlerBuilder(new OkHttpRequestHandler.Builder());
    }

    public Builder(RequestHandler.Builder builder) {
      requestHandlerBuilder(builder);
    }

    /**
     * Changes the RequestHandler.Builder strategy to change between the {@code
     * OkHttpRequestHandler} and the {@code GaeRequestHandler}.
     *
     * @param builder The {@code RequestHandler.Builder} to use for {@link #build()}
     * @return Returns this builder for call chaining.
     * @see OkHttpRequestHandler
     * @see GaeRequestHandler
     */
    public Builder requestHandlerBuilder(RequestHandler.Builder builder) {
      this.builder = builder;
      this.exceptionsAllowedToRetry.add(OverQueryLimitException.class);
      return this;
    }

    /**
     * Overrides the base URL of the API endpoint. Useful for testing or certain international usage
     * scenarios.
     *
     * @param baseUrl The URL to use, without a trailing slash, e.g. https://maps.googleapis.com
     * @return Returns this builder for call chaining.
     */
    Builder baseUrlOverride(String baseUrl) {
      baseUrlOverride = baseUrl;
      return this;
    }

    /**
     * Older name for {@link #baseUrlOverride(String)}. This was used back when testing was the only
     * use case foreseen for this.
     *
     * @deprecated Use baseUrlOverride(String) instead.
     * @param baseUrl The URL to use, without a trailing slash, e.g. https://maps.googleapis.com
     * @return Returns this builder for call chaining.
     */
    @Deprecated
    Builder baseUrlForTesting(String baseUrl) {
      return baseUrlOverride(baseUrl);
    }

    /**
     * Sets the API Key to use for authorizing requests.
     *
     * @param apiKey The API Key to use.
     * @return Returns this builder for call chaining.
     */
    public Builder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    /**
     * Sets the ClientID/Secret pair to use for authorizing requests. Most users should use {@link
     * #apiKey(String)} instead.
     *
     * @param clientId The Client ID to use.
     * @param cryptographicSecret The Secret to use.
     * @return Returns this builder for call chaining.
     */
    public Builder enterpriseCredentials(String clientId, String cryptographicSecret) {
      this.clientId = clientId;
      try {
        this.urlSigner = new UrlSigner(cryptographicSecret);
      } catch (NoSuchAlgorithmException | InvalidKeyException e) {
        throw new IllegalStateException(e);
      }
      return this;
    }

    /**
     * Sets the default channel for requests (can be overridden by requests). Only useful for Google
     * Maps for Work clients.
     *
     * @param channel The channel to use for analytics
     * @return Returns this builder for call chaining.
     */
    public Builder channel(String channel) {
      this.channel = channel;
      return this;
    }

    /**
     * Sets the default connect timeout for new connections. A value of 0 means no timeout.
     *
     * @see java.net.URLConnection#setConnectTimeout(int)
     * @param timeout The connect timeout period in {@code unit}s.
     * @param unit The connect timeout time unit.
     * @return Returns this builder for call chaining.
     */
    public Builder connectTimeout(long timeout, TimeUnit unit) {
      builder.connectTimeout(timeout, unit);
      return this;
    }

    /**
     * Sets the default read timeout for new connections. A value of 0 means no timeout.
     *
     * @see java.net.URLConnection#setReadTimeout(int)
     * @param timeout The read timeout period in {@code unit}s.
     * @param unit The read timeout time unit.
     * @return Returns this builder for call chaining.
     */
    public Builder readTimeout(long timeout, TimeUnit unit) {
      builder.readTimeout(timeout, unit);
      return this;
    }

    /**
     * Sets the default write timeout for new connections. A value of 0 means no timeout.
     *
     * @param timeout The write timeout period in {@code unit}s.
     * @param unit The write timeout time unit.
     * @return Returns this builder for call chaining.
     */
    public Builder writeTimeout(long timeout, TimeUnit unit) {
      builder.writeTimeout(timeout, unit);
      return this;
    }

    /**
     * Sets the cumulative time limit for which retry-able errors will be retried. Defaults to 60
     * seconds. Set to zero to retry requests forever.
     *
     * <p>This operates separately from the count-based {@link #maxRetries(Integer)}.
     *
     * @param timeout The retry timeout period in {@code unit}s.
     * @param unit The retry timeout time unit.
     * @return Returns this builder for call chaining.
     */
    public Builder retryTimeout(long timeout, TimeUnit unit) {
      this.errorTimeout = unit.toMillis(timeout);
      return this;
    }

    /**
     * Sets the maximum number of times each retry-able errors will be retried. Set this to null to
     * not have a max number. Set this to zero to disable retries.
     *
     * <p>This operates separately from the time-based {@link #retryTimeout(long, TimeUnit)}.
     *
     * @param maxRetries The maximum number of times to retry.
     * @return Returns this builder for call chaining.
     */
    public Builder maxRetries(Integer maxRetries) {
      this.maxRetries = maxRetries;
      return this;
    }

    /**
     * Disables retries completely, by setting max retries to 0 and retry timeout to 0.
     *
     * @return Returns this builder for call chaining.
     */
    public Builder disableRetries() {
      maxRetries(0);
      retryTimeout(0, TimeUnit.MILLISECONDS);
      return this;
    }

    /**
     * Sets the maximum number of queries that will be executed during a 1 second interval. The
     * default is 50. A minimum interval between requests will also be enforced, set to 1/(2 *
     * {@code maxQps}).
     *
     * @param maxQps The maximum queries per second.
     * @return Returns this builder for call chaining.
     */
    public Builder queryRateLimit(int maxQps) {
      builder.queriesPerSecond(maxQps);
      return this;
    }

    /**
     * Allows specific API exceptions to be retried or not retried.
     *
     * @param exception The {@code ApiException} to allow or deny being re-tried.
     * @param allowedToRetry Whether to allow or deny re-trying {@code exception}.
     * @return Returns this builder for call chaining.
     */
    public Builder setIfExceptionIsAllowedToRetry(
        Class<? extends ApiException> exception, boolean allowedToRetry) {
      if (allowedToRetry) {
        exceptionsAllowedToRetry.add(exception);
      } else {
        exceptionsAllowedToRetry.remove(exception);
      }
      return this;
    }

    /**
     * Sets the proxy for new connections.
     *
     * @param proxy The proxy to be used by the underlying HTTP client.
     * @return Returns this builder for call chaining.
     */
    public Builder proxy(Proxy proxy) {
      builder.proxy(proxy == null ? Proxy.NO_PROXY : proxy);
      return this;
    }

    /**
     * set authentication for proxy
     *
     * @param proxyUserName username for proxy authentication
     * @param proxyUserPassword username for proxy authentication
     * @return Returns this builder for call chaining.
     */
    public Builder proxyAuthentication(String proxyUserName, String proxyUserPassword) {
      builder.proxyAuthentication(proxyUserName, proxyUserPassword);
      return this;
    }

    /**
     * Sets the value for the HTTP header field name {@link HttpHeaders#X_GOOG_MAPS_EXPERIENCE_ID}
     * HTTP header value for the field name on subsequent API calls.
     *
     * @param experienceId The experience ID
     * @return Returns this builder for call chaining.
     */
    public Builder experienceId(String... experienceId) {
      this.experienceIdHeaderValue = experienceId;
      return this;
    }

    public Builder requestMetricsReporter(RequestMetricsReporter requestMetricsReporter) {
      this.requestMetricsReporter = requestMetricsReporter;
      return this;
    }

    /**
     * Converts this builder into a {@code GeoApiContext}.
     *
     * @return Returns the built {@code GeoApiContext}.
     */
    public GeoApiContext build() {
      return new GeoApiContext(
          builder.build(),
          apiKey,
          baseUrlOverride,
          channel,
          clientId,
          errorTimeout,
          exceptionsAllowedToRetry,
          maxRetries,
          urlSigner,
          requestMetricsReporter,
          experienceIdHeaderValue);
    }
  }
}
