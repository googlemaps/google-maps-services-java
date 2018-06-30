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

import static com.google.maps.internal.StringJoin.UrlValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.google.maps.SmallTests;
import java.util.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(SmallTests.class)
public class EnumsTest {
  @Test
  public void testUnknown() throws Exception {
    assertNotNull(AddressComponentType.UNKNOWN); // Does not implement UrlValue.

    assertCannotGetUrlValue(AddressType.UNKNOWN);
    assertCannotGetUrlValue(LocationType.UNKNOWN);
    assertCannotGetUrlValue(TravelMode.UNKNOWN);
  }

  @Test
  public void testCanonicalLiteralsForAddressType() {
    Map<AddressType, String> addressTypeToLiteralMap = new HashMap<AddressType, String>();
    // Short alias just to avoid line wrapping in the below code
    Map<AddressType, String> m = addressTypeToLiteralMap;
    m.put(AddressType.STREET_ADDRESS, "street_address");
    m.put(AddressType.ROUTE, "route");
    m.put(AddressType.INTERSECTION, "intersection");
    m.put(AddressType.POLITICAL, "political");
    m.put(AddressType.COUNTRY, "country");
    m.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_1, "administrative_area_level_1");
    m.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_2, "administrative_area_level_2");
    m.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_3, "administrative_area_level_3");
    m.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_4, "administrative_area_level_4");
    m.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_5, "administrative_area_level_5");
    m.put(AddressType.COLLOQUIAL_AREA, "colloquial_area");
    m.put(AddressType.LOCALITY, "locality");
    m.put(AddressType.WARD, "ward");
    m.put(AddressType.SUBLOCALITY, "sublocality");
    m.put(AddressType.SUBLOCALITY_LEVEL_1, "sublocality_level_1");
    m.put(AddressType.SUBLOCALITY_LEVEL_2, "sublocality_level_2");
    m.put(AddressType.SUBLOCALITY_LEVEL_3, "sublocality_level_3");
    m.put(AddressType.SUBLOCALITY_LEVEL_4, "sublocality_level_4");
    m.put(AddressType.SUBLOCALITY_LEVEL_5, "sublocality_level_5");
    m.put(AddressType.NEIGHBORHOOD, "neighborhood");
    m.put(AddressType.PREMISE, "premise");
    m.put(AddressType.SUBPREMISE, "subpremise");
    m.put(AddressType.POSTAL_CODE, "postal_code");
    m.put(AddressType.NATURAL_FEATURE, "natural_feature");
    m.put(AddressType.AIRPORT, "airport");
    m.put(AddressType.PARK, "park");
    m.put(AddressType.POINT_OF_INTEREST, "point_of_interest");
    m.put(AddressType.POST_OFFICE, "post_office");
    m.put(AddressType.PLACE_OF_WORSHIP, "place_of_worship");
    m.put(AddressType.BUS_STATION, "bus_station");
    m.put(AddressType.TRAIN_STATION, "train_station");
    m.put(AddressType.SUBWAY_STATION, "subway_station");
    m.put(AddressType.TRANSIT_STATION, "transit_station");
    m.put(AddressType.CHURCH, "church");
    m.put(AddressType.FINANCE, "finance");
    m.put(AddressType.ESTABLISHMENT, "establishment");
    m.put(AddressType.POSTAL_TOWN, "postal_town");
    m.put(AddressType.UNIVERSITY, "university");

    for (Map.Entry<AddressType, String> addressTypeLiteralPair :
        addressTypeToLiteralMap.entrySet()) {
      assertEquals(
          addressTypeLiteralPair.getValue(), addressTypeLiteralPair.getKey().toCanonicalLiteral());
    }
  }

  @Test
  public void testCanonicalLiteralsForAddressComponentType() {
    Map<AddressComponentType, String> addressComponentTypeToLiteralMap =
        new HashMap<AddressComponentType, String>();
    // Short alias just to avoid line wrapping in the below code
    Map<AddressComponentType, String> m = addressComponentTypeToLiteralMap;
    m.put(AddressComponentType.STREET_ADDRESS, "street_address");
    m.put(AddressComponentType.ROUTE, "route");
    m.put(AddressComponentType.INTERSECTION, "intersection");
    m.put(AddressComponentType.POLITICAL, "political");
    m.put(AddressComponentType.COUNTRY, "country");
    m.put(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1, "administrative_area_level_1");
    m.put(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2, "administrative_area_level_2");
    m.put(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_3, "administrative_area_level_3");
    m.put(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_4, "administrative_area_level_4");
    m.put(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_5, "administrative_area_level_5");
    m.put(AddressComponentType.COLLOQUIAL_AREA, "colloquial_area");
    m.put(AddressComponentType.LOCALITY, "locality");
    m.put(AddressComponentType.WARD, "ward");
    m.put(AddressComponentType.SUBLOCALITY, "sublocality");
    m.put(AddressComponentType.SUBLOCALITY_LEVEL_1, "sublocality_level_1");
    m.put(AddressComponentType.SUBLOCALITY_LEVEL_2, "sublocality_level_2");
    m.put(AddressComponentType.SUBLOCALITY_LEVEL_3, "sublocality_level_3");
    m.put(AddressComponentType.SUBLOCALITY_LEVEL_4, "sublocality_level_4");
    m.put(AddressComponentType.SUBLOCALITY_LEVEL_5, "sublocality_level_5");
    m.put(AddressComponentType.NEIGHBORHOOD, "neighborhood");
    m.put(AddressComponentType.PREMISE, "premise");
    m.put(AddressComponentType.SUBPREMISE, "subpremise");
    m.put(AddressComponentType.POSTAL_CODE, "postal_code");
    m.put(AddressComponentType.POST_BOX, "post_box");
    m.put(AddressComponentType.POSTAL_CODE_PREFIX, "postal_code_prefix");
    m.put(AddressComponentType.POSTAL_CODE_SUFFIX, "postal_code_suffix");
    m.put(AddressComponentType.NATURAL_FEATURE, "natural_feature");
    m.put(AddressComponentType.AIRPORT, "airport");
    m.put(AddressComponentType.PARK, "park");
    m.put(AddressComponentType.FLOOR, "floor");
    m.put(AddressComponentType.PARKING, "parking");
    m.put(AddressComponentType.POINT_OF_INTEREST, "point_of_interest");
    m.put(AddressComponentType.BUS_STATION, "bus_station");
    m.put(AddressComponentType.TRAIN_STATION, "train_station");
    m.put(AddressComponentType.SUBWAY_STATION, "subway_station");
    m.put(AddressComponentType.TRANSIT_STATION, "transit_station");
    m.put(AddressComponentType.LIGHT_RAIL_STATION, "light_rail_station");
    m.put(AddressComponentType.ESTABLISHMENT, "establishment");
    m.put(AddressComponentType.POSTAL_TOWN, "postal_town");
    m.put(AddressComponentType.ROOM, "room");
    m.put(AddressComponentType.STREET_NUMBER, "street_number");
    m.put(AddressComponentType.GENERAL_CONTRACTOR, "general_contractor");
    m.put(AddressComponentType.FOOD, "food");
    m.put(AddressComponentType.REAL_ESTATE_AGENCY, "real_estate_agency");
    m.put(AddressComponentType.CAR_RENTAL, "car_rental");
    m.put(AddressComponentType.STORE, "store");
    m.put(AddressComponentType.SHOPPING_MALL, "shopping_mall");
    m.put(AddressComponentType.LODGING, "lodging");
    m.put(AddressComponentType.TRAVEL_AGENCY, "travel_agency");
    m.put(AddressComponentType.ELECTRONICS_STORE, "electronics_store");
    m.put(AddressComponentType.HOME_GOODS_STORE, "home_goods_store");
    m.put(AddressComponentType.SCHOOL, "school");
    m.put(AddressComponentType.ART_GALLERY, "art_gallery");
    m.put(AddressComponentType.LAWYER, "lawyer");
    m.put(AddressComponentType.RESTAURANT, "restaurant");
    m.put(AddressComponentType.BAR, "bar");
    m.put(AddressComponentType.MEAL_TAKEAWAY, "meal_takeaway");
    m.put(AddressComponentType.CLOTHING_STORE, "clothing_store");
    m.put(AddressComponentType.LOCAL_GOVERNMENT_OFFICE, "local_government_office");
    m.put(AddressComponentType.FINANCE, "finance");
    m.put(AddressComponentType.MOVING_COMPANY, "moving_company");
    m.put(AddressComponentType.STORAGE, "storage");
    m.put(AddressComponentType.CAFE, "cafe");
    m.put(AddressComponentType.CAR_REPAIR, "car_repair");
    m.put(AddressComponentType.HEALTH, "health");
    m.put(AddressComponentType.INSURANCE_AGENCY, "insurance_agency");
    m.put(AddressComponentType.PAINTER, "painter");

    for (Map.Entry<AddressComponentType, String> AddressComponentTypeLiteralPair :
        addressComponentTypeToLiteralMap.entrySet()) {
      assertEquals(
          AddressComponentTypeLiteralPair.getValue(),
          AddressComponentTypeLiteralPair.getKey().toCanonicalLiteral());
    }
    List<AddressComponentType> enumsMinusUnknown =
        new ArrayList<>(Arrays.asList(AddressComponentType.values()));
    enumsMinusUnknown.remove(AddressComponentType.UNKNOWN);
    List<AddressComponentType> onlyInTest =
        setdiff(addressComponentTypeToLiteralMap.keySet(), enumsMinusUnknown);
    List<AddressComponentType> onlyInEnum =
        setdiff(enumsMinusUnknown, addressComponentTypeToLiteralMap.keySet());
    assertEquals(
        "Unexpected enum elements: Only in test: " + onlyInTest + ". Only in enum: " + onlyInEnum,
        addressComponentTypeToLiteralMap.size() + 1, // 1 for unknown
        AddressComponentType.values().length);
  }

  private static <T extends UrlValue> void assertCannotGetUrlValue(T unknown) {
    assertNotNull(unknown);
    try {
      unknown.toUrlValue();
      fail("Expected to throw UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      // Expected.
    }
  }

  private static <T> List<T> setdiff(Collection<T> a, Collection<T> b) {
    List<T> out = new ArrayList<>(a);
    out.removeAll(b);
    return out;
  }
}
