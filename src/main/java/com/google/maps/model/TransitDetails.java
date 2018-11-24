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
import java.time.ZonedDateTime;

/**
 * Transit directions return additional information that is not relevant for other modes of
 * transportation. These additional properties are exposed through the {@code TransitDetails}
 * object, returned as a field of an element in the {@code steps} array. From the {@code
 * TransitDetails} object you can access additional information about the transit stop, transit
 * line, and transit agency.
 */
public class TransitDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Information about the arrival stop/station for this part of the trip. */
  public StopDetails arrivalStop;

  /** Information about the departure stop/station for this part of the trip. */
  public StopDetails departureStop;

  /** The arrival time for this leg of the journey. */
  public ZonedDateTime arrivalTime;

  /** The departure time for this leg of the journey. */
  public ZonedDateTime departureTime;

  /**
   * The direction in which to travel on this line, as it is marked on the vehicle or at the
   * departure stop. This will often be the terminus station.
   */
  public String headsign;

  /**
   * The expected number of seconds between departures from the same stop at this time. For example,
   * with a headway value of 600, you would expect a ten minute wait if you should miss your bus.
   */
  public long headway;

  /**
   * The number of stops in this step, counting the arrival stop, but not the departure stop. For
   * example, if your directions involve leaving from Stop A, passing through stops B and C, and
   * arriving at stop D, {@code numStops} will equal 3.
   */
  public int numStops;

  /** Information about the transit line used in this step. */
  public TransitLine line;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    sb.append(departureStop).append(" at ").append(departureTime);
    sb.append(" -> ");
    sb.append(arrivalStop).append(" at ").append(arrivalTime);
    if (headsign != null) {
      sb.append(" (").append(headsign).append(" )");
    }
    if (line != null) {
      sb.append(" on ").append(line);
    }
    sb.append(", ").append(numStops).append(" stops");
    sb.append(", headway=").append(headway).append(" s");
    sb.append("]");
    return sb.toString();
  }
}
