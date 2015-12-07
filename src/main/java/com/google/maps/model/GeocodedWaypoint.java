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

/**
 * Geocoded Waypoint represents a point in a Directions API response, either the origin, one of the
 * requested waypoints, or the destination. Please see
 * <a href="https://developers.google.com/maps/documentation/directions/intro#GeocodedWaypoints">
 *   Geocoded Waypoints</a> for more detail.
 */
public class GeocodedWaypoint {
  /**
   * {@code geocoderStatus} indicates the status code resulting from the geocoding operation.
   */
  public GeocodedWaypointStatus geocoderStatus;

  /**
   * {@code partialMatch} indicates that the geocoder did not return an exact match for the original
   * request, though it was able to match part of the requested address.
   */
  public boolean partialMatch;

  /**
   * {@code placeId} is a unique identifier that can be used with other Google APIs.
   */
  public String placeId;

  /**
   * {@code types} indicates the address type of the geocoding result used for calculating
   * directions.
   */
  public AddressType types[];
}
