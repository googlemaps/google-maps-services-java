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

import com.google.maps.internal.StringJoin.UrlValue;

  /**
   * Location types for a reverse geocoding request. Please see
   * <a href="https://developers.google.com/maps/documentation/geocoding/#ReverseGeocoding">for
   * more detail</a>.
   */
  public enum LocationType implements UrlValue {
    /**
     * {@code ROOFTOP} restricts the results to addresses for which we have location information
     * accurate down to street address precision.
     */
    ROOFTOP("ROOFTOP"),

    /**
     * {@code RANGE_INTERPOLATED} restricts the results to those that reflect an approximation
     * (usually on a road) interpolated between two precise points (such as intersections). An
     * interpolated range generally indicates that rooftop geocodes are unavailable for a street
     * address.
     */
    RANGE_INTERPOLATED("RANGE_INTERPOLATED"),

    /**
     * {@code GEOMETRIC_CENTER} restricts the results to geometric centers of a location such as a
     * polyline (for example, a street) or polygon (region).
     */
    GEOMETRIC_CENTER("GEOMETRIC_CENTER"),

    /**
     * {@code APPROXIMATE} restricts the results to those that are characterized as approximate.
     */
    APPROXIMATE("APPROXIMATE");

    private String locationType;

    LocationType(String locationType) {
      this.locationType = locationType;
    }

    public LocationType lookup(String locationType) {
      if (locationType.equalsIgnoreCase(ROOFTOP.toString())) {
        return ROOFTOP;
      } else if (locationType.equalsIgnoreCase(RANGE_INTERPOLATED.toString())) {
        return RANGE_INTERPOLATED;
      } else if (locationType.equalsIgnoreCase(GEOMETRIC_CENTER.toString())) {
        return GEOMETRIC_CENTER;
      } else if (locationType.equalsIgnoreCase(APPROXIMATE.toString())) {
        return APPROXIMATE;
      } else {
        throw new RuntimeException("Unknown location type '" + locationType + "'");
      }
    }
  }
