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
 * The status result for a single {@link com.google.maps.model.GeocodedWaypoint}.
 *
 * @see <a href="https://developers.google.com/maps/documentation/directions/intro#StatusCodes">
 *     Documentation on status codes</a>
 */
public enum GeocodedWaypointStatus {
  /** Indicates the response contains a valid result. */
  OK,

  /** Indicates no route could be found between the origin and destination. */
  ZERO_RESULTS
}
