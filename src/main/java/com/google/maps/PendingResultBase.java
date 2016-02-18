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

import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.StringJoin.UrlValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation for {@code PendingResult}.
 *
 * <p>{@code T} is the class of the result, {@code A} is the actual base class of this abstract
 * class, and R is the type of the request.
 */
abstract class PendingResultBase<T, A extends PendingResultBase<T, A, R>,
    R extends ApiResponse<T>>
    implements PendingResult<T> {

  private final GeoApiContext context;
  private final ApiConfig config;
  private HashMap<String, String> params = new HashMap<String, String>();
  private PendingResult<T> delegate;
  private Class<? extends R> responseClass;

  protected PendingResultBase(GeoApiContext context, ApiConfig config, Class<? extends R> clazz) {
    this.context = context;
    this.config = config;
    this.responseClass = clazz;
  }

  @Override
  public final void setCallback(Callback<T> callback) {
    makeRequest().setCallback(callback);
  }

  @Override
  public final T await() throws Exception {
    PendingResult<T> request = makeRequest();
    return request.await();
  }

  @Override
  public final T awaitIgnoreError() {
    return makeRequest().awaitIgnoreError();
  }

  @Override
  public final void cancel() {
    if (delegate == null) {
      return;
    }
    delegate.cancel();
  }

  private PendingResult<T> makeRequest() {
    if (delegate != null) {
      throw new IllegalStateException(
          "'await', 'awaitIgnoreError' or 'setCallback' was already called.");
    }
    validateRequest();
    delegate = context.get(config, responseClass, params);
    return delegate;
  }

  protected abstract void validateRequest();

  protected A param(String key, String val) {
    params.put(key, val);

    @SuppressWarnings("unchecked") // safe by specification - A is the actual class of this instance
        A result = (A) this;
    return result;
  }

  protected A param(String key, UrlValue val) {
    params.put(key, val.toString());

    @SuppressWarnings("unchecked") // safe by specification - A is the actual class of this instance
        A result = (A) this;
    return result;
  }

  protected Map<String, String> params() {
    return Collections.unmodifiableMap(params);
  }

  /**
   * The language in which to return results. Note that we often update supported languages so this
   * list may not be exhaustive.
   *
   * @param language The language code, e.g. "en-AU" or "es"
   * @see <a href="https://developers.google.com/maps/faq#languagesupport">List of supported domain
   * languages</a>
   */
  public final A language(String language) {
    return param("language", language);
  }

  /**
   * A channel to pass with the request.  channel is used by Google Maps API for Work users to be
   * able to track usage across different applications with the same clientID. See:
   * https://developers.google.com/maps/documentation/business/clientside/quota
   *
   * @param channel String to pass with the request for analytics
   */
  public A channel(String channel) {
    return param("channel", channel);
  }
}
