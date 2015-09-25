package com.google.maps.model;

import java.net.URL;

public class PlacesSearchResult {

  /** formattedAddress is a string containing the human-readable address of this place. */
  public String formattedAddress;

  /**
   * geometry contains geometry information about the result, generally including the location (geocode) of the place
   * and (optionally) the viewport identifying its general area of coverage.
   */
  public Geometry geometry;

  /**
   * name contains the human-readable name for the returned result. For establishment results, this is usually the
   * business name.
   */
  public String name;

  /** icon contains the URL of a recommended icon which may be displayed to the user when indicating this result. */
  public URL icon;

  /** place_id is a textual identifier that uniquely identifies a place. */
  public String placeId;

  /** scope: Indicates the scope of the placeId. */
  public PlaceIdScope scope;

  /** rating contains the place's rating, from 1.0 to 5.0, based on aggregated user reviews. */
  public float rating;

  /** types contains an array of feature types describing the given result. */
  public String types[];

  /** openingHours may contain whether the place is open now or not. */
  public OpeningHours openingHours;

  /** photos is an array of photo objects, each containing a reference to an image. */
  public Photo photos[];

  /** vicinity contains a feature name of a nearby location. */
  public String vicinity;

  /**
   * This field is deprecated. Please do not use this field.
   *
   * @deprecated Please see the <a href="https://developers.google.com/places/web-service/search#deprecation">
   *   deprecation notice</a>.
   */
  public String id;

  /**
   * This field is deprecated. Please do not use this field.
   *
   * @deprecated Please see the <a href="https://developers.google.com/places/web-service/search#deprecation">
   *   deprecation notice</a>.
   */
  public String reference;

}
