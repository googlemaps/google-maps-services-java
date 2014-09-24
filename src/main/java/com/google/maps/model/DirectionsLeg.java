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

import org.joda.time.DateTime;

/**
 * A component of a Directions API result.
 *
 * See <a href="https://developers.google.com/maps/documentation/directions/#Legs">the Legs
 * documentation</a> for more detail.
 */
public class DirectionsLeg {

  /**
   * {@code steps[]} contains an array of steps denoting information about each separate step of the
   * leg of the journey.
   */
  public DirectionsStep[] steps;

  /**
   * {@code distance} indicates the total distance covered by this leg.
   */
  public Distance distance;

  /**
   * {@code duration} indicates the total duration of this leg
   */
  public Duration duration;

  /**
   * {@code durationInTraffic} indicates the total duration of this leg, taking into account current
   * traffic conditions. The duration in traffic will only be returned if all of the following are
   * true:
   * <ol>
   *   <li>The directions request includes a departureTime parameter set to a value within a few
   *       minutes of the current time.</li>
   *   <li>The request includes a valid Maps for Work client and signature parameter.</li>
   *   <li>Traffic conditions are available for the requested route.</li>
   *   <li>The directions request does not include stopover waypoints.</li>
   * </ol>
   */
  public Duration durationInTraffic;

  /**
   * {@code arrivalTime} contains the estimated time of arrival for this leg. This property is only
   * returned for transit directions.
   */
  public DateTime arrivalTime;

  /**
   * {@code departureTime} contains the estimated time of departure for this leg. The departureTime
   * is only available for transit directions.
   */
  public DateTime departureTime;


  /**
   * {@code startLocation} contains the latitude/longitude coordinates of the origin of this leg.
   * Because the Directions API calculates directions between locations by using the nearest
   * transportation option (usually a road) at the start and end points, startLocation may be
   * different than the provided origin of this leg if, for example, a road is not near the origin.
   */
  public LatLng startLocation;

  /**
   * {@code endLocation} contains the latitude/longitude coordinates of the given destination of
   * this leg. Because the Directions API calculates directions between locations by using the
   * nearest transportation option (usually a road) at the start and end points, endLocation may be
   * different than the provided destination of this leg if, for example, a road is not near the
   * destination.
   */
  public LatLng endLocation;

  /**
   * {@code startAddress} contains the human-readable address (typically a street address)
   * reflecting the start location of this leg.
   */
  public String startAddress;

  /**
   * {@code endAddress} contains the human-readable address (typically a street address) reflecting
   * the end location of this leg.
   */
  public String endAddress;

}
