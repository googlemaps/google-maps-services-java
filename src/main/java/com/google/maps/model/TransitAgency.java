package com.google.maps.model;

/**
 * The operator of a line.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/directions/#TransitDetails">
 * Transit details</a> for more detail.
 */
public class TransitAgency {

  /**
   * {@code name} contains the name of the transit agency.
   */
  public String name;

  /**
   * {@code url} contains the URL for the transit agency.
   */
  public String url;

  /**
   * {@code phone} contains the phone number of the transit agency.
   */
  public String phone;
}