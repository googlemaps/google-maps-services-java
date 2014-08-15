package com.google.maps.errors;

/**
 * Indicates that no results were returned.
 *
 * <p>In some cases, this will be treated as a success
 * state and you will only see an empty array. For time zone data, it means that no time zone
 * information could be found for the specified position or time. Confirm that the request is for
 * a location on land, and not over water.
 */
public class ZeroResultsException extends ApiException {

  public ZeroResultsException(String errorMessage) {
    super(errorMessage);
  }
}
