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
 * Result from a Geocoding API call.
 */
public class GeocodingResult {

  /**
   * {@code addressComponents} is an array containing the separate address components.
   */
  public AddressComponent[] addressComponents;

  /**
   * {@code formattedAddress} is the human-readable address of this location. Often this address is
   * equivalent to the "postal address," which sometimes differs from country to country. (Note that
   * some countries, such as the United Kingdom, do not allow distribution of true postal addresses
   * due to licensing restrictions.) This address is generally composed of one or more address
   * components. For example, the address "111 8th Avenue, New York, NY" contains separate address
   * components for "111" (the street number, "8th Avenue" (the route), "New York" (the city) and
   * "NY" (the US state). These address components contain additional information.
   */
  public String formattedAddress;

  /**
   * {@code postcodeLocalities} is an array denoting all the localities contained in a postal code.
   * This is only present when the result is a postal code that contains multiple localities.
   */
  public String[] postcodeLocalities;

  /**
   * {@code geometry} contains location information.
   */
  public Geometry geometry;

  /**
   * The {@code types} array indicates the type of the returned result. This array contains a set of
   * zero or more tags identifying the type of feature returned in the result. For example, a
   * geocode of "Chicago" returns "locality" which indicates that "Chicago" is a city, and also
   * returns "political" which indicates it is a political entity.
   */
  public AddressType[] types;

  /**
   * {@code partialMatch} indicates that the geocoder did not return an exact match for the original
   * request, though it was able to match part of the requested address. You may wish to examine the
   * original request for misspellings and/or an incomplete address.
   *
   * <p>Partial matches most often occur for street addresses that do not exist within the locality
   * you pass in the request. Partial matches may also be returned when a request matches two or
   * more locations in the same locality. For example, "21 Henr St, Bristol, UK" will return a
   * partial match for both Henry Street and Henrietta Street. Note that if a request includes a
   * misspelled address component, the geocoding service may suggest an alternate address.
   * Suggestions triggered in this way will not be marked as a partial match.
   */
  public boolean partialMatch;

  /**
   * {@code placeId} is a unique identifier for a place.
   */
  public String placeId;
}
