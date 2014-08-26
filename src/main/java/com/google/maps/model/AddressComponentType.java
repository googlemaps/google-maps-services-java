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
   * The Adress Component types. Please see
   * <a href="https://developers.google.com/maps/documentation/geocoding/#Types">Address Component
   * Types</a> for more detail.
   */
  public enum AddressComponentType implements UrlValue {

    /**
     * {@code STREET_ADDRESS} indicates a precise street address.
     */
    STREET_ADDRESS("street_address"),

    /**
     * {@code ROUTE} indicates a named route (such as "US 101").
     */
    ROUTE("route"),

    /**
     * {@code INTERSECTION} indicates a major intersection, usually of two major roads.
     */
    INTERSECTION("intersection"),

    /**
     * {@code POLITICAL} indicates a political entity. Usually, this type indicates a polygon of
     * some civil administration.
     */
    POLITICAL("political"),

    /**
     * {@code COUNTRY} indicates the national political entity, and is typically the highest order
     * type returned by the Geocoder.
     */
    COUNTRY("country"),

    /**
     * {@code ADMINISTRATIVE_AREA_LEVEL_1} indicates a first-order civil entity below the country
     * level. Within the United States, these administrative levels are states. Not all nations
     * exhibit these administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_1("administrative_area_level_1"),

    /**
     * {@code ADMINISTRATIVE_AREA_LEVEL_2} indicates a second-order civil entity below the country
     * level. Within the United States, these administrative levels are counties. Not all nations
     * exhibit these administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_2("administrative_area_level_2"),

    /**
     * {@code ADMINISTRATIVE_AREA_LEVEL_3} indicates a third-order civil entity below the country
     * level. This type indicates a minor civil division. Not all nations exhibit these
     * administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_3("administrative_area_level_3"),

    /**
     * {@code ADMINISTRATIVE_AREA_LEVEL_4} indicates a fourth-order civil entity below the country
     * level. This type indicates a minor civil division. Not all nations exhibit these
     * administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_4("administrative_area_level_4"),

    /**
     * {@code ADMINISTRATIVE_AREA_LEVEL_5} indicates a fifth-order civil entity below the country
     * level. This type indicates a minor civil division. Not all nations exhibit these
     * administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_5("administrative_area_level_5"),

    /**
     * {@code COLLOQUIAL_AREA} indicates a commonly-used alternative name for the entity.
     */
    COLLOQUIAL_AREA("colloquial_area"),

    /**
     * {@code LOCALITY} indicates an incorporated city or town political entity.
     */
    LOCALITY("locality"),

    /**
     * {@code SUBLOCALITY} indicates a first-order civil entity below a locality. For some locations
     * may receive one of the additional types: sublocality_level_1 to sublocality_level_5. Each
     * sublocality level is a civil entity. Larger numbers indicate a smaller geographic area.
     */
    SUBLOCALITY("sublocality"),
    SUBLOCALITY_LEVEL_1("sublocality_level_1"),
    SUBLOCALITY_LEVEL_2("sublocality_level_2"),
    SUBLOCALITY_LEVEL_3("sublocality_level_3"),
    SUBLOCALITY_LEVEL_4("sublocality_level_4"),
    SUBLOCALITY_LEVEL_5("sublocality_level_5"),


    /**
     * {@code NEIGHBORHOOD} indicates a named neighborhood.
     */
    NEIGHBORHOOD("neighborhood"),

    /**
     * {@code PREMISE} indicates a named location, usually a building or collection of buildings
     * with a common name.
     */
    PREMISE("premise"),

    /**
     * {@code SUBPREMISE} indicates a first-order entity below a named location, usually a singular
     * building within a collection of buildings with a common name
     */
    SUBPREMISE("subpremise"),

    /**
     * {@code POSTAL_CODE} indicates a postal code as used to address postal mail within the
     * country.
     */
    POSTAL_CODE("postal_code"),

    /**
     * {@code NATURAL_FEATURE} indicates a prominent natural feature.
     */
    NATURAL_FEATURE("natural_feature"),

    /**
     * {@code AIRPORT} indicates an airport.
     */
    AIRPORT("airport"),

    /**
     * {@code PARK} indicates a named park.
     */
    PARK("park"),

    /**
     * {@code POINT_OF_INTEREST} indicates a named point of interest. Typically, these "POI"s are
     * prominent local entities that don't easily fit in another category, such as "Empire State
     * Building" or "Statue of Liberty."
     */
    POINT_OF_INTEREST("point_of_interest"),

    /**
     * {@code FLOOR} indicates the floor of a building address.
     */
    FLOOR("floor"),

    /**
     * {@code ESTABLISHMENT} typically indicates a place that has not yet been categorized.
     */
    ESTABLISHMENT("establishment"),

    /**
     * {@code PARKING} indicates a parking lot or parking structure.
     */
    PARKING("parking"),

    /**
     * {@code POST_BOX} indicates a specific postal box.
     */
    POST_BOX("post_box"),

    /**
     * {@code POSTAL_TOWN} indicates a grouping of geographic areas, such as locality and
     * sublocality, used for mailing addresses in some countries.
     */
    POSTAL_TOWN("postal_town"),

    /**
     * {@code ROOM} indicates the room of a building address.
     */
    ROOM("room"),

    /**
     * {@code STREET_NUMBER} indicates the precise street number.
     */
    STREET_NUMBER("street_number"),

    /**
     * {@code BUS_STATION} indicates the location of a bus stop.
     */
    BUS_STATION("bus_station"),

    /**
     * {@code TRAIN_STATION} indicates the location of a train station.
     */
    TRAIN_STATION("train_station"),

    /**
     * {@code TRANSIT_STATION} indicates the location of a transit station.
     */
    TRANSIT_STATION("transit_station");


    private String addressComponentType;

    AddressComponentType(String addressComponentType) {
      this.addressComponentType = addressComponentType;
    }

    @Override
    public String toString() {
      return addressComponentType;
    }

    public static AddressComponentType lookup(String addressComponentType) {
      if (addressComponentType.equalsIgnoreCase(STREET_ADDRESS.toString())) {
        return STREET_ADDRESS;
      } else if (addressComponentType.equalsIgnoreCase(ROUTE.toString())) {
        return ROUTE;
      } else if (addressComponentType.equalsIgnoreCase(INTERSECTION.toString())) {
        return INTERSECTION;
      } else if (addressComponentType.equalsIgnoreCase(POLITICAL.toString())) {
        return POLITICAL;
      } else if (addressComponentType.equalsIgnoreCase(COUNTRY.toString())) {
        return COUNTRY;
      } else if (addressComponentType.equalsIgnoreCase(ADMINISTRATIVE_AREA_LEVEL_1.toString())) {
        return ADMINISTRATIVE_AREA_LEVEL_1;
      } else if (addressComponentType.equalsIgnoreCase(ADMINISTRATIVE_AREA_LEVEL_2.toString())) {
        return ADMINISTRATIVE_AREA_LEVEL_2;
      } else if (addressComponentType.equalsIgnoreCase(ADMINISTRATIVE_AREA_LEVEL_3.toString())) {
        return ADMINISTRATIVE_AREA_LEVEL_3;
      } else if (addressComponentType.equalsIgnoreCase(ADMINISTRATIVE_AREA_LEVEL_4.toString())) {
        return ADMINISTRATIVE_AREA_LEVEL_4;
      } else if (addressComponentType.equalsIgnoreCase(ADMINISTRATIVE_AREA_LEVEL_5.toString())) {
        return ADMINISTRATIVE_AREA_LEVEL_5;
      } else if (addressComponentType.equalsIgnoreCase(COLLOQUIAL_AREA.toString())) {
        return COLLOQUIAL_AREA;
      } else if (addressComponentType.equalsIgnoreCase(LOCALITY.toString())) {
        return LOCALITY;
      } else if (addressComponentType.equalsIgnoreCase(SUBLOCALITY.toString())) {
        return SUBLOCALITY;
      } else if (addressComponentType.equalsIgnoreCase(NEIGHBORHOOD.toString())) {
        return NEIGHBORHOOD;
      } else if (addressComponentType.equalsIgnoreCase(PREMISE.toString())) {
        return PREMISE;
      } else if (addressComponentType.equalsIgnoreCase(SUBPREMISE.toString())) {
        return SUBPREMISE;
      } else if (addressComponentType.equalsIgnoreCase(POSTAL_CODE.toString())) {
        return POSTAL_CODE;
      } else if (addressComponentType.equalsIgnoreCase(NATURAL_FEATURE.toString())) {
        return NATURAL_FEATURE;
      } else if (addressComponentType.equalsIgnoreCase(AIRPORT.toString())) {
        return AIRPORT;
      } else if (addressComponentType.equalsIgnoreCase(PARK.toString())) {
        return PARK;
      } else if (addressComponentType.equalsIgnoreCase(POINT_OF_INTEREST.toString())) {
        return POINT_OF_INTEREST;
      } else if (addressComponentType.equalsIgnoreCase(FLOOR.toString())) {
        return FLOOR;
      } else if (addressComponentType.equalsIgnoreCase(ESTABLISHMENT.toString())) {
        return ESTABLISHMENT;
      } else if (addressComponentType.equalsIgnoreCase(PARKING.toString())) {
        return PARKING;
      } else if (addressComponentType.equalsIgnoreCase(POST_BOX.toString())) {
        return POST_BOX;
      } else if (addressComponentType.equalsIgnoreCase(POSTAL_TOWN.toString())) {
        return POSTAL_TOWN;
      } else if (addressComponentType.equalsIgnoreCase(ROOM.toString())) {
        return ROOM;
      } else if (addressComponentType.equalsIgnoreCase(STREET_NUMBER.toString())) {
        return STREET_NUMBER;
      } else if (addressComponentType.equalsIgnoreCase(BUS_STATION.toString())) {
        return BUS_STATION;
      } else if (addressComponentType.equalsIgnoreCase(TRAIN_STATION.toString())) {
        return TRAIN_STATION;
      } else if (addressComponentType.equalsIgnoreCase(TRANSIT_STATION.toString())) {
        return TRANSIT_STATION;
      } else if (addressComponentType.equalsIgnoreCase(SUBLOCALITY_LEVEL_1.toString())) {
        return SUBLOCALITY_LEVEL_1;
      } else if (addressComponentType.equalsIgnoreCase(SUBLOCALITY_LEVEL_2.toString())) {
        return SUBLOCALITY_LEVEL_2;
      } else if (addressComponentType.equalsIgnoreCase(SUBLOCALITY_LEVEL_3.toString())) {
        return SUBLOCALITY_LEVEL_3;
      } else if (addressComponentType.equalsIgnoreCase(SUBLOCALITY_LEVEL_4.toString())) {
        return SUBLOCALITY_LEVEL_4;
      } else if (addressComponentType.equalsIgnoreCase(SUBLOCALITY_LEVEL_5.toString())) {
        return SUBLOCALITY_LEVEL_5;
      } else {
        throw new RuntimeException("Unknown address component type '" + addressComponentType + "'");
      }
    }
  }

