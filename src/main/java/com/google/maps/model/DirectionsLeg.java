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
import java.time.ZonedDateTime;

/**
 * A component of a Directions API result.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/directions/intro#Legs">the Legs
 * documentation</a> for more detail.
 */
public class DirectionsLeg implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Contains an array of steps denoting information about each separate step of this leg of the
   * journey.
   */
  public DirectionsStep[] steps;

  /** The total distance covered by this leg. */
  public Distance distance;

  /** The total duration of this leg. */
  public Duration duration;

  /**
   * The total duration of this leg, taking into account current traffic conditions. The duration in
   * traffic will only be returned if all of the following are true:
   *
   * <ol>
   *   <li>The directions request includes a departureTime parameter set to a value within a few
   *       minutes of the current time.
   *   <li>The request includes a valid Maps for Work client and signature parameter.
   *   <li>Traffic conditions are available for the requested route.
   *   <li>The directions request does not include stopover waypoints.
   * </ol>
   */
  public Duration durationInTraffic;

  /**
   * The estimated time of arrival for this leg. This property is only returned for transit
   * directions.
   */
  public ZonedDateTime arrivalTime;

  /**
   * The estimated time of departure for this leg. The departureTime is only available for transit
   * directions.
   */
  public ZonedDateTime departureTime;

  /**
   * The latitude/longitude coordinates of the origin of this leg. Because the Directions API
   * calculates directions between locations by using the nearest transportation option (usually a
   * road) at the start and end points, startLocation may be different from the provided origin of
   * this leg if, for example, a road is not near the origin.
   */
  public LatLng startLocation;

  /**
   * The latitude/longitude coordinates of the given destination of this leg. Because the Directions
   * API calculates directions between locations by using the nearest transportation option (usually
   * a road) at the start and end points, endLocation may be different than the provided destination
   * of this leg if, for example, a road is not near the destination.
   */
  public LatLng endLocation;

  /**
   * The human-readable address (typically a street address) reflecting the start location of this
   * leg.
   */
  public String startAddress;

  /**
   * The human-readable address (typically a street address) reflecting the end location of this
   * leg.
   */
  public String endAddress;

  @Override
  public String toString() {
    StringBuilder sb =
        new StringBuilder(
            String.format(
                "[DirectionsLeg: \"%s\" -> \"%s\" (%s -> %s)",
                startAddress, endAddress, startLocation, endLocation));
    if (departureTime != null) {
      sb.append(", departureTime=").append(departureTime);
    }
    if (arrivalTime != null) {
      sb.append(", arrivalTime=").append(arrivalTime);
    }
    if (durationInTraffic != null) {
      sb.append(", durationInTraffic=").append(durationInTraffic);
    }
    sb.append(", duration=").append(duration);
    sb.append(", distance=").append(distance);
    sb.append(": ").append(steps.length).append(" steps]");
    return sb.toString();
  }
}
