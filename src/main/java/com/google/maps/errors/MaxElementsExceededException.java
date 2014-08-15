package com.google.maps.errors;

/**
 * Indicates that the product of origins and destinations exceeds the per-query limit.
 *
 * @see <a href="https://developers.google.com/maps/documentation/distancematrix/#Limits">Limits</a>
 */
public class MaxElementsExceededException extends ApiException {

  public MaxElementsExceededException(String errorMessage) {
    super(errorMessage);
  }
}
