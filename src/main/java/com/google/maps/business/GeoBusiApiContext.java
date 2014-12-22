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
package com.google.maps.business;

import com.google.maps.ApiContext;
import com.google.maps.internal.UrlSigner;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Gavin.Lin
 */
public class GeoBusiApiContext extends ApiContext {

  private static final String DEFAULT_HOST = "https://www.googleapis.com";

  public GeoBusiApiContext() {
    super(DEFAULT_HOST);
  }

  @Override
  protected GeoBusiApiContext setBaseUrl(String baseUrl) {
    this.host = baseUrl;
    return this;
  }

  @Override
  public GeoBusiApiContext setApiKey(String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  @Override
  public GeoBusiApiContext setEnterpriseCredentials(String clientId, String cryptographicSecret) {
    this.clientId = clientId;
    this.urlSigner = new UrlSigner(cryptographicSecret);
    return this;
  }

  @Override
  public GeoBusiApiContext setConnectTimeout(long timeout, TimeUnit unit) {
    client.setConnectTimeout(timeout, unit);
    return this;
  }

  @Override
  public GeoBusiApiContext setReadTimeout(long timeout, TimeUnit unit) {
    client.setReadTimeout(timeout, unit);
    return this;
  }

  @Override
  public GeoBusiApiContext setWriteTimeout(long timeout, TimeUnit unit) {
    client.setWriteTimeout(timeout, unit);
    return this;
  }

  @Override
  public GeoBusiApiContext setRetryTimeout(long timeout, TimeUnit unit) {
    this.errorTimeout = unit.toMillis(timeout);
    return this;
  }

  @Override
  public GeoBusiApiContext setQueryRateLimit(int maxQps) {
    rateLimitExecutorService.setQueriesPerSecond(maxQps);
    return this;
  }

  @Override
  public GeoBusiApiContext setQueryRateLimit(int maxQps, int minimumInterval) {
    rateLimitExecutorService.setQueriesPerSecond(maxQps, minimumInterval);
    return this;
  }
}
