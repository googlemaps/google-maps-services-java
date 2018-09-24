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

package com.google.maps;

import static com.google.maps.TestUtils.retrieveBody;
import static com.google.maps.model.ComponentFilter.administrativeArea;
import static com.google.maps.model.ComponentFilter.country;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.LocationType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(MediumTests.class)
public class GeocodingApiTest {

  private static final double EPSILON = 0.005;
  private static String simpleGeocodeResponse;
  private static String placeGeocodeResponse;
  private static String reverseGeocodeResponse;
  private static String simpleReverseGeocodeResponse;
  private static String utfResultGeocodeResponse;
  private static String reverseGeocodeWithKitaWardResponse;
  private static String geocodeLibraryType;

  public GeocodingApiTest() {
    simpleGeocodeResponse = retrieveBody("SimpleGeocodeResponse.json");
    placeGeocodeResponse = retrieveBody("PlaceGeocodeResponse.json");
    reverseGeocodeResponse = retrieveBody("ReverseGeocodeResponse.json");
    simpleReverseGeocodeResponse = retrieveBody("SimpleReverseGeocodeResponse.json");
    utfResultGeocodeResponse = retrieveBody("UtfResultGeocodeResponse.json");
    reverseGeocodeWithKitaWardResponse = retrieveBody("ReverseGeocodeWithKitaWardResponse.json");
    geocodeLibraryType = retrieveBody("GeocodeLibraryType.json");
  }

