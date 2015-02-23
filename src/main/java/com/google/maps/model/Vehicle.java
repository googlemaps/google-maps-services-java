package com.google.maps.model;

/**
 * The vehicle used on a line.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/directions/#TransitDetails">
 * Transit details</a> for more detail.
 */
public class Vehicle {

  /**
   * {@code name} contains the name of the vehicle on this line. eg. "Subway."
   */
  public String name;

  /**
   * {@code vehicle} contains the type of vehicle that runs on this line. See the
   * {@link com.google.maps.model.VehicleType VehicleType} documentation for a complete list of
   * supported values.
   */
  public VehicleType vehicle;

  /**
   * {@code icon} contains the URL for an icon associated with this vehicle type.
   */
  public String icon;
}