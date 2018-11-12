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

/** A point that has been snapped to a road by the Roads API. */
public class SnappedPoint implements Serializable {

  private static final long serialVersionUID = 1L;
  /** A latitude/longitude value representing the snapped location. */
  public LatLng location;

  /**
   * The index of the corresponding value in the original request. Each value in the request should
   * map to a snapped value in the response. However, if you've set interpolate=true, then it's
   * possible that the response will contain more coordinates than the request. Interpolated values
   * will not have an originalIndex. These values are indexed from 0, so a point with an
   * originalIndex of 4 will be the snapped value of the 5th lat/lng passed to the path parameter.
   *
   * <p>A point that was not on the original path, or when interpolate=false, will have an
   * originalIndex of -1.
   */
  public int originalIndex = -1;

  /**
   * A unique identifier for a place. All placeIds returned by the Roads API will correspond to road
   * segments. The placeId can be passed to the speedLimit method to determine the speed limit along
   * that road segment.
   */
  public String placeId;

  @Override
  public String toString() {
    return String.format("[%s, placeId=%s, originalIndex=%s]", location, placeId, originalIndex);
  }
}
