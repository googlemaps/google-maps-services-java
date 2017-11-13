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

import java.io.Serializable;

/**
 * DirectionsResult represents a result from the Google Directions API Web Service.
 *
 * <p>Please see <a href="https://developers.google.com/maps/documentation/directions/intro">
 * Directions API</a> for more detail.
 */
public class DirectionsResult implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Details about the geocoding of origin, destination, and waypoints. See <a
   * href="https://developers.google.com/maps/documentation/directions/intro#GeocodedWaypoints">
   * Geocoded Waypoints</a> for more detail.
   */
  public GeocodedWaypoint geocodedWaypoints[];

  /**
   * Routes from the origin to the destination. See <a
   * href="https://developers.google.com/maps/documentation/directions/intro#Routes">Routes</a> for
   * more detail.
   */
  public DirectionsRoute routes[];
}
