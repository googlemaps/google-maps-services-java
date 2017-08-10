/*
 * Copyright 2016 Google Inc. All rights reserved.
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

import com.google.maps.internal.StringJoin;

/** Used by Places API to restrict search results to those within a given price range. */
public enum PriceLevel implements StringJoin.UrlValue {
  FREE("0"),
  INEXPENSIVE("1"),
  MODERATE("2"),
  EXPENSIVE("3"),
  VERY_EXPENSIVE("4"),

  /**
   * Indicates an unknown price level type returned by the server. The Java Client for Google Maps
   * Services should be updated to support the new value.
   */
  UNKNOWN("Unknown");

  private final String priceLevel;

  PriceLevel(final String priceLevel) {
    this.priceLevel = priceLevel;
  }

  @Override
  public String toString() {
    return priceLevel;
  }

  @Override
  public String toUrlValue() {
    if (this == UNKNOWN) {
      throw new UnsupportedOperationException("Shouldn't use PriceLevel.UNKNOWN in a request.");
    }
    return priceLevel;
  }
}
