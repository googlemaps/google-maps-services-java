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

import static com.google.maps.internal.StringJoin.join;

import com.google.maps.internal.StringJoin;

/**
 * This class represents a component filter for a geocode request. In a geocoding response, the
 * Google Geocoding API can return address results restricted to a specific area. The restriction is
 * specified using the components filter.
 *
 * <p>Please see <a href="https://developers.google.com/maps/documentation/geocoding/#ComponentFiltering">
 * Component Filtering</a> for more detail.
 */
public class ComponentFilter implements StringJoin.UrlValue {
  public final String component;
  public final String value;

  ComponentFilter(String component, String value) {
    this.component = component;
    this.value = value;
  }

  @Override
  public String toString() {
    return toUrlValue();
  }

  @Override
  public String toUrlValue() {
    return join(':', component, value);
  }

  /**
   * {@code route} matches long or short name of a route.
   */
  public static ComponentFilter route(String route) {
    return new ComponentFilter("route", route);
  }

  /**
   * {@code locality} matches against both locality and sublocality types.
   */
  public static ComponentFilter locality(String locality) {
    return new ComponentFilter("locality", locality);
  }

  /**
   * {@code administrativeArea} matches all the administrative area levels.
   */
  public static ComponentFilter administrativeArea(String administrativeArea) {
    return new ComponentFilter("administrative_area", administrativeArea);
  }

  /**
   * {@code postalCode} matches postal code and postal code prefix.
   */
  public static ComponentFilter postalCode(String postalCode) {
    return new ComponentFilter("postal_code", postalCode);
  }

  /**
   * {@code country} matches a country name or a two letter ISO 3166-1 country code.
   */
  public static ComponentFilter country(String country) {
    return new ComponentFilter("country", country);
  }
}
