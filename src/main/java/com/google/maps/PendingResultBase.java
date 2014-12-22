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

import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.StringJoin.UrlValue;
import com.squareup.okhttp.MediaType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation for {@code PendingResult}.
 *
 * <p>{@code T} is the class of the result, {@code A} is the actual base class of this abstract
 * class, and R is the type of the request.
 */
public abstract class PendingResultBase<T, A extends PendingResultBase<T, A, R>,
      R extends ApiResponse<T>>
    implements PendingResult<T> {

  private final ApiContext context;
  private MethodType methodType = MethodType.GET;
  private MediaType contentType;
  private HashMap<String, String> params = new HashMap<String, String>();
  private Object body = new Object();
  private PendingResult<T> delegate;
  private Class<R> responseClass;
  private String base;

  protected PendingResultBase(ApiContext context, Class<R> responseClass, String base) {
    this.context = context;
    this.responseClass = responseClass;
    this.base = base;
  }

  @Override
  public final void setMethod(MethodType methodType, MediaType contentType){
    this.methodType = methodType;
    if (methodType != MethodType.POST && contentType == null){
      throw new IllegalArgumentException("If not GET, must set content type.");
    }
    this.contentType = contentType;
  }

  @Override
  public final void setCallback(Callback<T> callback) {
    makeRequest().setCallback(callback);
  }

  @Override
  public final T await() throws Exception {
    PendingResult<T> request = makeRequest();
    T result = request.await();
    return result;
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
    delegate = methodType == MethodType.GET 
            ? context.get(responseClass, base, params) 
            : context.post(responseClass, base, params, contentType, body);
    return (PendingResult<T>) delegate;
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

  protected A body(Object body) {
    this.body = body;

    @SuppressWarnings("unchecked") // safe by specification - A is the actual class of this instance
    A result = (A) this;
    return result;
  }

  protected Object body() {
    return this.body;
  }
  
  /**
   * The language in which to return results. Note that we often update supported languages so
   * this list may not be exhaustive.
   *
   * @see <a href="https://developers.google.com/maps/faq#languagesupport">List of supported
   * domain languages</a>
   * @param language  The language code, e.g. "en-AU" or "es"
   */
  public final A language(String language) {
    return param("language", language);
  }
}
