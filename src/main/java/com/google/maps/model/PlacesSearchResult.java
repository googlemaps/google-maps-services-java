/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.google.maps.model;

import java.net.URL;

/**
 * PlaceSearchResult represents a single result in the search results return from the Google Places
 * API Web Service.
 *
 * <p>Please see <a href="https://developers.google.com/places/web-service/search#PlaceSearchResults">Place
 * Search Results</a> for more detail.</p>
 */
public class PlacesSearchResult {

  /**
   * formattedAddress is a string containing the human-readable address of this place.
   */
  public String formattedAddress;

  /**
   * geometry contains geometry information about the result, generally including the location
   * (geocode) of the place and (optionally) the viewport identifying its general area of coverage.
   */
  public Geometry geometry;

  /**
   * name contains the human-readable name for the returned result. For establishment results, this
   * is usually the business name.
   */
  public String name;

  /**
   * icon contains the URL of a recommended icon which may be displayed to the user when indicating
   * this result.
   */
  public URL icon;

  /**
   * placeId is a textual identifier that uniquely identifies a place.
   */
  public String placeId;

  /**
   * scope indicates the scope of the placeId.
   */
  public PlaceIdScope scope;

  /**
   * rating contains the place's rating, from 1.0 to 5.0, based on aggregated user reviews.
   */
  public float rating;

  /**
   * types contains an array of feature types describing the given result.
   */
  public String types[];

  /**
   * openingHours may contain whether the place is open now or not.
   */
  public OpeningHours openingHours;

  /**
   * photos is an array of photo objects, each containing a reference to an image.
   */
  public Photo photos[];

  /**
   * vicinity contains a feature name of a nearby location.
   */
  public String vicinity;

  /**
   * permanentlyClosed is a boolean flag indicating whether the place has permanently shut down.
   */
  public boolean permanentlyClosed;
}
