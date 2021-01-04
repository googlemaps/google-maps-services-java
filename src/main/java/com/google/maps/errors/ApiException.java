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
 * ApiException and its descendants represent an error returned by the remote API. API errors are
 * determined by the {@code status} field returned in any of the Geo API responses.
 */
public class ApiException extends Exception {
  private static final long serialVersionUID = -6550606366694345191L;

  protected ApiException(String message) {
    super(message);
  }

  /**
   * Construct the appropriate ApiException from the response. If the response was successful, this
   * method will return null.
   *
   * @param status The status field returned from the API
   * @param errorMessage The error message returned from the API
   * @return The appropriate ApiException based on the status or null if no error occurred.
   */
  public static ApiException from(String status, String errorMessage) {
    // Classic Geo API error formats
    if ("OK".equals(status)) {
      return null;
    } else if ("INVALID_REQUEST".equals(status)) {
      return new InvalidRequestException(errorMessage);
    } else if ("MAX_ELEMENTS_EXCEEDED".equals(status)) {
      return new MaxElementsExceededException(errorMessage);
    } else if ("MAX_ROUTE_LENGTH_EXCEEDED".equals(status)) {
      return new MaxRouteLengthExceededException(errorMessage);
    } else if ("MAX_WAYPOINTS_EXCEEDED".equals(status)) {
      return new MaxWaypointsExceededException(errorMessage);
    } else if ("NOT_FOUND".equals(status)) {
      return new NotFoundException(errorMessage);
    } else if ("OVER_QUERY_LIMIT".equals(status)) {
      if ("You have exceeded your daily request quota for this API. If you did not set a custom daily request quota, verify your project has an active billing account: http://g.co/dev/maps-no-account"
          .equalsIgnoreCase(errorMessage)) {
        return new OverDailyLimitException(errorMessage);
      }
      return new OverQueryLimitException(errorMessage);
    } else if ("REQUEST_DENIED".equals(status)) {
      return new RequestDeniedException(errorMessage);
    } else if ("UNKNOWN_ERROR".equals(status)) {
      return new UnknownErrorException(errorMessage);
    } else if ("ZERO_RESULTS".equals(status)) {
      return new ZeroResultsException(errorMessage);
    }

    // New-style Geo API error formats
    if ("ACCESS_NOT_CONFIGURED".equals(status)) {
      return new AccessNotConfiguredException(errorMessage);
    } else if ("INVALID_ARGUMENT".equals(status)) {
      return new InvalidRequestException(errorMessage);
    } else if ("RESOURCE_EXHAUSTED".equals(status)) {
      return new OverQueryLimitException(errorMessage);
    } else if ("PERMISSION_DENIED".equals(status)) {
      return new RequestDeniedException(errorMessage);
    }

    // Geolocation Errors
    if ("keyInvalid".equals(status)) {
      return new AccessNotConfiguredException(errorMessage);
    } else if ("dailyLimitExceeded".equals(status)) {
      return new OverDailyLimitException(errorMessage);
    } else if ("userRateLimitExceeded".equals(status)) {
      return new OverQueryLimitException(errorMessage);
    } else if ("notFound".equals(status)) {
      return new NotFoundException(errorMessage);
    } else if ("parseError".equals(status)) {
      return new InvalidRequestException(errorMessage);
    } else if ("invalid".equals(status)) {
      return new InvalidRequestException(errorMessage);
    }

    // We've hit an unknown error. This is not a state we should hit,
    // but we don't want to crash a user's application if we introduce a new error.
    return new UnknownErrorException(
        "An unexpected error occurred. Status: " + status + ", Message: " + errorMessage);
  }
}
