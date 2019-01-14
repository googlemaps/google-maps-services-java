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

import com.google.maps.internal.PolylineEncoding;
import java.io.Serializable;
import java.util.List;

/**
 * Encoded Polylines are used by the API to represent paths.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/utilities/polylinealgorithm">
 * Encoded Polyline Algorithm</a> for more detail on the protocol.
 */
public class EncodedPolyline implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String points;

  public EncodedPolyline() {
    this.points = null;
  }

  /**
   * @param encodedPoints A string representation of a path, encoded with the Polyline Algorithm.
   */
  public EncodedPolyline(String encodedPoints) {
    this.points = encodedPoints;
  }

  /** @param points A path as a collection of {@code LatLng} points. */
  public EncodedPolyline(List<LatLng> points) {
    this.points = PolylineEncoding.encode(points);
  }

  public String getEncodedPath() {
    return points;
  }

  public List<LatLng> decodePath() {
    return PolylineEncoding.decode(points);
  }

  // Use the encoded point representation; decoding to get an alternate representation for
  // individual points would be expensive.
  @Override
  public String toString() {
    return String.format("[EncodedPolyline: %s]", points);
  }
}
