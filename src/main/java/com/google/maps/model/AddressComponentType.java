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
 * The Address Component types. Please see <a href=
 * "https://developers.google.com/maps/documentation/geocoding/intro#Types">Address Types and
 * Address Component Types</a> for more detail.
 */
public enum AddressComponentType {
  ADMINISTRATIVE_AREA_LEVEL_1("administrative_area_level_1"),
  ADMINISTRATIVE_AREA_LEVEL_2("administrative_area_level_2"),
  ADMINISTRATIVE_AREA_LEVEL_3("administrative_area_level_3"),
  ADMINISTRATIVE_AREA_LEVEL_4("administrative_area_level_4"),
  ADMINISTRATIVE_AREA_LEVEL_5("administrative_area_level_5"),
  ARCHIPELAGO("archipelago"),
  COLLOQUIAL_AREA("colloquial_area"),
  CONTINENT("continent"),
  COUNTRY("country"),
  ESTABLISHMENT("establishment"),
  FINANCE("finance"),
  FLOOR("floor"),
  FOOD("food"),
  GENERAL_CONTRACTOR("general_contractor"),
  GEOCODE("geocode"),
  HEALTH("health"),
  INTERSECTION("intersection"),
  LOCALITY("locality"),
  NATURAL_FEATURE("natural_feature"),
  NEIGHBORHOOD("neighborhood"),
  PLACE_OF_WORSHIP("place_of_worship"),
  POINT_OF_INTEREST("point_of_interest"),
  POLITICAL("political"),
  POST_BOX("post_box"),
  POSTAL_CODE("postal_code"),
  POSTAL_CODE_PREFIX("postal_code_prefix"),
  POSTAL_CODE_SUFFIX("postal_code_suffix"),
  POSTAL_TOWN("postal_town"),
  PREMISE("premise"),
  ROOM("room"),
  ROUTE("route"),
  STREET_ADDRESS("street_address"),
  STREET_NUMBER("street_number"),
  SUBLOCALITY("sublocality"),
  SUBLOCALITY_LEVEL_1("sublocality_level_1"),
  SUBLOCALITY_LEVEL_2("sublocality_level_2"),
  SUBLOCALITY_LEVEL_3("sublocality_level_3"),
  SUBLOCALITY_LEVEL_4("sublocality_level_4"),
  SUBLOCALITY_LEVEL_5("sublocality_level_5"),
  SUBPREMISE("subpremise"),
  @Deprecated
  WARD("ward"),
  /**
   * Indicates an unknown address component type returned by the server. The Java Client for Google
   * Maps Services should be updated to support the new value.
   */
  UNKNOWN("unknown");

  private final String addressComponentType;

  AddressComponentType(final String addressComponentType) {
    this.addressComponentType = addressComponentType;
  }

  @Override
  public String toString() {
    return addressComponentType;
  }

  public String toCanonicalLiteral() {
    return toString();
  }
}
