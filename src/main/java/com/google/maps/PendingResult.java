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

import com.google.maps.errors.ApiException;
import java.io.IOException;

/**
 * A pending result from an API call.
 *
 * @param <T> the type of the result object.
 */
public interface PendingResult<T> {

  /**
   * Performs the request asynchronously, calling {@link
   * com.google.maps.PendingResult.Callback#onResult onResult} or {@link
   * com.google.maps.PendingResult.Callback#onFailure onFailure} after the request has been
   * completed.
   *
   * @param callback The callback to call on completion.
   */
  void setCallback(Callback<T> callback);

  /**
   * Performs the request synchronously.
   *
   * @return The result.
   * @throws ApiException Thrown if the API Returned result is an error.
   * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied,
   *     and the thread is interrupted.
   * @throws IOException Thrown when an I/O exception of some sort has occurred.
   */
  T await() throws ApiException, InterruptedException, IOException;

  /**
   * Performs the request synchronously, ignoring exceptions while performing the request and errors
   * returned by the server.
   *
   * @return The result, or null if there was any error or exception ignored.
   */
  T awaitIgnoreError();

  /** Attempts to cancel the request. */
  void cancel();

  /**
   * The callback interface the API client code needs to implement to handle API results.
   *
   * @param <T> The type of the result object.
   */
  interface Callback<T> {

    /**
     * Called when the request was successfully completed.
     *
     * @param result The result of the call.
     */
    void onResult(T result);

    /**
     * Called when there was an error performing the request.
     *
     * @param e The exception describing the failure.
     */
    void onFailure(Throwable e);
  }
}
