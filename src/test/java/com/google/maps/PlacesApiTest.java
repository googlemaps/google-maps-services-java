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

import com.google.maps.model.AddressComponentType;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceDetails.Review.AspectRating.RatingType;
import com.google.maps.model.PlaceDetails.OpeningHours.Period.OpenClose.DayOfWeek;
import com.google.maps.model.PlaceDetails.OpeningHours.Period;
import com.google.maps.model.PlaceDetails.PriceLevel;

import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PlacesApiTest {

  public static final String GOOGLE_SYDNEY = "ChIJN1t_tDeuEmsRUsoyG83frY4";
  public static final String QUAY_PLACE_ID = "ChIJ02qnq0KuEmsRHUJF4zo1x4I";
  public static final String QUERY_AUTOCOMPLETE_INPUT = "Pizza near Par";

  private GeoApiContext context = new GeoApiContext().setApiKey("AIzaFakeKey");
  private String placeDetailResponseBody;
  private String quayResponseBody;
  private String queryAutocompleteResponseBody;

  public PlacesApiTest() {
    placeDetailResponseBody = retrieveBody("PlaceDetailsResponse.txt");
    quayResponseBody = retrieveBody("PlaceDetailsQuay.txt");
    queryAutocompleteResponseBody = retrieveBody("QueryAutocompleteResponse.txt");
  }

  private String retrieveBody(String filename) {
    InputStream input = this.getClass().getResourceAsStream(filename);
    Scanner s = new java.util.Scanner(input).useDelimiter("\\A");
    return s.next();
  }

  @Test
  public void testPlaceDetailsRequest() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    MockWebServer server = new MockWebServer();
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    PlacesApi.placeDetails(context, GOOGLE_SYDNEY).awaitIgnoreError();

    List<NameValuePair> actualParams =
        parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue(GOOGLE_SYDNEY, "placeid", actualParams);

    server.shutdown();
  }

  @Test
  public void testPlaceDetailsLookupGoogleSydney() throws Exception {

    MockResponse response = new MockResponse();
    response.setBody(placeDetailResponseBody);
    MockWebServer server = new MockWebServer();
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    PlaceDetails placeDetails = PlacesApi.placeDetails(context, GOOGLE_SYDNEY).await();

    assertNotNull(placeDetails);

    // Address
    assertNotNull(placeDetails.addressComponents);
    assertEquals(placeDetails.addressComponents[0].longName, "5");
    assertEquals(placeDetails.addressComponents[0].types.length, 0);
    assertEquals(placeDetails.addressComponents[1].longName, "48");
    assertEquals(placeDetails.addressComponents[1].types[0], AddressComponentType.STREET_NUMBER);
    assertEquals(placeDetails.addressComponents[2].longName, "Pirrama Road");
    assertEquals(placeDetails.addressComponents[2].shortName, "Pirrama Rd");
    assertEquals(placeDetails.addressComponents[2].types[0], AddressComponentType.ROUTE);
    assertEquals(placeDetails.addressComponents[3].shortName, "Pyrmont");
    assertEquals(placeDetails.addressComponents[3].types[0], AddressComponentType.LOCALITY);
    assertEquals(placeDetails.addressComponents[3].types[1], AddressComponentType.POLITICAL);
    assertEquals(placeDetails.addressComponents[4].longName, "New South Wales");
    assertEquals(placeDetails.addressComponents[4].shortName, "NSW");
    assertEquals(placeDetails.addressComponents[4].types[0], AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1);
    assertEquals(placeDetails.addressComponents[4].types[1], AddressComponentType.POLITICAL);
    assertEquals(placeDetails.addressComponents[5].longName, "Australia");
    assertEquals(placeDetails.addressComponents[5].shortName, "AU");
    assertEquals(placeDetails.addressComponents[5].types[0], AddressComponentType.COUNTRY);
    assertEquals(placeDetails.addressComponents[5].types[1], AddressComponentType.POLITICAL);
    assertEquals(placeDetails.addressComponents[6].shortName, "2009");
    assertEquals(placeDetails.addressComponents[6].types[0], AddressComponentType.POSTAL_CODE);
    assertNotNull(placeDetails.formattedAddress);
    assertEquals(placeDetails.formattedAddress, "5, 48 Pirrama Rd, Pyrmont NSW 2009, Australia");
    assertNotNull(placeDetails.vicinity);
    assertEquals(placeDetails.vicinity, "5 48 Pirrama Road, Pyrmont");
    assertNotNull(placeDetails.adrAddress);
    assertEquals(placeDetails.adrAddress, "5, <span class=\"street-address\">48 Pirrama Rd</span>," +
        " <span class=\"locality\">Pyrmont</span> <span class=\"region\">NSW</span> " +
        "<span class=\"postal-code\">2009</span>, <span class=\"country-name\">Australia</span>");

    // Phone numbers
    assertNotNull(placeDetails.formattedPhoneNumber);
    assertEquals(placeDetails.formattedPhoneNumber, "(02) 9374 4000");
    assertNotNull(placeDetails.internationalPhoneNumber);
    assertEquals(placeDetails.internationalPhoneNumber, "+61 2 9374 4000");

    // Geometry
    assertNotNull(placeDetails.geometry);
    assertNotNull(placeDetails.geometry.location);
    assertNotNull(placeDetails.geometry.location.lat);
    assertEquals(placeDetails.geometry.location.lat, -33.866611, 0.001);
    assertNotNull(placeDetails.geometry.location.lng);
    assertEquals(placeDetails.geometry.location.lng, 151.195832, 0.001);

    // URLs
    assertNotNull(placeDetails.icon);
    assertEquals(placeDetails.icon.toURI(), new URI("https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png"));
    assertNotNull(placeDetails.url);
    assertEquals(placeDetails.url.toURI(), new URI("https://plus.google.com/111337342022929067349/about?hl=en-US"));
    assertNotNull(placeDetails.website);
    assertEquals(placeDetails.website.toURI(), new URI("https://www.google.com.au/about/careers/locations/sydney/"));

    // Name
    assertNotNull(placeDetails.name);
    assertEquals(placeDetails.name, "Google");

    // Opening Hours
    assertNotNull(placeDetails.openingHours);
    assertNotNull(placeDetails.openingHours.openNow);
    assertTrue(placeDetails.openingHours.openNow);
    assertNotNull(placeDetails.openingHours.periods);
    assertEquals(placeDetails.openingHours.periods.length, 5);

    {
      Period monday = placeDetails.openingHours.periods[0];
      Period tuesday = placeDetails.openingHours.periods[1];
      Period wednesday = placeDetails.openingHours.periods[2];
      Period thursday = placeDetails.openingHours.periods[3];
      Period friday = placeDetails.openingHours.periods[4];

      assertEquals(DayOfWeek.MONDAY, monday.open.day);
      assertEquals("0830", monday.open.time);
      assertEquals(DayOfWeek.MONDAY, monday.close.day);
      assertEquals("1730", monday.close.time);

      assertEquals(DayOfWeek.TUESDAY, tuesday.open.day);
      assertEquals("0830", tuesday.open.time);
      assertEquals(DayOfWeek.TUESDAY, tuesday.close.day);
      assertEquals("1730", tuesday.close.time);

      assertEquals(DayOfWeek.WEDNESDAY, wednesday.open.day);
      assertEquals("0830", wednesday.open.time);
      assertEquals(DayOfWeek.WEDNESDAY, wednesday.close.day);
      assertEquals("1730", wednesday.close.time);

      assertEquals(DayOfWeek.THURSDAY, thursday.open.day);
      assertEquals("0830", thursday.open.time);
      assertEquals(DayOfWeek.THURSDAY, thursday.close.day);
      assertEquals("1730", thursday.close.time);

      assertEquals(DayOfWeek.FRIDAY, friday.open.day);
      assertEquals("0830", friday.open.time);
      assertEquals(DayOfWeek.FRIDAY, friday.close.day);
      assertEquals("1700", friday.close.time);
    }

    assertNotNull(placeDetails.openingHours.weekdayText);
    assertEquals(placeDetails.openingHours.weekdayText[0], "Monday: 8:30 am – 5:30 pm");
    assertEquals(placeDetails.openingHours.weekdayText[1], "Tuesday: 8:30 am – 5:30 pm");
    assertEquals(placeDetails.openingHours.weekdayText[2], "Wednesday: 8:30 am – 5:30 pm");
    assertEquals(placeDetails.openingHours.weekdayText[3], "Thursday: 8:30 am – 5:30 pm");
    assertEquals(placeDetails.openingHours.weekdayText[4], "Friday: 8:30 am – 5:00 pm");
    assertEquals(placeDetails.openingHours.weekdayText[5], "Saturday: Closed");
    assertEquals(placeDetails.openingHours.weekdayText[6], "Sunday: Closed");
    assertNotNull(placeDetails.utcOffset);
    assertEquals(placeDetails.utcOffset, 600);


    // Photos
    assertNotNull(placeDetails.photos);
    assertNotNull(placeDetails.photos[0]);
    assertNotNull(placeDetails.photos[0].photoReference);
    assertNotNull(placeDetails.photos[0].height);
    assertNotNull(placeDetails.photos[0].width);
    assertNotNull(placeDetails.photos[0].htmlAttributions);
    assertNotNull(placeDetails.photos[0].htmlAttributions[0]);

    // Reviews
    assertNotNull(placeDetails.reviews);
    assertNotNull(placeDetails.reviews[0]);
    assertNotNull(placeDetails.reviews[0].authorName);
    assertEquals(placeDetails.reviews[0].authorName, "Danielle Lonnon");
    assertNotNull(placeDetails.reviews[0].authorUrl);
    assertEquals(placeDetails.reviews[0].authorUrl.toURI(),
        new URI("https://plus.google.com/118257578392162991040"));
    assertNotNull(placeDetails.reviews[0].language);
    assertEquals(placeDetails.reviews[0].language, "en");
    assertNotNull(placeDetails.reviews[0].rating);
    assertEquals(placeDetails.reviews[0].rating, 5);
    assertNotNull(placeDetails.reviews[0].text);
    assertTrue(placeDetails.reviews[0].text.startsWith("As someone who works in the theatre,"));
    assertNotNull(placeDetails.reviews[0].aspects);
    assertNotNull(placeDetails.reviews[0].aspects[0]);
    assertNotNull(placeDetails.reviews[0].aspects[0].rating);
    assertEquals(placeDetails.reviews[0].aspects[0].rating, 3);
    assertNotNull(placeDetails.reviews[0].aspects[0].type);
    assertEquals(placeDetails.reviews[0].aspects[0].type, RatingType.OVERALL);

    // Place ID
    assertNotNull(placeDetails.placeId);
    assertEquals(placeDetails.placeId, GOOGLE_SYDNEY);
    assertNotNull(placeDetails.scope);
    assertEquals(placeDetails.scope, PlaceDetails.PlaceIdScope.GOOGLE);
    assertNotNull(placeDetails.types);
    assertEquals(placeDetails.types[0], "establishment");
    assertNotNull(placeDetails.rating);
    assertEquals(placeDetails.rating, 4.4, 0.1);
    assertNotNull(placeDetails.userRatingsTotal);
    assertEquals(placeDetails.userRatingsTotal, 98);

    // Deprecated fields. Here for test completeness, but will be removed once the field is no longer returned.
    assertNotNull(placeDetails.id);
    assertNotNull(placeDetails.reference);

    server.shutdown();
  }

  @Test
  public void testPlaceDetailsLookupQuay() throws Exception {

    MockResponse response = new MockResponse();
    response.setBody(quayResponseBody);
    MockWebServer server = new MockWebServer();
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    PlaceDetails placeDetails = PlacesApi.placeDetails(context, QUAY_PLACE_ID).await();

    assertNotNull(placeDetails);

    assertNotNull(placeDetails.priceLevel);
    assertEquals(PriceLevel.VERY_EXPENSIVE, placeDetails.priceLevel);

    assertNotNull(placeDetails.photos);
    PlaceDetails.Photo photo = placeDetails.photos[0];
    assertEquals(1944, photo.height);
    assertEquals(2592, photo.width);
    assertEquals("<a href=\"https://maps.google.com/maps/contrib/101719343658521132777\">James Prendergast</a>",
        photo.htmlAttributions[0]);
    assertEquals("CmRdAAAATDVdhv0RdMEZlvO2jNE_EXXZZnCWvenfvLmWCsYqVtCFxZiasbcv1X0CNDTkpaCtrurGzVxTVt8Fqc7egdA7VyFeq1VFaq1GiFatWrFAUm_H0CN9u2wbfjb1Zf0NL9QiEhCj6I5O2h6eFH_2sa5hyVaEGhTdn8b7RWD-2W64OrT3mFGjzzLWlQ",
        photo.photoReference);

    server.shutdown();
  }

  @Test
  public void testQueryAutocompleteRequest() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    MockWebServer server = new MockWebServer();
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    QueryAutocompleteRequest req = PlacesApi.queryAutocomplete(context, QUERY_AUTOCOMPLETE_INPUT);
    req.offset(10);
    LatLng location = new LatLng(10, 20);
    req.location(location);
    req.radius(5000);
    req.language("en");
    req.awaitIgnoreError();

    List<NameValuePair> actualParams =
        parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue(QUERY_AUTOCOMPLETE_INPUT, "input", actualParams);
    assertParamValue("10", "offset", actualParams);
    assertParamValue(location.toUrlValue(), "location", actualParams);
    assertParamValue("5000", "radius", actualParams);
    assertParamValue("en", "language", actualParams);

    server.shutdown();

  }

  @Test
  public void testQueryAutocompletePizzaNearPar() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody(queryAutocompleteResponseBody);
    MockWebServer server = new MockWebServer();
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    QueryAutocompletePrediction[] predictions = PlacesApi.queryAutocomplete(context, QUERY_AUTOCOMPLETE_INPUT).await();

    assertNotNull(predictions);
    assertEquals(predictions.length, 5);

  }

  // TODO(brettmorgan): find a home for these utility methods

  private List<NameValuePair> parseQueryParamsFromRequestLine(String requestLine) throws Exception {
    // Extract the URL part from the HTTP request line
    String[] chunks = requestLine.split("\\s");
    String url = chunks[1];

    return URLEncodedUtils.parse(new URI(url), "UTF-8");
  }

  private void assertParamValue(String expectedValue, String paramName, List<NameValuePair> params)
      throws Exception {
    boolean paramFound = false;
    for (NameValuePair pair : params) {
      if (pair.getName().equals(paramName)) {
        paramFound = true;
        assertEquals(expectedValue, pair.getValue());
      }
    }
    assertTrue(paramFound);
  }

}
