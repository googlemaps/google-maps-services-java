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

import static com.google.maps.internal.StringJoin.join;

import java.io.Serializable;

/**
 * The parts of an address.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/geocoding/intro#Types">Address
 * Types and Address Component Types</a> in the <a
 * href="https://developers.google.com/maps/documentation/geocoding/intro">Google Maps Geocoding API
 * Developer's Guide</a> for more detail.
 */
public class AddressComponent implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The full text description or name of the address component as returned by the Geocoder. */
  public String longName;

  /**
   * An abbreviated textual name for the address component, if available. For example, an address
   * component for the state of Alaska may have a longName of "Alaska" and a shortName of "AK" using
   * the 2-letter postal abbreviation.
   */
  public String shortName;

  /** Indicates the type of each part of the address. Examples include street number or country. */
  public AddressComponentType[] types;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[AddressComponent: ");
    sb.append("\"").append(longName).append("\"");
    if (shortName != null) {
      sb.append(" (\"").append(shortName).append("\")");
    }
    sb.append(" (").append(join(", ", (Object[]) types)).append(")");
    sb.append("]");
    return sb.toString();
  }
}
