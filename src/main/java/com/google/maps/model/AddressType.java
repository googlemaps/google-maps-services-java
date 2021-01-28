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
 * The Address types. Please see <a
 * href="https://developers.google.com/maps/documentation/geocoding/intro#Types">Address Types and
 * Address Component Types</a> for more detail. Some addresses contain additional place categories.
 * Please see <a href="https://developers.google.com/places/supported_types">Place Types</a> for
 * more detail.
 */
public enum AddressType implements UrlValue {

  /** A precise street address. */
  STREET_ADDRESS("street_address"),

  /** A precise street number. */
  STREET_NUMBER("street_number"),

  /** The floor in the address of the building. */
  FLOOR("floor"),

  /** The room in the address of the building */
  ROOM("room"),

  /** A specific mailbox. */
  POST_BOX("post_box"),

  /** A named route (such as "US 101"). */
  ROUTE("route"),

  /** A major intersection, usually of two major roads. */
  INTERSECTION("intersection"),

  /** A continent. */
  CONTINENT("continent"),

  /** A political entity. Usually, this type indicates a polygon of some civil administration. */
  POLITICAL("political"),

  /** The national political entity, typically the highest order type returned by the Geocoder. */
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
   * A first-order civil entity below a locality. Some locations may receive one of the additional
   * types: {@code SUBLOCALITY_LEVEL_1} to {@code SUBLOCALITY_LEVEL_5}. Each sublocality level is a
   * civil entity. Larger numbers indicate a smaller geographic area.
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

  /** A postal code prefix as used to address postal mail within the country. */
  POSTAL_CODE_SUFFIX("postal_code_suffix"),

  /* Plus code */
  PLUS_CODE("plus_code"),

  /** A prominent natural feature. */
  NATURAL_FEATURE("natural_feature"),

  /** An airport. */
  AIRPORT("airport"),

  /** A university. */
  UNIVERSITY("university"),

  /** A named park. */
  PARK("park"),

  /** A museum. */
  MUSEUM("museum"),

  /**
   * A named point of interest. Typically, these "POI"s are prominent local entities that don't
   * easily fit in another category, such as "Empire State Building" or "Statue of Liberty."
   */
  POINT_OF_INTEREST("point_of_interest"),

  /** A place that has not yet been categorized. */
  ESTABLISHMENT("establishment"),

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

  /** The location of a church. */
  CHURCH("church"),

  /** The location of a primary school. */
  PRIMARY_SCHOOL("primary_school"),

  /** The location of a secondary school. */
  SECONDARY_SCHOOL("secondary_school"),

  /** The location of a finance institute. */
  FINANCE("finance"),

  /** The location of a post office. */
  POST_OFFICE("post_office"),

  /** The location of a place of worship. */
  PLACE_OF_WORSHIP("place_of_worship"),

  /**
   * A grouping of geographic areas, such as locality and sublocality, used for mailing addresses in
   * some countries.
   */
  POSTAL_TOWN("postal_town"),

  /** Currently not a documented return type. */
  SYNAGOGUE("synagogue"),

  /** Currently not a documented return type. */
  FOOD("food"),

  /** Currently not a documented return type. */
  GROCERY_OR_SUPERMARKET("grocery_or_supermarket"),

  /** Currently not a documented return type. */
  STORE("store"),

  /** The location of a drugstore. */
  DRUGSTORE("drugstore"),

  /** Currently not a documented return type. */
  LAWYER("lawyer"),

  /** Currently not a documented return type. */
  HEALTH("health"),

  /** Currently not a documented return type. */
  INSURANCE_AGENCY("insurance_agency"),

  /** Currently not a documented return type. */
  GAS_STATION("gas_station"),

  /** Currently not a documented return type. */
  CAR_DEALER("car_dealer"),

  /** Currently not a documented return type. */
  CAR_REPAIR("car_repair"),

  /** Currently not a documented return type. */
  MEAL_TAKEAWAY("meal_takeaway"),

  /** Currently not a documented return type. */
  FURNITURE_STORE("furniture_store"),

  /** Currently not a documented return type. */
  HOME_GOODS_STORE("home_goods_store"),

  /** Currently not a documented return type. */
  SHOPPING_MALL("shopping_mall"),

  /** Currently not a documented return type. */
  GYM("gym"),

  /** Currently not a documented return type. */
  ACCOUNTING("accounting"),

  /** Currently not a documented return type. */
  MOVING_COMPANY("moving_company"),

  /** Currently not a documented return type. */
  LODGING("lodging"),

  /** Currently not a documented return type. */
  STORAGE("storage"),

  /** Currently not a documented return type. */
  CASINO("casino"),

  /** Currently not a documented return type. */
  PARKING("parking"),

  /** Currently not a documented return type. */
  STADIUM("stadium"),

  /** Currently not a documented return type. */
  TRAVEL_AGENCY("travel_agency"),

  /** Currently not a documented return type. */
  NIGHT_CLUB("night_club"),

  /** Currently not a documented return type. */
  BEAUTY_SALON("beauty_salon"),

  /** Currently not a documented return type. */
  HAIR_CARE("hair_care"),

  /** Currently not a documented return type. */
  SPA("spa"),

  /** Currently not a documented return type. */
  SHOE_STORE("shoe_store"),

  /** Currently not a documented return type. */
  BAKERY("bakery"),

