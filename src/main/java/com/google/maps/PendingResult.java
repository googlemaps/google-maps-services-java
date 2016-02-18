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

/**
 * Represents a pending result from an API call.
 *
 * @param <T> the type of the result object.
 */
public interface PendingResult<T> {

  /**
   * Performs the request asynchronously, calling onResult or onFailure after the request has been
   * completed.
   */
  void setCallback(Callback<T> callback);

  /**
   * Performs the request synchronously.
   *
   * @return The result.
   */
  T await() throws Exception;

  /**
   * Performs the request synchronously, ignoring exceptions while performing the request and errors
   * returned by the server.
   *
   * @return The result, or null if there was any error or exception ignored.
   */
  T awaitIgnoreError();

  /**
   * Attempt to cancel the request.
   */
  void cancel();

  /**
   * The callback interface the API client code needs to implement to handle API results.
   */
  interface Callback<T> {

    /**
     * Called when the request was successfully completed.
     */
    void onResult(T result);

    /**
     * Called when there was an error performing the request.
     */
    void onFailure(Throwable e);
  }
}
