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

/** A combined snap-to-roads and speed limit response. */
public class SnappedSpeedLimitResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Speed limit results. */
  public SpeedLimit[] speedLimits;

  /** Snap-to-road results. */
  public SnappedPoint[] snappedPoints;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[SnappedSpeedLimitResponse:");
    if (speedLimits != null && speedLimits.length > 0) {
      sb.append(" ").append(speedLimits.length).append(" speedLimits");
    }
    if (snappedPoints != null && snappedPoints.length > 0) {
      sb.append(" ").append(snappedPoints.length).append(" speedLimits");
    }
    sb.append("]");
    return sb.toString();
  }
}