  /** Currently not a documented return type. */
  PHARMACY("pharmacy"),

  /** Currently not a documented return type. */
  SCHOOL("school"),

  /** Currently not a documented return type. */
  BOOK_STORE("book_store"),

  /** Currently not a documented return type. */
  DEPARTMENT_STORE("department_store"),

  /** Currently not a documented return type. */
  RESTAURANT("restaurant"),

  /** Currently not a documented return type. */
  REAL_ESTATE_AGENCY("real_estate_agency"),

  /** Currently not a documented return type. */
  BAR("bar"),

  /** Currently not a documented return type. */
  DOCTOR("doctor"),

  /** Currently not a documented return type. */
  HOSPITAL("hospital"),

  /** Currently not a documented return type. */
  FIRE_STATION("fire_station"),

  /** Currently not a documented return type. */
  SUPERMARKET("supermarket"),

  /** Currently not a documented return type. */
  CITY_HALL("city_hall"),

  /** Currently not a documented return type. */
  LOCAL_GOVERNMENT_OFFICE("local_government_office"),

  /** Currently not a documented return type. */
  ATM("atm"),

  /** Currently not a documented return type. */
  BANK("bank"),

  /** Currently not a documented return type. */
  LIBRARY("library"),

  /** Currently not a documented return type. */
  CAR_WASH("car_wash"),

  /** Currently not a documented return type. */
  HARDWARE_STORE("hardware_store"),

  /** Currently not a documented return type. */
  AMUSEMENT_PARK("amusement_park"),

  /** Currently not a documented return type. */
  AQUARIUM("aquarium"),

  /** Currently not a documented return type. */
  ART_GALLERY("art_gallery"),

  /** Currently not a documented return type. */
  BICYCLE_STORE("bicycle_store"),

  /** Currently not a documented return type. */
  BOWLING_ALLEY("bowling_alley"),

  /** Currently not a documented return type. */
  CAFE("cafe"),

  /** Currently not a documented return type. */
  CAMPGROUND("campground"),

  /** Currently not a documented return type. */
  CAR_RENTAL("car_rental"),

  /** Currently not a documented return type. */
  CEMETERY("cemetery"),

  /** Currently not a documented return type. */
  CLOTHING_STORE("clothing_store"),

  /** Currently not a documented return type. */
  CONVENIENCE_STORE("convenience_store"),

  /** Currently not a documented return type. */
  COURTHOUSE("courthouse"),

  /** Currently not a documented return type. */
  DENTIST("dentist"),

  /** Currently not a documented return type. */
  ELECTRICIAN("electrician"),

  /** Currently not a documented return type. */
  ELECTRONICS_STORE("electronics_store"),

  /** Currently not a documented return type. */
  EMBASSY("embassy"),

  /** Currently not a documented return type. */
  FLORIST("florist"),

  /** Currently not a documented return type. */
  FUNERAL_HOME("funeral_home"),

  /** Currently not a documented return type. */
  GENERAL_CONTRACTOR("general_contractor"),

  /** Currently not a documented return type. */
  HINDU_TEMPLE("hindu_temple"),

  /** Currently not a documented return type. */
  JEWELRY_STORE("jewelry_store"),

  /** Currently not a documented return type. */
  LAUNDRY("laundry"),

  /** Currently not a documented return type. */
  LIQUOR_STORE("liquor_store"),

  /** Currently not a documented return type. */
  LOCKSMITH("locksmith"),

  /** Currently not a documented return type. */
  MEAL_DELIVERY("meal_delivery"),

  /** Currently not a documented return type. */
  MOSQUE("mosque"),

  /** Currently not a documented return type. */
  MOVIE_RENTAL("movie_rental"),

  /** Currently not a documented return type. */
  MOVIE_THEATER("movie_theater"),

  /** Currently not a documented return type. */
  PAINTER("painter"),

  /** Currently not a documented return type. */
  PET_STORE("pet_store"),

  /** Currently not a documented return type. */
  PHYSIOTHERAPIST("physiotherapist"),

  /** Currently not a documented return type. */
  PLUMBER("plumber"),

  /** Currently not a documented return type. */
  POLICE("police"),

  /** Currently not a documented return type. */
  ROOFING_CONTRACTOR("roofing_contractor"),

  /** Currently not a documented return type. */
  RV_PARK("rv_park"),

  /** Currently not a documented return type. */
  TAXI_STAND("taxi_stand"),

  /** Currently not a documented return type. */
  VETERINARY_CARE("veterinary_care"),

  /** Currently not a documented return type. */
  ZOO("zoo"),

  /** An archipelago. */
  ARCHIPELAGO("archipelago"),

  /** A tourist attraction */
  TOURIST_ATTRACTION("tourist_attraction"),

  /** Currently not a documented return type. */
  TOWN_SQUARE("town_square"),

  /**
   * Indicates an unknown address type returned by the server. The Java Client for Google Maps
   * Services should be updated to support the new value.
   */
  UNKNOWN("unknown");

  private final String addressType;

  AddressType(final String addressType) {
    this.addressType = addressType;
  }

  @Override
  public String toString() {
    return addressType;
  }

  public String toCanonicalLiteral() {
    return toString();
  }

  @Override
  public String toUrlValue() {
    if (this == UNKNOWN) {
      throw new UnsupportedOperationException("Shouldn't use AddressType.UNKNOWN in a request.");
    }
    return addressType;
  }
}
