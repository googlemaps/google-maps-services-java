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
 * The Address Component types. Please see <a
 * href="https://developers.google.com/maps/documentation/geocoding/intro#Types">Address Types and
 * Address Component Types</a> for more detail.
 */
public enum AddressComponentType {

  /** A precise street address. */
  STREET_ADDRESS("street_address"),

  /** A named route (such as "US 101"). */
  ROUTE("route"),

  /** A major intersection, usually of two major roads. */
  INTERSECTION("intersection"),

  /** A continent. */
  CONTINENT("continent"),

  /** A political entity. Usually, this type indicates a polygon of some civil administration. */
  POLITICAL("political"),

  /** A national political entity, typically the highest order type returned by the Geocoder. */
  COUNTRY("country"),

  /**
   * A first-order civil entity below the country level. Within the United States, these
   * administrative levels are states. Not all nations exhibit these administrative levels.
   */
  ADMINISTRATIVE_AREA_LEVEL_1("administrative_area_level_1"),

  /**
   * A second-order civil entity below the country level. Within the United States, these
   * administrative levels are counties. Not all nations exhibit these administrative levels.
   */
  ADMINISTRATIVE_AREA_LEVEL_2("administrative_area_level_2"),

  /**
   * A third-order civil entity below the country level. This type indicates a minor civil division.
   * Not all nations exhibit these administrative levels.
   */
  ADMINISTRATIVE_AREA_LEVEL_3("administrative_area_level_3"),

  /**
   * A fourth-order civil entity below the country level. This type indicates a minor civil
   * division. Not all nations exhibit these administrative levels.
   */
  ADMINISTRATIVE_AREA_LEVEL_4("administrative_area_level_4"),

  /**
   * A fifth-order civil entity below the country level. This type indicates a minor civil division.
   * Not all nations exhibit these administrative levels.
   */
  ADMINISTRATIVE_AREA_LEVEL_5("administrative_area_level_5"),

  /** A commonly-used alternative name for the entity. */
  COLLOQUIAL_AREA("colloquial_area"),

  /** An incorporated city or town political entity. */
  LOCALITY("locality"),

  /**
   * A specific type of Japanese locality, used to facilitate distinction between multiple locality
   * components within a Japanese address.
   */
  WARD("ward"),

  /**
   * A first-order civil entity below a locality. For some locations may receive one of the
   * additional types: sublocality_level_1 to sublocality_level_5. Each sublocality level is a civil
   * entity. Larger numbers indicate a smaller geographic area.
   */
  SUBLOCALITY("sublocality"),
  SUBLOCALITY_LEVEL_1("sublocality_level_1"),
  SUBLOCALITY_LEVEL_2("sublocality_level_2"),
  SUBLOCALITY_LEVEL_3("sublocality_level_3"),
  SUBLOCALITY_LEVEL_4("sublocality_level_4"),
  SUBLOCALITY_LEVEL_5("sublocality_level_5"),

  /** A named neighborhood. */
  NEIGHBORHOOD("neighborhood"),

  /** A named location, usually a building or collection of buildings with a common name. */
  PREMISE("premise"),

  /**
   * A first-order entity below a named location, usually a singular building within a collection of
   * buildings with a common name.
   */
  SUBPREMISE("subpremise"),

  /** A postal code as used to address postal mail within the country. */
  POSTAL_CODE("postal_code"),

  /** A postal code prefix as used to address postal mail within the country. */
  POSTAL_CODE_PREFIX("postal_code_prefix"),

  /** A postal code suffix as used to address postal mail within the country. */
  POSTAL_CODE_SUFFIX("postal_code_suffix"),

  /** A prominent natural feature. */
  NATURAL_FEATURE("natural_feature"),

  /** An airport. */
  AIRPORT("airport"),

  /** A named park. */
  PARK("park"),

