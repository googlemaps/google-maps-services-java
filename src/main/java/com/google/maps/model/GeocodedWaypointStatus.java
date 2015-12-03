package com.google.maps.model;

/**
 * The status result for a single {@link com.google.maps.model.GeocodedWaypoint}.
 *
 * @see <a href="https://developers.google.com/maps/documentation/directions/intro#StatusCodes">
 *   Documentation on status codes</a>
 */
public enum GeocodedWaypointStatus {
  /**
   * {@code OK} indicates the response contains a valid result.
   */
  OK,

  /**
   * {@code ZERO_RESULTS} indicates no route could be found between the origin and destination.
   */
  ZERO_RESULTS

}
