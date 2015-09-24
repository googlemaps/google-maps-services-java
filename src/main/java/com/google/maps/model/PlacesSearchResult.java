package com.google.maps.model;

import java.net.URL;

public class PlacesSearchResult {

  public String formattedAddress;

  public Geometry geometry;

  public String name;

  public URL icon;

  public String placeId;

  public float rating;

  public String types[];


  /**
   * This field is deprecated. Please do not use this field.
   *
   * @deprecated Please see the deprecation notice.
   */
  public String id;

  /**
   * This field is deprecated. Please do not use this field.
   *
   * @deprecated Please see the deprecation notice.
   */
  public String reference;

}
