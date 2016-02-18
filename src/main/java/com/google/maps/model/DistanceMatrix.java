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
 * A complete result from a Distance Matrix API call.
 *
 * @see <a href="https://developers.google.com/maps/documentation/distancematrix/#DistanceMatrixResponses">
 * Distance Matrix Results</a>
 */
public class DistanceMatrix {

  /**
   * {@code originAddresses} contains an array of addresses as returned by the API from your
   * original request. These are formatted by the geocoder and localized according to the language
   * parameter passed with the request.
   */
  public final String[] originAddresses;

  /**
   * {@code destinationAddresses} contains an array of addresses as returned by the API from your
   * original request. As with {@link #originAddresses}, these are localized if appropriate.
   */
  public final String[] destinationAddresses;

  /**
   * {@code rows} contains an array of elements, which in turn each contain a status, duration, and
   * distance element.
   */
  public final DistanceMatrixRow[] rows;

  public DistanceMatrix(String[] originAddresses, String[] destinationAddresses,
                        DistanceMatrixRow[] rows) {
    this.originAddresses = originAddresses;
    this.destinationAddresses = destinationAddresses;
    this.rows = rows;
  }
}
