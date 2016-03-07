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
 * A single result corresponding to a origin/destination pair in a Distance Matrix response.
 *
 * <p>Be sure to check the status for each element, as a matrix response can have a mix of
 * successful and failed elements depending on the connectivity of the origin and destination.
 */
public class DistanceMatrixElement {

  /**
   * {@code status} indicates the status of the request for this origin/destination pair.
   *
   * Will be one of {@link com.google.maps.model.DistanceMatrixElementStatus}.
   */
  public DistanceMatrixElementStatus status;

  /**
   * {@code duration} indicates the total duration of this leg
   */
  public Duration duration;

  /**
   * {@code durationInTraffic} indicates the length of time to travel this route,
   * based on current and historical traffic conditions.  The duration in traffic
   * will only be returned if all of the following are true:
   * <ol>
   *   <li>The request includes a departureTime parameter.</li>
   *   <li>The request includes a valid API key, or a valid Google Maps APIs Premium Plan client ID and signature.</li>
   *   <li>Traffic conditions are available for the requested route.</li>
   *   <li>The mode parameter is set to driving.</li>
   * </ol>
   */
  public Duration durationInTraffic;

  /**
   * {@code distance} indicates the total distance covered by this leg.
   */
  public Distance distance;

  /**
   * {@code fare} indicates the contains information about the fare (that is, the ticket costs) on
   * this route.
   */
  public Fare fare;
}
