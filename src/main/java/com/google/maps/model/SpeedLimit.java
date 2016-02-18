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
 * A speed limit result from the Roads API.
 */
public class SpeedLimit {
  /**
   * {@code placeId} is a unique identifier for a place. All placeIds returned by the Roads API will
   * correspond to road segments.
   */
  public String placeId;

  /**
   * {@code speedLimit} is the speed limit for that road segment, specified in kilometers per hour.
   *
   * <p>To obtain the speed in miles per hour, use {@link #speedLimitMph()}.
   */
  public double speedLimit;

  /**
   * Returns the speed limit in miles per hour (MPH).
   */
  public long speedLimitMph() {
    return Math.round(speedLimit * 0.621371);
  }
}
