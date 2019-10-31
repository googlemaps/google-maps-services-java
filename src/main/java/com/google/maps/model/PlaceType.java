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

/** Used by the Places API to restrict the results to places matching the specified type. */
public enum PlaceType implements StringJoin.UrlValue {
  /** Table #1 Types https://developers.google.com/places/web-service/supported_types */
  ACCOUNTING("accounting"),
  AIRPORT("airport"),
  AMUSEMENT_PARK("amusement_park"),
  AQUARIUM("aquarium"),
  ART_GALLERY("art_gallery"),
  ATM("atm"),
  BAKERY("bakery"),
  BANK("bank"),
  BAR("bar"),
  BEAUTY_SALON("beauty_salon"),
  BICYCLE_STORE("bicycle_store"),
  BOOK_STORE("book_store"),
  BOWLING_ALLEY("bowling_alley"),
  BUS_STATION("bus_station"),
  CAFE("cafe"),
  CAMPGROUND("campground"),
  CAR_DEALER("car_dealer"),
  CAR_RENTAL("car_rental"),
  CAR_REPAIR("car_repair"),
  CAR_WASH("car_wash"),
  CASINO("casino"),
  CEMETERY("cemetery"),
  CHURCH("church"),
  CITY_HALL("city_hall"),
  CLOTHING_STORE("clothing_store"),
  CONVENIENCE_STORE("convenience_store"),
  COURTHOUSE("courthouse"),
  DENTIST("dentist"),
  DEPARTMENT_STORE("department_store"),
  DOCTOR("doctor"),
  DRUGSTORE("drugstore"),
  ELECTRICIAN("electrician"),
  ELECTRONICS_STORE("electronics_store"),
  EMBASSY("embassy"),
  FIRE_STATION("fire_station"),
  FLORIST("florist"),
  FUNERAL_HOME("funeral_home"),
  FURNITURE_STORE("furniture_store"),
  GAS_STATION("gas_station"),
  GROCERY_OR_SUPERMARKET("grocery_or_supermarket"),
  GYM("gym"),
  HAIR_CARE("hair_care"),
  HARDWARE_STORE("hardware_store"),
  HINDU_TEMPLE("hindu_temple"),
  HOME_GOODS_STORE("home_goods_store"),
  HOSPITAL("hospital"),
  INSURANCE_AGENCY("insurance_agency"),
  JEWELRY_STORE("jewelry_store"),
  LAUNDRY("laundry"),
  LAWYER("lawyer"),
  LIBRARY("library"),
  LIGHT_RAIL_STATION("light_rail_station"),
  LIQUOR_STORE("liquor_store"),
  LOCAL_GOVERNMENT_OFFICE("local_government_office"),
  LOCKSMITH("locksmith"),
  LODGING("lodging"),
  MEAL_DELIVERY("meal_delivery"),
  MEAL_TAKEAWAY("meal_takeaway"),
  MOSQUE("mosque"),
  MOVIE_RENTAL("movie_rental"),
  MOVIE_THEATER("movie_theater"),
  MOVING_COMPANY("moving_company"),
  MUSEUM("museum"),
  NIGHT_CLUB("night_club"),
  PAINTER("painter"),
  PARK("park"),
  PARKING("parking"),
  PET_STORE("pet_store"),
  PHARMACY("pharmacy"),
  PHYSIOTHERAPIST("physiotherapist"),
  PLUMBER("plumber"),
  POLICE("police"),
  POST_OFFICE("post_office"),
  PRIMARY_SCHOOL("primary_school"),
  REAL_ESTATE_AGENCY("real_estate_agency"),
  RESTAURANT("restaurant"),
  ROOFING_CONTRACTOR("roofing_contractor"),
  RV_PARK("rv_park"),
  SCHOOL("school"),
  SECONDARY_SCHOOL("secondary_school"),
  SHOE_STORE("shoe_store"),
  SHOPPING_MALL("shopping_mall"),
  SPA("spa"),
  STADIUM("stadium"),
  STORAGE("storage"),
  STORE("store"),
  SUBWAY_STATION("subway_station"),
  SUPERMARKET("supermarket"),
  SYNAGOGUE("synagogue"),
  TAXI_STAND("taxi_stand"),
  TOURIST_ATTRACTION("tourist_attraction"),
  TRAIN_STATION("train_station"),
  TRANSIT_STATION("transit_station"),
  TRAVEL_AGENCY("travel_agency"),
  UNIVERSITY("university"),
  VETERINARY_CARE("veterinary_care"),
  ZOO("zoo"),

  /** Table #2 Types https://developers.google.com/places/web-service/supported_types */
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

  UNKNOWN("unknown");

  PlaceType(final String placeType) {
    this.placeType = placeType;
  }

  private final String placeType;

  @Override
  public String toUrlValue() {
    if (this == UNKNOWN) {
      throw new UnsupportedOperationException("Shouldn't use PlaceType.UNKNOWN in a request.");
    }
    return placeType;
  }
  
  @Override
  public String toString() {
    return placeType;
  }
}
