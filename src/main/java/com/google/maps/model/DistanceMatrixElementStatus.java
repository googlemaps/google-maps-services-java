package com.google.maps.model;

/**
 * The status result for a single {@link com.google.maps.model.DistanceMatrixElement}.
 *
 * @see <a href="https://developers.google
 * .com/maps/documentation/distancematrix/#StatusCodes">Documentation on status codes</a>
 */
public class DistanceMatrixElementStatus {
  private DistanceMatrixElementStatus() {
  }

  /**
   * {@code OK} indicates the response contains a valid result.
   */
  public static final String OK = "OK";

  /**
   * {@code NOT_FOUND} indicates that the origin and/or destination of this pairing could not be
   * geocoded.
   */
  public static final String NOT_FOUND = "NOT_FOUND";

  /**
   * {@code ZERO_RESULTS} indicates no route could be found between the origin and destination.
   */
  public static final String ZERO_RESULTS = "ZERO_RESULTS";
}
