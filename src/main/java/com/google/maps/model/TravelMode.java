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

import com.google.maps.internal.StringJoin.UrlValue;

/**
 * You may specify the transportation mode to use for calulating directions. Directions are
 * calculating as driving directions by default.
 *
 * @see <a href="https://developers.google
 * .com/maps/documentation/directions/#TravelModes">Directions API travel modes</a>
 * @see <a href="https://developers.google
 * .com/maps/documentation/distancematrix/#RequestParameters">Distance Matrix API travel modes</a>
 */
public enum TravelMode implements UrlValue {
  DRIVING("driving"), WALKING("walking"), BICYCLING("bicycling"), TRANSIT("transit");

  private final String mode;

  TravelMode(String mode) {
    this.mode = mode;
  }

  @Override
  public String toString() {
    return mode;
  }

  public static TravelMode lookup(String travelMode) {
    if (travelMode.equalsIgnoreCase(DRIVING.toString())) {
      return DRIVING;
    } else if (travelMode.equalsIgnoreCase(WALKING.toString())) {
      return WALKING;
    } else if (travelMode.equalsIgnoreCase(BICYCLING.toString())) {
      return BICYCLING;
    } else if (travelMode.equalsIgnoreCase(TRANSIT.toString())) {
      return TRANSIT;
    } else {
      throw new RuntimeException("Unknown Travel Mode '" + travelMode + "'");
    }
  }
}