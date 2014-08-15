package com.google.maps.model;

import com.google.maps.internal.StringJoin.UrlValue;

import java.util.Locale;

/**
 * A place on Earth, represented by a Latitude/Longitude pair.
 */
public class LatLng implements UrlValue {

  /**
   * The latitude of this location.
   */
  public double lat;

  /**
   * The longitude of this location.
   */
  public double lng;

  /**
   * Construct a location with a latitude longitude pair.
   */
  public LatLng(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  @Override
  public String toString() {
    // Enforce Locale to English for double to string conversion
    return String.format(Locale.ENGLISH, "%f,%f", lat, lng);
  }
}