  /**
   * A named point of interest. Typically, these "POI"s are prominent local entities that don't
   * easily fit in another category, such as "Empire State Building" or "Statue of Liberty."
   */
  POINT_OF_INTEREST("point_of_interest"),

  /** The floor of a building address. */
  FLOOR("floor"),

  /** Typically indicates a place that has not yet been categorized. */
  ESTABLISHMENT("establishment"),

  /** A parking lot or parking structure. */
  PARKING("parking"),

  /** A specific postal box. */
  POST_BOX("post_box"),

  /**
   * A grouping of geographic areas, such as locality and sublocality, used for mailing addresses in
   * some countries.
   */
  POSTAL_TOWN("postal_town"),

  /** The room of a building address. */
  ROOM("room"),

  /** The precise street number of an address. */
  STREET_NUMBER("street_number"),

  /** The location of a bus stop. */
  BUS_STATION("bus_station"),

  /** The location of a train station. */
  TRAIN_STATION("train_station"),

  /** The location of a subway station. */
  SUBWAY_STATION("subway_station"),

  /** The location of a transit station. */
  TRANSIT_STATION("transit_station"),

  /** The location of a light rail station. */
  LIGHT_RAIL_STATION("light_rail_station"),

  /** A general contractor. */
  GENERAL_CONTRACTOR("general_contractor"),

  /** A food service establishment. */
  FOOD("food"),

  /** A real-estate agency. */
  REAL_ESTATE_AGENCY("real_estate_agency"),

  /** A car-rental establishment. */
  CAR_RENTAL("car_rental"),

  /** A travel agency. */
  TRAVEL_AGENCY("travel_agency"),

  /** An electronics store. */
  ELECTRONICS_STORE("electronics_store"),

  /** A home goods store. */
  HOME_GOODS_STORE("home_goods_store"),

  /** A school. */
  SCHOOL("school"),

  /** A store. */
  STORE("store"),

  /** A shopping mall. */
  SHOPPING_MALL("shopping_mall"),

  /** A lodging establishment. */
  LODGING("lodging"),

  /** An art gallery. */
  ART_GALLERY("art_gallery"),

  /** A lawyer. */
  LAWYER("lawyer"),

  /** A restaurant. */
  RESTAURANT("restaurant"),

  /** A bar. */
  BAR("bar"),

  /** A take-away meal establishment. */
  MEAL_TAKEAWAY("meal_takeaway"),

  /** A clothing store. */
  CLOTHING_STORE("clothing_store"),

  /** A local government office. */
  LOCAL_GOVERNMENT_OFFICE("local_government_office"),

  /** A finance establishment. */
  FINANCE("finance"),

  /** A moving company. */
  MOVING_COMPANY("moving_company"),

  /** A storage establishment. */
  STORAGE("storage"),

  /** A cafe. */
  CAFE("cafe"),

  /** A car repair establishment. */
  CAR_REPAIR("car_repair"),

  /** A health service provider. */
  HEALTH("health"),

  /** An insurance agency. */
  INSURANCE_AGENCY("insurance_agency"),

  /** A painter. */
  PAINTER("painter"),

  /** An archipelago. */
  ARCHIPELAGO("archipelago"),

  /** A museum. */
  MUSEUM("museum"),

  /** A campground. */
  CAMPGROUND("campground"),

  /** An RV park. */
  RV_PARK("rv_park"),

  /** A meal delivery establishment. */
  MEAL_DELIVERY("meal_delivery"),

  /** A primary school. */
  PRIMARY_SCHOOL("primary_school"),

  /** A secondary school. */
  SECONDARY_SCHOOL("secondary_school"),

  /** A town square. */
  TOWN_SQUARE("town_square"),

  /** Tourist Attraction */
  TOURIST_ATTRACTION("tourist_attraction"),

  /** Plus code */
  PLUS_CODE("plus_code"),

  /** DRUGSTORE */
  DRUGSTORE("drugstore"),

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
