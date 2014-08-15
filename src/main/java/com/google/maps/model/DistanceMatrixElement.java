package com.google.maps.model;

/**
 * A single result corresponding to a origin/destination pair in a Distance Matrix response.
 *
 * <p>Be sure to check the status for each element, as a matrix response can have a mix of
 * successful and failed elements depending on the connectivity of the origin and destination.
 */
public class DistanceMatrixElement {

  /**
   * {@code status} indicates the status of the request for this origin/destination pair.
   *
   * Will be one of {@link com.google.maps.model.DistanceMatrixElementStatus}.
   */
  public String status;

  /**
   * {@code duration} indicates the total duration of this leg
   */
  public Duration duration;

  /**
   * {@code distance} indicates the total distance covered by this leg.
   */
  public Distance distance;
}
