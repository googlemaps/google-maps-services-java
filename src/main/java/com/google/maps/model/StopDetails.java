package com.google.maps.model;

/**
 * The stop/station.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/directions/#TransitDetails">
 * Transit details</a> for more detail.
 */
public class StopDetails {

  /**
   * The name of the transit station/stop. eg. "Union Square".
   */
  public String name;

  /**
   * The location of the transit station/stop, represented as a lat and lng field.
   */
  public LatLng location;
}
