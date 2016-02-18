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

import static com.google.maps.model.ComponentFilter.administrativeArea;
import static com.google.maps.model.ComponentFilter.country;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.LocationType;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Category(LargeTests.class)
public class GeocodingApiTest extends AuthenticatedTest {

  public static final double EPSILON = 0.000001;

  private GeoApiContext context;

  public GeocodingApiTest(GeoApiContext context) {
    this.context = context
        .setQueryRateLimit(3)
        .setConnectTimeout(1, TimeUnit.SECONDS)
        .setReadTimeout(1, TimeUnit.SECONDS)
        .setWriteTimeout(1, TimeUnit.SECONDS);
  }

  @Test
  public void testSimpleGeocode() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context).address("Sydney").await();
    checkSydneyResult(results);
  }

  @Test
  public void testPlaceGeocode() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context)
        .place("ChIJP3Sa8ziYEmsRUKgyFmh9AQM")
        .await();
    checkSydneyResult(results);
  }

  @Test
  public void testAsync() throws Exception {
    final List<GeocodingResult[]> resps = new ArrayList<GeocodingResult[]>();

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
    GeocodingApi.newRequest(context).address("Sydney").setCallback(callback);

    Thread.sleep(2500);

    assertFalse(resps.isEmpty());
    assertNotNull(resps.get(0));
    checkSydneyResult(resps.get(0));
  }

  private void checkSydneyResult(GeocodingResult[] results) {
    assertNotNull(results);
    assertNotNull(results[0].geometry);
    assertNotNull(results[0].geometry.location);
    assertEquals(-33.8674869, results[0].geometry.location.lat, EPSILON);
    assertEquals(151.2069902, results[0].geometry.location.lng, EPSILON);
    assertEquals("ChIJP3Sa8ziYEmsRUKgyFmh9AQM", results[0].placeId);
    assertEquals(LocationType.APPROXIMATE, results[0].geometry.locationType);
  }

  @Test
  public void testBadKey() throws Exception {
    GeoApiContext badContext = new GeoApiContext()
        .setApiKey("AIza.........");

    GeocodingResult[] results = GeocodingApi.newRequest(badContext).address("Sydney")
        .awaitIgnoreError();
    assertNull(results);

    try {
      results = GeocodingApi.newRequest(badContext).address("Sydney").await();
      assertNull(results);
      fail("Expected exception REQUEST_DENIED");
    } catch (Exception e) {
      assertEquals("The provided API key is invalid.", e.getMessage());
    }
  }

  @Test
  public void testReverseGeocode() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context)
        .latlng(new LatLng(-33.8674869, 151.2069902)).await();

    assertTrue("Address didn't contain 'Sydney'",
        results[0].formattedAddress.contains("Sydney"));
  }

  /**
   * Simple geocode sample:
   * <a href="https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA">
   * Address Geocode for "1600 Amphitheatre Parkway, Mountain View, CA"</a>.
   */
  @Test
  public void testGeocodeTheGoogleplex() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context)
        .address("1600 Amphitheatre Parkway, Mountain View, CA").await();

    assertNotNull(results);
    assertEquals("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA",
        results[0].formattedAddress);
  }

  /**
   * Address geocode with bounds:
   * <a href="https://maps.googleapis.com/maps/api/geocode/json?address=Winnetka&bounds=34.172684,-118.604794|34.236144,-118.500938">
   * Winnetka within (34.172684,-118.604794) - (34.236144,-118.500938)</a>.
   */
  @Test
  public void testGeocodeWithBounds() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context).address("Winnetka")
        .bounds(new LatLng(34.172684, -118.604794), new LatLng(34.236144, -118.500938)).await();

    assertNotNull(results);
    assertEquals("Winnetka, Los Angeles, CA, USA", results[0].formattedAddress);
  }

  /**
   * Geocode with region biasing:
   * <a href="https://maps.googleapis.com/maps/api/geocode/json?address=Toledo&region=es>Geocode
   * for Toledo in Spain</a>.
   */
  @Test
  public void testGeocodeWithRegionBiasing() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context).address("Toledo").region("es")
        .await();

    assertNotNull(results);
    assertEquals("Toledo, Toledo, Spain", results[0].formattedAddress);
  }

  /**
   * Geocode with component filtering:
   * <a href="https://maps.googleapis.com/maps/api/geocode/json?address=santa+cruz&components=country:ES">
   * Geocoding "santa cruz" with country set to ES</a>.
   */
  @Test
  public void testGeocodeWithComponentFilter() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context).address("santa cruz")
        .components(ComponentFilter.country("ES")).await();

    assertNotNull(results);
    assertEquals("Santa Cruz de Tenerife, Santa Cruz de Tenerife, Spain",
        results[0].formattedAddress);
  }

  /**
   * Geocode with multiple component filters:
   * <a href="https://maps.googleapis.com/maps/api/geocode/json?address=Torun&components=administrative_area:TX|country:US">
   * Geocoding Torun, with administrative area of "TX" and country of "US"</a>.
   */
  @Test
  public void testGeocodeWithMultipleComponentFilters() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context).address("Torun")
        .components(administrativeArea("TX"), country("US")).await();

    assertNotNull(results);
    assertEquals("Texas, USA", results[0].formattedAddress);
  }

  /**
   * Making a request using just components filter:
   * <a href="https://maps.googleapis.com/maps/api/geocode/json?components=route:Annegatan|administrative_area:Helsinki|country:Finland">
   * Searching for a route of Annegatan, in the administrative area of Helsinki, and the country of
   * Finland </a>.
   */
  @Test
  public void testGeocodeWithJustComponents() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context).components(
        ComponentFilter.route("Annegatan"),
        ComponentFilter.administrativeArea("Helsinki"),
        ComponentFilter.country("Finland")).await();

    assertNotNull(results);
    assertTrue(results[0].formattedAddress.startsWith("Annegatan"));
  }

  /**
   * Simple reverse geocoding.
   * <a href="https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452">
   * Reverse geocode (40.714224,-73.961452)</a>.
   */
  @Test
  public void testSimpleReverseGeocode() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context)
        .latlng(new LatLng(40.714224, -73.961452)).await();

    assertNotNull(results);
    assertEquals("277 Bedford Ave, Brooklyn, NY 11211, USA", results[0].formattedAddress);
    assertEquals("277", results[0].addressComponents[0].longName);
    assertEquals("277", results[0].addressComponents[0].shortName);
    assertEquals(AddressComponentType.STREET_NUMBER,
        results[0].addressComponents[0].types[0]);
    assertEquals(AddressType.STREET_ADDRESS, results[0].types[0]);
  }

  /**
   * Reverse geocode restricted by type:
   * <a href="https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&location_type=ROOFTOP&result_type=street_address">
   * Reverse Geocode (40.714224,-73.961452) with location type of ROOFTOP and result type of
   * street_address</a>.
   */
  @Test
  public void testReverseGeocodeRestrictedByType() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context)
        .latlng(new LatLng(40.714224, -73.961452)).locationType(LocationType.ROOFTOP)
        .resultType(AddressType.STREET_ADDRESS).await();

    assertNotNull(results);
  }

  /**
   * Testing partial match.
   */
  @Test
  public void testPartialMatch() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context)
        .address("Pirrama Pyrmont").await();

    assertNotNull(results);
    assertTrue(results[0].partialMatch);
  }

  /**
   * Testing UTF8 result parsing.
   */
  @Test
  public void testUtfResult() throws Exception {
    GeocodingResult[] results = GeocodingApi.newRequest(context)
        .latlng(new LatLng(46.8023388, 1.6551867))
        .await();

    assertEquals("1 Rue Fernand Raynaud, 36000 Châteauroux, France", results[0].formattedAddress);
  }
}
