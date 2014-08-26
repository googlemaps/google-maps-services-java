/*
 * Copyright 2014 Google Inc. All rights reserved.
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

/**
 * The Geometry of a Geocoding Result.
 */
public class Geometry {
  /**
   * {@code bounds} (optionally returned) stores the bounding box which can fully contain the
   * returned result. Note that these bounds may not match the recommended viewport. (For example,
   * San Francisco includes the Farallon islands, which are technically part of the city, but
   * probably should not be returned in the viewport.)
   */
  public Bounds bounds;

  /**
   * {@code location} contains the geocoded {@code latitude,longitude} value. For normal address
   * lookups, this field is typically the most important.
   */
  public LatLng location;

  /**
   * The level of certainty of this geocoding result.
   */
  public LocationType locationType;

  /**
   * {@code viewport} contains the recommended viewport for displaying the returned result.
   * Generally the viewport is used to frame a result when displaying it to a user.
   */
  public Bounds viewport;
}
