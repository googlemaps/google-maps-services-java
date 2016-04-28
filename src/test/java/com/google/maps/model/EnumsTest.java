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

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;
import java.util.Map;

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
    addressTypeToLiteralMap.put(AddressType.STREET_ADDRESS, "street_address");
    addressTypeToLiteralMap.put(AddressType.ROUTE, "route");
    addressTypeToLiteralMap.put(AddressType.INTERSECTION, "intersection");
    addressTypeToLiteralMap.put(AddressType.POLITICAL, "political");
    addressTypeToLiteralMap.put(AddressType.COUNTRY, "country");
    addressTypeToLiteralMap.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_1,
                                "administrative_area_level_1");
    addressTypeToLiteralMap.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_2,
                                "administrative_area_level_2");
    addressTypeToLiteralMap.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_3,
                                "administrative_area_level_3");
    addressTypeToLiteralMap.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_4,
                                "administrative_area_level_4");
    addressTypeToLiteralMap.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_5,
                                "administrative_area_level_5");
    addressTypeToLiteralMap.put(AddressType.COLLOQUIAL_AREA, "colloquial_area");
    addressTypeToLiteralMap.put(AddressType.LOCALITY, "locality");
    addressTypeToLiteralMap.put(AddressType.WARD, "ward");
    addressTypeToLiteralMap.put(AddressType.SUBLOCALITY, "sublocality");
    addressTypeToLiteralMap.put(AddressType.SUBLOCALITY_LEVEL_1, "sublocality_level_1");
    addressTypeToLiteralMap.put(AddressType.SUBLOCALITY_LEVEL_2, "sublocality_level_2");
    addressTypeToLiteralMap.put(AddressType.SUBLOCALITY_LEVEL_3, "sublocality_level_3");
    addressTypeToLiteralMap.put(AddressType.SUBLOCALITY_LEVEL_4, "sublocality_level_4");
    addressTypeToLiteralMap.put(AddressType.SUBLOCALITY_LEVEL_5, "sublocality_level_5");
    addressTypeToLiteralMap.put(AddressType.NEIGHBORHOOD, "neighborhood");
    addressTypeToLiteralMap.put(AddressType.PREMISE, "premise");
    addressTypeToLiteralMap.put(AddressType.SUBPREMISE, "subpremise");
    addressTypeToLiteralMap.put(AddressType.POSTAL_CODE, "postal_code");
    addressTypeToLiteralMap.put(AddressType.NATURAL_FEATURE, "natural_feature");
    addressTypeToLiteralMap.put(AddressType.AIRPORT, "airport");
    addressTypeToLiteralMap.put(AddressType.PARK, "park");
    addressTypeToLiteralMap.put(AddressType.POINT_OF_INTEREST, "point_of_interest");
    addressTypeToLiteralMap.put(AddressType.POST_OFFICE, "post_office");
    addressTypeToLiteralMap.put(AddressType.PLACE_OF_WORSHIP, "place_of_worship");
    addressTypeToLiteralMap.put(AddressType.BUS_STATION, "bus_station");
    addressTypeToLiteralMap.put(AddressType.TRAIN_STATION, "train_station");
    addressTypeToLiteralMap.put(AddressType.SUBWAY_STATION, "subway_station");
    addressTypeToLiteralMap.put(AddressType.TRANSIT_STATION, "transit_station");
    addressTypeToLiteralMap.put(AddressType.CHURCH, "church");
    addressTypeToLiteralMap.put(AddressType.FINANCE, "finance");
    addressTypeToLiteralMap.put(AddressType.ESTABLISHMENT, "establishment");
    addressTypeToLiteralMap.put(AddressType.POSTAL_TOWN, "postal_town");
    addressTypeToLiteralMap.put(AddressType.UNIVERSITY, "university");

    for (Map.Entry<AddressType, String> addressTypeLiteralPair :
        addressTypeToLiteralMap.entrySet()) {
      assertEquals(addressTypeLiteralPair.getValue(),
                   addressTypeLiteralPair.getKey().toCanonicalLiteral());
    }
    assertEquals(addressTypeToLiteralMap.size() + 1, // 1 for unknown
                 AddressType.values().length);
  }

  @Test
  public void testCanonicalLiteralsForAddressComponentType() {
    Map<AddressComponentType, String> addressComponentTypeToLiteralMap =
        new HashMap<AddressComponentType, String>();
    addressComponentTypeToLiteralMap.put(AddressComponentType.STREET_ADDRESS, "street_address");
    addressComponentTypeToLiteralMap.put(AddressComponentType.ROUTE, "route");
    addressComponentTypeToLiteralMap.put(AddressComponentType.INTERSECTION, "intersection");
    addressComponentTypeToLiteralMap.put(AddressComponentType.POLITICAL, "political");
    addressComponentTypeToLiteralMap.put(AddressComponentType.COUNTRY, "country");
    addressComponentTypeToLiteralMap.put(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1,
                                "administrative_area_level_1");
    addressComponentTypeToLiteralMap.put(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2,
                                "administrative_area_level_2");
    addressComponentTypeToLiteralMap.put(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_3,
                                "administrative_area_level_3");
    addressComponentTypeToLiteralMap.put(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_4,
                                "administrative_area_level_4");
    addressComponentTypeToLiteralMap.put(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_5,
                                "administrative_area_level_5");
    addressComponentTypeToLiteralMap.put(AddressComponentType.COLLOQUIAL_AREA, "colloquial_area");
    addressComponentTypeToLiteralMap.put(AddressComponentType.LOCALITY, "locality");
    addressComponentTypeToLiteralMap.put(AddressComponentType.WARD, "ward");
    addressComponentTypeToLiteralMap.put(AddressComponentType.SUBLOCALITY, "sublocality");
    addressComponentTypeToLiteralMap.put(AddressComponentType.SUBLOCALITY_LEVEL_1,
                                         "sublocality_level_1");
    addressComponentTypeToLiteralMap.put(AddressComponentType.SUBLOCALITY_LEVEL_2,
                                         "sublocality_level_2");
    addressComponentTypeToLiteralMap.put(AddressComponentType.SUBLOCALITY_LEVEL_3,
                                         "sublocality_level_3");
    addressComponentTypeToLiteralMap.put(AddressComponentType.SUBLOCALITY_LEVEL_4,
                                         "sublocality_level_4");
    addressComponentTypeToLiteralMap.put(AddressComponentType.SUBLOCALITY_LEVEL_5,
                                         "sublocality_level_5");
    addressComponentTypeToLiteralMap.put(AddressComponentType.NEIGHBORHOOD, "neighborhood");
    addressComponentTypeToLiteralMap.put(AddressComponentType.PREMISE, "premise");
    addressComponentTypeToLiteralMap.put(AddressComponentType.SUBPREMISE, "subpremise");
    addressComponentTypeToLiteralMap.put(AddressComponentType.POSTAL_CODE, "postal_code");
    addressComponentTypeToLiteralMap.put(AddressComponentType.POST_BOX, "post_box");
    addressComponentTypeToLiteralMap.put(AddressComponentType.POSTAL_CODE_PREFIX,
                                         "postal_code_prefix");
    addressComponentTypeToLiteralMap.put(AddressComponentType.POSTAL_CODE_SUFFIX,
                                         "postal_code_suffix");
    addressComponentTypeToLiteralMap.put(AddressComponentType.NATURAL_FEATURE, "natural_feature");
    addressComponentTypeToLiteralMap.put(AddressComponentType.AIRPORT, "airport");
    addressComponentTypeToLiteralMap.put(AddressComponentType.PARK, "park");
    addressComponentTypeToLiteralMap.put(AddressComponentType.FLOOR, "floor");
    addressComponentTypeToLiteralMap.put(AddressComponentType.PARKING, "parking");
    addressComponentTypeToLiteralMap.put(AddressComponentType.POINT_OF_INTEREST,
                                         "point_of_interest");
    addressComponentTypeToLiteralMap.put(AddressComponentType.BUS_STATION, "bus_station");
    addressComponentTypeToLiteralMap.put(AddressComponentType.TRAIN_STATION, "train_station");
    addressComponentTypeToLiteralMap.put(AddressComponentType.SUBWAY_STATION, "subway_station");
    addressComponentTypeToLiteralMap.put(AddressComponentType.TRANSIT_STATION, "transit_station");
    addressComponentTypeToLiteralMap.put(AddressComponentType.ESTABLISHMENT, "establishment");
    addressComponentTypeToLiteralMap.put(AddressComponentType.POSTAL_TOWN, "postal_town");
    addressComponentTypeToLiteralMap.put(AddressComponentType.ROOM, "room");
    addressComponentTypeToLiteralMap.put(AddressComponentType.STREET_NUMBER, "street_number");

    for (Map.Entry<AddressComponentType, String> AddressComponentTypeLiteralPair :
        addressComponentTypeToLiteralMap.entrySet()) {
      assertEquals(AddressComponentTypeLiteralPair.getValue(),
                   AddressComponentTypeLiteralPair.getKey().toCanonicalLiteral());
    }
    assertEquals(addressComponentTypeToLiteralMap.size() + 1, // 1 for unknown
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
}
