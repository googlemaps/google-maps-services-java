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

package com.google.maps.errors;

/**
 * ApiException and it's descendants represent an error returned by the remote API. API errors
 * are determined by the {@code status} field returned in any of the Geo API responses.
 */
public class ApiException extends Exception {
  protected ApiException(String message) {
    super(message);
  }

  /**
   * Construct the appropriate ApiException from the response. If the response was successful,
   * this method will return null.
   *
   * @param status  The status field returned from the API
   * @param errorMessage The error message returned from the API
   * @return The appropriate ApiException based on the status or null if no error occurred.
   */
  public static ApiException from(String status, String errorMessage) {
    switch (status) {
      case "OK":
        return null;
      case "INVALID_REQUEST":
        return new InvalidRequestException(errorMessage);
      case "MAX_ELEMENTS_EXCEEDED":
        return new MaxElementsExceededException(errorMessage);
      case "NOT_FOUND":
        return new NotFoundException(errorMessage);
      case "OVER_QUERY_LIMIT":
        return new OverQueryLimitException(errorMessage);
      case "REQUEST_DENIED":
        return new RequestDeniedException(errorMessage);
      case "UNKNOWN_ERROR":
        return new UnknownErrorException(errorMessage);
      case "ZERO_RESULTS":
        return new ZeroResultsException(errorMessage);
    }

    // We've hit an unknown error. This is not a state we should hit,
    // but we don't want to crash a user's application if we introduce a new error.
    return new UnknownErrorException("An unexpected error occurred. "
       + "Status: " + status + ", "
       + "Message: " + errorMessage);
  }
}