  @Test
  public void testGeocodeLibraryType() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(geocodeLibraryType)) {
      GeocodingResult[] results = GeocodingApi.newRequest(sc.context).address("80 FR").await();

      assertEquals(1, results.length);
      assertEquals(3, results[0].types.length);
      assertEquals(AddressType.ESTABLISHMENT, results[0].types[0]);
      assertEquals(AddressType.LIBRARY, results[0].types[1]);
      assertEquals(AddressType.POINT_OF_INTEREST, results[0].types[2]);
      assertNotNull(Arrays.toString(results));
    }
  }

  @Test
  public void testSimpleGeocode() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(simpleGeocodeResponse)) {
      GeocodingResult[] results = GeocodingApi.newRequest(sc.context).address("Sydney").await();
      checkSydneyResult(results);
      assertNotNull(Arrays.toString(results));

      sc.assertParamValue("Sydney", "address");
    }
  }

  @Test
  public void testPlaceGeocode() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(placeGeocodeResponse)) {
      String placeID = "ChIJP3Sa8ziYEmsRUKgyFmh9AQM";
      GeocodingResult[] results = GeocodingApi.newRequest(sc.context).place(placeID).await();
      checkSydneyResult(results);

      sc.assertParamValue(placeID, "place_id");
    }
  }

  @Test
  public void testAsync() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(simpleGeocodeResponse)) {
      final List<GeocodingResult[]> resps = new ArrayList<>();

      PendingResult.Callback<GeocodingResult[]> callback =
          new PendingResult.Callback<GeocodingResult[]>() {
            @Override
            public void onResult(GeocodingResult[] result) {
              resps.add(result);
            }

            @Override
            public void onFailure(Throwable e) {
              fail("Got error when expected success.");
            }
          };
      GeocodingApi.newRequest(sc.context).address("Sydney").setCallback(callback);

      Thread.sleep(2500);

      assertFalse(resps.isEmpty());
      assertNotNull(resps.get(0));
      checkSydneyResult(resps.get(0));

      sc.assertParamValue("Sydney", "address");
    }
  }

  private void checkSydneyResult(GeocodingResult[] results) {
    assertNotNull(results);
    assertNotNull(results[0].geometry);
    assertNotNull(results[0].geometry.location);
    assertEquals(-33.8674869, results[0].geometry.location.lat, EPSILON);
    assertEquals(151.2069902, results[0].geometry.location.lng, EPSILON);
    assertEquals(LocationType.APPROXIMATE, results[0].geometry.locationType);
  }

  @Test
  public void testReverseGeocode() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(reverseGeocodeResponse)) {
      LatLng latlng = new LatLng(-33.8674869, 151.2069902);
      GeocodingResult[] results = GeocodingApi.newRequest(sc.context).latlng(latlng).await();

      assertEquals(10, results.length);
      assertEquals("343 George St, Sydney NSW 2000, Australia", results[0].formattedAddress);
      assertEquals(
          "York St Near Barrack St, Sydney NSW 2017, Australia", results[1].formattedAddress);
      assertEquals("Sydney NSW 2000, Australia", results[2].formattedAddress);

      sc.assertParamValue(latlng.toUrlValue(), "latlng");
    }
  }

  /**
   * Simple geocode sample: <a
   * href="https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA">
   * Address Geocode for "1600 Amphitheatre Parkway, Mountain View, CA"</a>.
   */
  @Test
  public void testGeocodeTheGoogleplex() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"address_components\" : [\n"
                + "            {\n"
                + "               \"long_name\" : \"Google Building 41\",\n"
                + "               \"short_name\" : \"Google Bldg 41\",\n"
                + "               \"types\" : [ \"premise\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"1600\",\n"
                + "               \"short_name\" : \"1600\",\n"
                + "               \"types\" : [ \"street_number\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Amphitheatre Parkway\",\n"
                + "               \"short_name\" : \"Amphitheatre Pkwy\",\n"
                + "               \"types\" : [ \"route\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Mountain View\",\n"
                + "               \"short_name\" : \"Mountain View\",\n"
                + "               \"types\" : [ \"locality\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Santa Clara County\",\n"
                + "               \"short_name\" : \"Santa Clara County\",\n"
                + "               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"California\",\n"
                + "               \"short_name\" : \"CA\",\n"
                + "               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"United States\",\n"
                + "               \"short_name\" : \"US\",\n"
                + "               \"types\" : [ \"country\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"94043\",\n"
                + "               \"short_name\" : \"94043\",\n"
                + "               \"types\" : [ \"postal_code\" ]\n"
                + "            }\n"
                + "         ],\n"
                + "         \"formatted_address\" : \"Google Bldg 41, 1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\",\n"
                + "         \"geometry\" : {\n"
                + "            \"bounds\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 37.4228642,\n"
                + "                  \"lng\" : -122.0851557\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 37.4221145,\n"
                + "                  \"lng\" : -122.0859841\n"
                + "               }\n"
                + "            },\n"
                + "            \"location\" : {\n"
                + "               \"lat\" : 37.4224082,\n"
                + "               \"lng\" : -122.0856086\n"
                + "            },\n"
                + "            \"location_type\" : \"ROOFTOP\",\n"
                + "            \"viewport\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 37.4238383302915,\n"
                + "                  \"lng\" : -122.0842209197085\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 37.4211403697085,\n"
                + "                  \"lng\" : -122.0869188802915\n"
                + "               }\n"
                + "            }\n"
                + "         },\n"
                + "         \"place_id\" : \"ChIJxQvW8wK6j4AR3ukttGy3w2s\",\n"
                + "         \"types\" : [ \"premise\" ]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      String address = "1600 Amphitheatre Parkway, Mountain View, CA";
      GeocodingResult[] results = GeocodingApi.newRequest(sc.context).address(address).await();

      assertNotNull(results);
      assertNotNull(Arrays.toString(results));
      assertEquals(
          "Google Bldg 41, 1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA",
          results[0].formattedAddress);
      sc.assertParamValue(address, "address");
    }
  }

  /**
   * Address geocode with bounds: <a
   * href="https://maps.googleapis.com/maps/api/geocode/json?address=Winnetka&bounds=34.172684,-118.604794|34.236144,-118.500938">
   * Winnetka within (34.172684,-118.604794) - (34.236144,-118.500938)</a>.
   */
  @Test
  public void testGeocodeWithBounds() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"address_components\" : [\n"
                + "            {\n"
                + "               \"long_name\" : \"Winnetka\",\n"
                + "               \"short_name\" : \"Winnetka\",\n"
                + "               \"types\" : [ \"neighborhood\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Los Angeles\",\n"
                + "               \"short_name\" : \"Los Angeles\",\n"
                + "               \"types\" : [ \"locality\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Los Angeles County\",\n"
                + "               \"short_name\" : \"Los Angeles County\",\n"
                + "               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"California\",\n"
                + "               \"short_name\" : \"CA\",\n"
                + "               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"United States\",\n"
                + "               \"short_name\" : \"US\",\n"
                + "               \"types\" : [ \"country\", \"political\" ]\n"
                + "            }\n"
                + "         ],\n"
                + "         \"formatted_address\" : \"Winnetka, Los Angeles, CA, USA\",\n"
                + "         \"geometry\" : {\n"
                + "            \"bounds\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 34.2355209,\n"
                + "                  \"lng\" : -118.5534191\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 34.1854649,\n"
                + "                  \"lng\" : -118.588536\n"
                + "               }\n"
                + "            },\n"
                + "            \"location\" : {\n"
                + "               \"lat\" : 34.2048586,\n"
                + "               \"lng\" : -118.5739621\n"
                + "            },\n"
                + "            \"location_type\" : \"APPROXIMATE\",\n"
                + "            \"viewport\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 34.2355209,\n"
                + "                  \"lng\" : -118.5534191\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 34.1854649,\n"
                + "                  \"lng\" : -118.588536\n"
                + "               }\n"
                + "            }\n"
                + "         },\n"
                + "         \"place_id\" : \"ChIJ0fd4S_KbwoAR2hRDrsr3HmQ\",\n"
                + "         \"types\" : [ \"neighborhood\", \"political\" ]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      GeocodingResult[] results =
          GeocodingApi.newRequest(sc.context)
              .address("Winnetka")
              .bounds(new LatLng(34.172684, -118.604794), new LatLng(34.236144, -118.500938))
              .await();

      assertNotNull(Arrays.toString(results));

      assertEquals("Winnetka, Los Angeles, CA, USA", results[0].formattedAddress);
      assertEquals("ChIJ0fd4S_KbwoAR2hRDrsr3HmQ", results[0].placeId);

      sc.assertParamValue("Winnetka", "address");
      sc.assertParamValue("34.17268400,-118.60479400|34.23614400,-118.50093800", "bounds");
    }
  }

  /**
   * Geocode with region biasing: <a
   * href="https://maps.googleapis.com/maps/api/geocode/json?address=Toledo&region=es">Geocode for
   * Toledo in Spain</a>.
   */
  @Test
  public void testGeocodeWithRegionBiasing() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"address_components\" : [\n"
                + "            {\n"
                + "               \"long_name\" : \"Toledo\",\n"
                + "               \"short_name\" : \"Toledo\",\n"
                + "               \"types\" : [ \"locality\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Toledo\",\n"
                + "               \"short_name\" : \"TO\",\n"
                + "               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Castile-La Mancha\",\n"
                + "               \"short_name\" : \"CM\",\n"
                + "               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Spain\",\n"
                + "               \"short_name\" : \"ES\",\n"
                + "               \"types\" : [ \"country\", \"political\" ]\n"
                + "            }\n"
                + "         ],\n"
                + "         \"formatted_address\" : \"Toledo, Spain\",\n"
                + "         \"geometry\" : {\n"
                + "            \"bounds\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 39.88605099999999,\n"
                + "                  \"lng\" : -3.9192423\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 39.8383676,\n"
                + "                  \"lng\" : -4.0796176\n"
                + "               }\n"
                + "            },\n"
                + "            \"location\" : {\n"
                + "               \"lat\" : 39.8628316,\n"
                + "               \"lng\" : -4.027323099999999\n"
                + "            },\n"
                + "            \"location_type\" : \"APPROXIMATE\",\n"
                + "            \"viewport\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 39.88605099999999,\n"
                + "                  \"lng\" : -3.9192423\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 39.8383676,\n"
                + "                  \"lng\" : -4.0796176\n"
                + "               }\n"
                + "            }\n"
                + "         },\n"
                + "         \"place_id\" : \"ChIJ8f21C60Lag0R_q11auhbf8Y\",\n"
                + "         \"types\" : [ \"locality\", \"political\" ]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      GeocodingResult[] results =
          GeocodingApi.newRequest(sc.context).address("Toledo").region("es").await();

      assertNotNull(Arrays.toString(results));

      assertNotNull(results);
      assertEquals("Toledo, Spain", results[0].formattedAddress);
      assertEquals(AddressType.LOCALITY, results[0].types[0]);
      assertEquals(AddressType.POLITICAL, results[0].types[1]);

      sc.assertParamValue("Toledo", "address");
      sc.assertParamValue("es", "region");
    }
  }

  /**
   * Geocode with component filtering: <a
   * href="https://maps.googleapis.com/maps/api/geocode/json?address=santa+cruz&components=country:ES">
   * Geocoding "santa cruz" with country set to ES</a>.
   */
  @Test
  public void testGeocodeWithComponentFilter() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"address_components\" : [\n"
                + "            {\n"
                + "               \"long_name\" : \"Santa Cruz de Tenerife\",\n"
                + "               \"short_name\" : \"Santa Cruz de Tenerife\",\n"
                + "               \"types\" : [ \"locality\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Santa Cruz de Tenerife\",\n"
                + "               \"short_name\" : \"TF\",\n"
                + "               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Canary Islands\",\n"
                + "               \"short_name\" : \"CN\",\n"
                + "               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Spain\",\n"
                + "               \"short_name\" : \"ES\",\n"
                + "               \"types\" : [ \"country\", \"political\" ]\n"
                + "            }\n"
                + "         ],\n"
                + "         \"formatted_address\" : \"Santa Cruz de Tenerife, Spain\",\n"
                + "         \"geometry\" : {\n"
                + "            \"bounds\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 28.487616,\n"
                + "                  \"lng\" : -16.2356646\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 28.4280248,\n"
                + "                  \"lng\" : -16.3370045\n"
                + "               }\n"
                + "            },\n"
                + "            \"location\" : {\n"
                + "               \"lat\" : 28.4636296,\n"
                + "               \"lng\" : -16.2518467\n"
                + "            },\n"
                + "            \"location_type\" : \"APPROXIMATE\",\n"
                + "            \"viewport\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 28.487616,\n"
                + "                  \"lng\" : -16.2356646\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 28.4280248,\n"
                + "                  \"lng\" : -16.3370045\n"
                + "               }\n"
                + "            }\n"
                + "         },\n"
                + "         \"place_id\" : \"ChIJcUElzOzMQQwRLuV30nMUEUM\",\n"
                + "         \"types\" : [ \"locality\", \"political\" ]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      GeocodingResult[] results =
          GeocodingApi.newRequest(sc.context)
              .address("santa cruz")
              .components(ComponentFilter.country("ES"))
              .await();

      assertNotNull(Arrays.toString(results));

      assertEquals("Santa Cruz de Tenerife, Spain", results[0].formattedAddress);
      assertEquals("ChIJcUElzOzMQQwRLuV30nMUEUM", results[0].placeId);

      sc.assertParamValue("country:ES", "components");
      sc.assertParamValue("santa cruz", "address");
    }
  }

  /**
   * Geocode with multiple component filters: <a
   * href="https://maps.googleapis.com/maps/api/geocode/json?address=Torun&components=administrative_area:TX|country:US">
   * Geocoding Torun, with administrative area of "TX" and country of "US"</a>.
   */
  @Test
  public void testGeocodeWithMultipleComponentFilters() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"address_components\" : [\n"
                + "            {\n"
                + "               \"long_name\" : \"Texas\",\n"
                + "               \"short_name\" : \"TX\",\n"
                + "               \"types\" : [\n"
                + "                  \"administrative_area_level_1\",\n"
                + "                  \"establishment\",\n"
                + "                  \"point_of_interest\",\n"
                + "                  \"political\"\n"
                + "               ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"United States\",\n"
                + "               \"short_name\" : \"US\",\n"
                + "               \"types\" : [ \"country\", \"political\" ]\n"
                + "            }\n"
                + "         ],\n"
                + "         \"formatted_address\" : \"Texas, USA\",\n"
                + "         \"geometry\" : {\n"
                + "            \"bounds\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 36.5007041,\n"
                + "                  \"lng\" : -93.5080389\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 25.8371638,\n"
                + "                  \"lng\" : -106.6456461\n"
                + "               }\n"
                + "            },\n"
                + "            \"location\" : {\n"
                + "               \"lat\" : 31.9685988,\n"
                + "               \"lng\" : -99.9018131\n"
                + "            },\n"
                + "            \"location_type\" : \"APPROXIMATE\",\n"
                + "            \"viewport\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 36.5018864,\n"
                + "                  \"lng\" : -93.5080389\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 25.83819,\n"
                + "                  \"lng\" : -106.6452951\n"
                + "               }\n"
                + "            }\n"
                + "         },\n"
                + "         \"partial_match\" : true,\n"
                + "         \"place_id\" : \"ChIJSTKCCzZwQIYRPN4IGI8c6xY\",\n"
                + "         \"types\" : [\n"
                + "            \"administrative_area_level_1\",\n"
                + "            \"establishment\",\n"
                + "            \"point_of_interest\",\n"
                + "            \"political\"\n"
                + "         ]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      GeocodingResult[] results =
          GeocodingApi.newRequest(sc.context)
              .address("Torun")
              .components(administrativeArea("TX"), country("US"))
              .await();

      assertNotNull(Arrays.toString(results));

      assertEquals("Texas, USA", results[0].formattedAddress);
      assertEquals(true, results[0].partialMatch);
      assertEquals("ChIJSTKCCzZwQIYRPN4IGI8c6xY", results[0].placeId);

      sc.assertParamValue("administrative_area:TX|country:US", "components");
      sc.assertParamValue("Torun", "address");
    }
  }

  /**
   * Making a request using just components filter: <a
   * href="https://maps.googleapis.com/maps/api/geocode/json?components=route:Annegatan|administrative_area:Helsinki|country:Finland">
   * Searching for a route of Annegatan, in the administrative area of Helsinki, and the country of
   * Finland </a>.
   */
  @Test
  public void testGeocodeWithJustComponents() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"address_components\" : [\n"
                + "            {\n"
                + "               \"long_name\" : \"Annankatu\",\n"
                + "               \"short_name\" : \"Annankatu\",\n"
                + "               \"types\" : [ \"route\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Helsinki\",\n"
                + "               \"short_name\" : \"HKI\",\n"
                + "               \"types\" : [ \"locality\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Finland\",\n"
                + "               \"short_name\" : \"FI\",\n"
                + "               \"types\" : [ \"country\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"00101\",\n"
                + "               \"short_name\" : \"00101\",\n"
                + "               \"types\" : [ \"postal_code\" ]\n"
                + "            }\n"
                + "         ],\n"
                + "         \"formatted_address\" : \"Annankatu, 00101 Helsinki, Finland\",\n"
                + "         \"geometry\" : {\n"
                + "            \"bounds\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 60.168997,\n"
                + "                  \"lng\" : 24.9433353\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 60.16226160000001,\n"
                + "                  \"lng\" : 24.9332897\n"
                + "               }\n"
                + "            },\n"
                + "            \"location\" : {\n"
                + "               \"lat\" : 60.1657808,\n"
                + "               \"lng\" : 24.938451\n"
                + "            },\n"
                + "            \"location_type\" : \"GEOMETRIC_CENTER\",\n"
                + "            \"viewport\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 60.168997,\n"
                + "                  \"lng\" : 24.9433353\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 60.16226160000001,\n"
                + "                  \"lng\" : 24.9332897\n"
                + "               }\n"
                + "            }\n"
                + "         },\n"
                + "         \"place_id\" : \"EiBBbm5hbmthdHUsIDAwMTAxIEhlbHNpbmtpLCBTdW9taQ\",\n"
                + "         \"types\" : [ \"route\" ]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      GeocodingResult[] results =
          GeocodingApi.newRequest(sc.context)
              .components(
                  ComponentFilter.route("Annegatan"),
                  ComponentFilter.administrativeArea("Helsinki"),
                  ComponentFilter.country("Finland"))
              .await();

      assertNotNull(results);
      assertNotNull(Arrays.toString(results));
      assertEquals("Annankatu, 00101 Helsinki, Finland", results[0].formattedAddress);
      assertEquals("EiBBbm5hbmthdHUsIDAwMTAxIEhlbHNpbmtpLCBTdW9taQ", results[0].placeId);

      sc.assertParamValue(
          "route:Annegatan|administrative_area:Helsinki|country:Finland", "components");
    }
  }

  /**
   * Simple reverse geocoding. <a
   * href="https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452">Reverse
   * geocode (40.714224,-73.961452)</a>.
   */
  @Test
  public void testSimpleReverseGeocode() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(simpleReverseGeocodeResponse)) {
      LatLng latlng = new LatLng(40.714224, -73.961452);
      GeocodingResult[] results = GeocodingApi.newRequest(sc.context).latlng(latlng).await();

      assertNotNull(results);
      assertNotNull(Arrays.toString(results));
      assertEquals("277 Bedford Ave, Brooklyn, NY 11211, USA", results[0].formattedAddress);
      assertEquals("277", results[0].addressComponents[0].longName);
      assertEquals("277", results[0].addressComponents[0].shortName);
      assertEquals(AddressComponentType.STREET_NUMBER, results[0].addressComponents[0].types[0]);
      assertEquals(AddressType.STREET_ADDRESS, results[0].types[0]);

      sc.assertParamValue(latlng.toUrlValue(), "latlng");
    }
  }

  /**
   * Reverse geocode restricted by type: <a
   * href="https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&location_type=ROOFTOP&result_type=street_address">
   * Reverse Geocode (40.714224,-73.961452) with location type of ROOFTOP and result type of
   * street_address</a>.
   */
  @Test
  public void testReverseGeocodeRestrictedByType() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"address_components\" : [\n"
                + "            {\n"
                + "               \"long_name\" : \"277\",\n"
                + "               \"short_name\" : \"277\",\n"
                + "               \"types\" : [ \"street_number\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Bedford Avenue\",\n"
                + "               \"short_name\" : \"Bedford Ave\",\n"
                + "               \"types\" : [ \"route\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Williamsburg\",\n"
                + "               \"short_name\" : \"Williamsburg\",\n"
                + "               \"types\" : [ \"neighborhood\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Brooklyn\",\n"
                + "               \"short_name\" : \"Brooklyn\",\n"
                + "               \"types\" : [ \"political\", \"sublocality\", \"sublocality_level_1\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Kings County\",\n"
                + "               \"short_name\" : \"Kings County\",\n"
                + "               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"New York\",\n"
                + "               \"short_name\" : \"NY\",\n"
                + "               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"United States\",\n"
                + "               \"short_name\" : \"US\",\n"
                + "               \"types\" : [ \"country\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"11211\",\n"
                + "               \"short_name\" : \"11211\",\n"
                + "               \"types\" : [ \"postal_code\" ]\n"
                + "            }\n"
                + "         ],\n"
                + "         \"formatted_address\" : \"277 Bedford Ave, Brooklyn, NY 11211, USA\",\n"
                + "         \"geometry\" : {\n"
                + "            \"location\" : {\n"
                + "               \"lat\" : 40.7142205,\n"
                + "               \"lng\" : -73.9612903\n"
                + "            },\n"
                + "            \"location_type\" : \"ROOFTOP\",\n"
                + "            \"viewport\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 40.71556948029149,\n"
                + "                  \"lng\" : -73.95994131970849\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 40.7128715197085,\n"
                + "                  \"lng\" : -73.9626392802915\n"
                + "               }\n"
                + "            }\n"
                + "         },\n"
                + "         \"place_id\" : \"ChIJd8BlQ2BZwokRAFUEcm_qrcA\",\n"
                + "         \"types\" : [ \"street_address\" ]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      LatLng latlng = new LatLng(40.714224, -73.961452);
      GeocodingResult[] results =
          GeocodingApi.newRequest(sc.context)
              .latlng(latlng)
              .locationType(LocationType.ROOFTOP)
              .resultType(AddressType.STREET_ADDRESS)
              .await();

      assertNotNull(results);
      assertNotNull(Arrays.toString(results));
      assertEquals("277 Bedford Ave, Brooklyn, NY 11211, USA", results[0].formattedAddress);
      assertEquals(LocationType.ROOFTOP, results[0].geometry.locationType);
      assertEquals("ChIJd8BlQ2BZwokRAFUEcm_qrcA", results[0].placeId);

      sc.assertParamValue(latlng.toUrlValue(), "latlng");
      sc.assertParamValue(LocationType.ROOFTOP.toUrlValue(), "location_type");
      sc.assertParamValue(AddressType.STREET_ADDRESS.toUrlValue(), "result_type");
    }
  }

  /** Testing UTF8 result parsing. */
  @Test
  public void testUtfResult() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(utfResultGeocodeResponse)) {
      LatLng location = new LatLng(46.8023388, 1.6551867);
      GeocodingResult[] results = GeocodingApi.newRequest(sc.context).latlng(location).await();
      assertEquals("1 Rue Fernand Raynaud, 36000 Châteauroux, France", results[0].formattedAddress);
      sc.assertParamValue(location.toUrlValue(), "latlng");
    }
  }

  /**
   * Testing custom parameter pass through.
   *
   * <p>See <a
   * href="https://googlegeodevelopers.blogspot.com.au/2016/11/address-geocoding-in-google-maps-apis.html">
   * Address Geocoding in the Google Maps APIs</a> for the reasoning behind this usage.
   */
  @Test
  public void testCustomParameterPassThrough() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"address_components\" : [\n"
                + "            {\n"
                + "               \"long_name\" : \"Google Building 41\",\n"
                + "               \"short_name\" : \"Google Bldg 41\",\n"
                + "               \"types\" : [ \"premise\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"1600\",\n"
                + "               \"short_name\" : \"1600\",\n"
                + "               \"types\" : [ \"street_number\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Amphitheatre Parkway\",\n"
                + "               \"short_name\" : \"Amphitheatre Pkwy\",\n"
                + "               \"types\" : [ \"route\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Mountain View\",\n"
                + "               \"short_name\" : \"Mountain View\",\n"
                + "               \"types\" : [ \"locality\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Santa Clara County\",\n"
                + "               \"short_name\" : \"Santa Clara County\",\n"
                + "               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"California\",\n"
                + "               \"short_name\" : \"CA\",\n"
                + "               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"United States\",\n"
                + "               \"short_name\" : \"US\",\n"
                + "               \"types\" : [ \"country\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"94043\",\n"
                + "               \"short_name\" : \"94043\",\n"
                + "               \"types\" : [ \"postal_code\" ]\n"
                + "            }\n"
                + "         ],\n"
                + "         \"formatted_address\" : \"Google Bldg 41, 1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\",\n"
                + "         \"geometry\" : {\n"
                + "            \"bounds\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 37.4228642,\n"
                + "                  \"lng\" : -122.0851557\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 37.4221145,\n"
                + "                  \"lng\" : -122.0859841\n"
                + "               }\n"
                + "            },\n"
                + "            \"location\" : {\n"
                + "               \"lat\" : 37.4224082,\n"
                + "               \"lng\" : -122.0856086\n"
                + "            },\n"
                + "            \"location_type\" : \"ROOFTOP\",\n"
                + "            \"viewport\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 37.4238383302915,\n"
                + "                  \"lng\" : -122.0842209197085\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 37.4211403697085,\n"
                + "                  \"lng\" : -122.0869188802915\n"
                + "               }\n"
                + "            }\n"
                + "         },\n"
                + "         \"place_id\" : \"ChIJxQvW8wK6j4AR3ukttGy3w2s\",\n"
                + "         \"types\" : [ \"premise\" ]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      String address = "1600 Amphitheatre Parkway, Mountain View, CA";
      GeocodingResult[] results =
          GeocodingApi.newRequest(sc.context)
              .address(address)
              .custom("new_forward_geocoder", "true")
              .await();

      assertNotNull(Arrays.toString(results));
      assertEquals(
          "Google Bldg 41, 1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA",
          results[0].formattedAddress);

      sc.assertParamValue(address, "address");
      sc.assertParamValue("true", "new_forward_geocoder");
    }
  }

  /** Testing Kita Ward reverse geocode. */
  @Test
  public void testReverseGeocodeWithKitaWard() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(reverseGeocodeWithKitaWardResponse)) {
      LatLng location = new LatLng(35.03937, 135.729243);
      GeocodingResult[] results = GeocodingApi.newRequest(sc.context).latlng(location).await();

      assertNotNull(results);
      assertNotNull(Arrays.toString(results));
      assertEquals(
          "Japan, 〒603-8361 Kyōto-fu, Kyōto-shi, Kita-ku, Kinkakujichō, １ 北山鹿苑寺金閣寺",
          results[0].formattedAddress);
      assertEquals("Kita Ward", results[3].addressComponents[0].shortName);
      assertEquals("Kita Ward", results[3].addressComponents[0].longName);
      assertEquals(AddressComponentType.LOCALITY, results[3].addressComponents[0].types[0]);
      assertEquals(AddressComponentType.POLITICAL, results[3].addressComponents[0].types[1]);
      assertEquals(AddressComponentType.WARD, results[3].addressComponents[0].types[2]);

      sc.assertParamValue(location.toUrlValue(), "latlng");
    }
  }

  /** Testing supported Address Types for Geocoding. */
  @Test
  public void testSupportedAddressTypesFood() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"address_components\" : [\n"
                + "            {\n"
                + "               \"long_name\" : \"21800\",\n"
                + "               \"short_name\" : \"21800\",\n"
                + "               \"types\" : [ \"street_number\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"West Eleven Mile Road\",\n"
                + "               \"short_name\" : \"W Eleven Mile Rd\",\n"
                + "               \"types\" : [ \"route\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Southfield\",\n"
                + "               \"short_name\" : \"Southfield\",\n"
                + "               \"types\" : [ \"locality\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Oakland County\",\n"
                + "               \"short_name\" : \"Oakland County\",\n"
                + "               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Michigan\",\n"
                + "               \"short_name\" : \"MI\",\n"
                + "               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"United States\",\n"
                + "               \"short_name\" : \"US\",\n"
                + "               \"types\" : [ \"country\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"48076\",\n"
                + "               \"short_name\" : \"48076\",\n"
                + "               \"types\" : [ \"postal_code\" ]\n"
                + "            }\n"
                + "         ],\n"
                + "         \"formatted_address\" : \"21800 W Eleven Mile Rd, Southfield, MI 48076, USA\",\n"
                + "         \"geometry\" : {\n"
                + "            \"location\" : {\n"
                + "               \"lat\" : 42.4879618,\n"
                + "               \"lng\" : -83.2595228\n"
                + "            },\n"
                + "            \"location_type\" : \"ROOFTOP\",\n"
                + "            \"viewport\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 42.4893107802915,\n"
                + "                  \"lng\" : -83.25817381970849\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 42.4866128197085,\n"
                + "                  \"lng\" : -83.26087178029151\n"
                + "               }\n"
                + "            }\n"
                + "         },\n"
                + "         \"place_id\" : \"ChIJ6zOD5dy3JIgRgsMEeGna5dI\",\n"
                + "         \"types\" : [\n"
                + "            \"establishment\",\n"
                + "            \"food\",\n"
                + "            \"grocery_or_supermarket\",\n"
                + "            \"point_of_interest\",\n"
                + "            \"store\"\n"
                + "         ]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      String address = "Noah's Marketplace, 21800 W Eleven Mile Rd";
      GeocodingResult[] results = GeocodingApi.newRequest(sc.context).address(address).await();

      assertNotNull(results);
      assertNotNull(Arrays.toString(results));
      assertEquals(AddressType.ESTABLISHMENT, results[0].types[0]);
      assertEquals(AddressType.FOOD, results[0].types[1]);
      assertEquals(AddressType.GROCERY_OR_SUPERMARKET, results[0].types[2]);
      assertEquals(AddressType.POINT_OF_INTEREST, results[0].types[3]);
      assertEquals(AddressType.STORE, results[0].types[4]);

      sc.assertParamValue(address, "address");
    }
  }

  /** Testing supported Address Types for Geocoding - Synagogue. */
  @Test
  public void testSupportedAddressTypesSynagogue() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"address_components\" : [\n"
                + "            {\n"
                + "               \"long_name\" : \"15620\",\n"
                + "               \"short_name\" : \"15620\",\n"
                + "               \"types\" : [ \"street_number\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"West 10 Mile Road\",\n"
                + "               \"short_name\" : \"W 10 Mile Rd\",\n"
                + "               \"types\" : [ \"route\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Southfield\",\n"
                + "               \"short_name\" : \"Southfield\",\n"
                + "               \"types\" : [ \"locality\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Oakland County\",\n"
                + "               \"short_name\" : \"Oakland County\",\n"
                + "               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"Michigan\",\n"
                + "               \"short_name\" : \"MI\",\n"
                + "               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"United States\",\n"
                + "               \"short_name\" : \"US\",\n"
                + "               \"types\" : [ \"country\", \"political\" ]\n"
                + "            },\n"
                + "            {\n"
                + "               \"long_name\" : \"48075\",\n"
                + "               \"short_name\" : \"48075\",\n"
                + "               \"types\" : [ \"postal_code\" ]\n"
                + "            }\n"
                + "         ],\n"
                + "         \"formatted_address\" : \"15620 W 10 Mile Rd, Southfield, MI 48075, USA\",\n"
                + "         \"geometry\" : {\n"
                + "            \"location\" : {\n"
                + "               \"lat\" : 42.4742111,\n"
                + "               \"lng\" : -83.2046522\n"
                + "            },\n"
                + "            \"location_type\" : \"ROOFTOP\",\n"
                + "            \"viewport\" : {\n"
                + "               \"northeast\" : {\n"
                + "                  \"lat\" : 42.4755600802915,\n"
                + "                  \"lng\" : -83.20330321970849\n"
                + "               },\n"
                + "               \"southwest\" : {\n"
                + "                  \"lat\" : 42.4728621197085,\n"
                + "                  \"lng\" : -83.20600118029151\n"
                + "               }\n"
                + "            }\n"
                + "         },\n"
                + "         \"place_id\" : \"ChIJn5hABPnIJIgRr_d3wqujFS0\",\n"
                + "         \"types\" : [ \"establishment\", \"place_of_worship\", \"point_of_interest\", \"synagogue\" ]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      String address = "Ahavas Olam, 15620 W. Ten Mile Road";
      GeocodingResult[] results = GeocodingApi.newRequest(sc.context).address(address).await();

      assertNotNull(results);
      assertNotNull(Arrays.toString(results));
      assertEquals(AddressType.ESTABLISHMENT, results[0].types[0]);
      assertEquals(AddressType.PLACE_OF_WORSHIP, results[0].types[1]);
      assertEquals(AddressType.POINT_OF_INTEREST, results[0].types[2]);
      assertEquals(AddressType.SYNAGOGUE, results[0].types[3]);

      sc.assertParamValue(address, "address");
    }
  }
}
