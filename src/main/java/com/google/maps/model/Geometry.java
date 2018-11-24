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

import java.io.Serializable;

/** The geometry of a Geocoding result. */
public class Geometry implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * The bounding box which can fully contain the returned result (optionally returned). Note that
   * these bounds may not match the recommended viewport. (For example, San Francisco includes the
   * Farallon islands, which are technically part of the city, but probably should not be returned
   * in the viewport.)
   */
  public Bounds bounds;

  /**
   * The geocoded latitude/longitude value. For normal address lookups, this field is typically the
   * most important.
   */
  public LatLng location;

  /** The level of certainty of this geocoding result. */
  public LocationType locationType;

  /**
   * The recommended viewport for displaying the returned result. Generally the viewport is used to
   * frame a result when displaying it to a user.
   */
  public Bounds viewport;

  @Override
  public String toString() {
    return String.format(
        "[Geometry: %s (%s) bounds=%s, viewport=%s]", location, locationType, bounds, viewport);
  }
}
