/*
 * Copyright 2016 Google Inc. All rights reserved.
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

/**
 * A Geolocation API result.
 *
 * <p>A successful geolocation request will return a result defining a location and radius.
 *
 * <p>Please see <a
 * href="https://developers.google.com/maps/documentation/geolocation/intro#responses">Geolocation
 * responses</a> for more detail.
 */
public class GeolocationResult implements Serializable {

  private static final long serialVersionUID = 1L;
  /** The user’s estimated latitude and longitude. */
  public LatLng location;
  /**
   * The accuracy of the estimated location, in meters. This represents the radius of a circle
   * around the returned {@code location}.
   */
  public double accuracy;

  @Override
  public String toString() {
    return String.format("%s, accuracy=%s m", location, accuracy);
  }
}
