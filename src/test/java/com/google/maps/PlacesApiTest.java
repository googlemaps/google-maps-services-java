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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.LatLng;
import com.google.maps.model.OpeningHours.Period;
import com.google.maps.model.OpeningHours.Period.OpenClose.DayOfWeek;
import com.google.maps.model.Photo;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceDetails.Review.AspectRating.RatingType;
import com.google.maps.model.PlaceIdScope;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.PriceLevel;
import com.google.maps.model.RankBy;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Scanner;

public class PlacesApiTest {

  public static final String GOOGLE_SYDNEY = "ChIJN1t_tDeuEmsRUsoyG83frY4";
  public static final String QUAY_PLACE_ID = "ChIJ02qnq0KuEmsRHUJF4zo1x4I";
  public static final String QUERY_AUTOCOMPLETE_INPUT = "pizza near par";

  private final GeoApiContext context = new GeoApiContext().setApiKey("AIzaFakeKey");
  private final String placeDetailResponseBody;
  private final String quayResponseBody;
  private final String queryAutocompleteResponseBody;
  private final String queryAutocompleteWithPlaceIdResponseBody;
  private final String textSearchResponseBody;
  private final String textSearchPizzaInNYCbody;


  public PlacesApiTest() {
    placeDetailResponseBody = retrieveBody("PlaceDetailsResponse.json");
    quayResponseBody = retrieveBody("PlaceDetailsQuay.json");
    queryAutocompleteResponseBody = retrieveBody("QueryAutocompleteResponse.json");
    queryAutocompleteWithPlaceIdResponseBody = retrieveBody("QueryAutocompleteResponseWithPlaceID.json");
    textSearchResponseBody = retrieveBody("TextSearchResponse.json");
    textSearchPizzaInNYCbody = retrieveBody("TextSearchPizzaInNYC.json");
  }

  private String retrieveBody(String filename) {
    InputStream input = this.getClass().getResourceAsStream(filename);
    Scanner s = new java.util.Scanner(input).useDelimiter("\\A");
    String body = s.next();
    if (body == null || body.length() == 0) {
      throw new IllegalArgumentException("filename '" + filename + "' resulted in null or empty body");
    }
    return body;
  }

  private MockWebServer server;

  @Before
  public void setup() {
    server = new MockWebServer();
  }

  @After
  public void teardown() throws Exception {
    server.shutdown();
  }

  @Test
  public void testPlaceDetailsRequest() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    PlacesApi.placeDetails(context, GOOGLE_SYDNEY).awaitIgnoreError();

    List<NameValuePair> actualParams =
        parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue(GOOGLE_SYDNEY, "placeid", actualParams);
  }

  @Test
  public void testPlaceDetailsLookupGoogleSydney() throws Exception {

    MockResponse response = new MockResponse();
    response.setBody(placeDetailResponseBody);
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
    assertEquals(placeDetails.icon.toURI(),
        new URI("https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png"));
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
      LocalTime opening = new LocalTime(8, 30);
      LocalTime closing5pm = new LocalTime(17, 0);
      LocalTime closing530pm = new LocalTime(17, 30);

      assertEquals(opening, monday.open.time);
      assertEquals(DayOfWeek.MONDAY, monday.close.day);
      assertEquals(closing530pm, monday.close.time);

      assertEquals(DayOfWeek.TUESDAY, tuesday.open.day);
      assertEquals(opening, tuesday.open.time);
      assertEquals(DayOfWeek.TUESDAY, tuesday.close.day);
      assertEquals(closing530pm, tuesday.close.time);

      assertEquals(DayOfWeek.WEDNESDAY, wednesday.open.day);
      assertEquals(opening, wednesday.open.time);
      assertEquals(DayOfWeek.WEDNESDAY, wednesday.close.day);
      assertEquals(closing530pm, wednesday.close.time);

      assertEquals(DayOfWeek.THURSDAY, thursday.open.day);
      assertEquals(opening, thursday.open.time);
      assertEquals(DayOfWeek.THURSDAY, thursday.close.day);
      assertEquals(closing530pm, thursday.close.time);

      assertEquals(DayOfWeek.FRIDAY, friday.open.day);
      assertEquals(opening, friday.open.time);
      assertEquals(DayOfWeek.FRIDAY, friday.close.day);
      assertEquals(closing5pm, friday.close.time);
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
    Photo photo = placeDetails.photos[0];
    assertNotNull(photo);
    assertNotNull(photo.photoReference);
    assertNotNull(photo.height);
    assertNotNull(photo.width);
    assertNotNull(photo.htmlAttributions);
    assertNotNull(photo.htmlAttributions[0]);

    // Reviews
    assertNotNull(placeDetails.reviews);
    PlaceDetails.Review review = placeDetails.reviews[0];
    assertNotNull(review);
    assertNotNull(review.authorName);
    assertEquals("Danielle Lonnon", review.authorName);
    assertNotNull(review.authorUrl);
    assertEquals(new URI("https://plus.google.com/118257578392162991040"), review.authorUrl.toURI());
    assertNotNull(review.language);
    assertEquals("en", review.language);
    assertNotNull(review.rating);
    assertEquals(5, review.rating);
    assertNotNull(review.text);
    assertTrue(review.text.startsWith("As someone who works in the theatre,"));
    assertNotNull(review.aspects);
    PlaceDetails.Review.AspectRating aspect = review.aspects[0];
    assertNotNull(aspect);
    assertNotNull(aspect.rating);
    assertEquals(3, aspect.rating);
    assertNotNull(aspect.type);
    assertEquals(RatingType.OVERALL, aspect.type);
    assertEquals(1425790392, review.time.getMillis() / 1000);
    assertEquals("2015-03-08 04:53AM", review.time.toString(DateTimeFormat.forPattern("YYYY-MM-dd HH:mmaa")));

    // Place ID
    assertNotNull(placeDetails.placeId);
    assertEquals(placeDetails.placeId, GOOGLE_SYDNEY);
    assertNotNull(placeDetails.scope);
    assertEquals(placeDetails.scope, PlaceIdScope.GOOGLE);
    assertNotNull(placeDetails.types);
    assertEquals(placeDetails.types[0], "establishment");
    assertNotNull(placeDetails.rating);
    assertEquals(placeDetails.rating, 4.4, 0.1);
  }

  @Test
  public void testPlaceDetailsLookupQuay() throws Exception {

    MockResponse response = new MockResponse();
    response.setBody(quayResponseBody);
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    PlaceDetails placeDetails = PlacesApi.placeDetails(context, QUAY_PLACE_ID).await();
    assertNotNull(placeDetails);
    assertNotNull(placeDetails.priceLevel);
    assertEquals(PriceLevel.VERY_EXPENSIVE, placeDetails.priceLevel);
    assertNotNull(placeDetails.photos);
    Photo photo = placeDetails.photos[0];
    assertEquals(1944, photo.height);
    assertEquals(2592, photo.width);
    assertEquals("<a href=\"https://maps.google.com/maps/contrib/101719343658521132777\">James Prendergast</a>",
        photo.htmlAttributions[0]);
    assertEquals(
        "CmRdAAAATDVdhv0RdMEZlvO2jNE_EXXZZnCWvenfvLmWCsYqVtCFxZiasbcv1X0CNDTkpaCtrurGzVxTVt8Fqc7egdA7VyFeq1VFaq1GiFatWrFAUm_H0CN9u2wbfjb1Zf0NL9QiEhCj6I5O2h6eFH_2sa5hyVaEGhTdn8b7RWD-2W64OrT3mFGjzzLWlQ",
        photo.photoReference);
  }

  @Test
  public void testQueryAutocompleteRequest() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    LatLng location = new LatLng(10, 20);
    PlacesApi.queryAutocomplete(context, QUERY_AUTOCOMPLETE_INPUT)
        .offset(10)
        .location(location)
        .radius(5000)
        .language("en")
        .awaitIgnoreError();

    List<NameValuePair> actualParams =
        parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue(QUERY_AUTOCOMPLETE_INPUT, "input", actualParams);
    assertParamValue("10", "offset", actualParams);
    assertParamValue(location.toUrlValue(), "location", actualParams);
    assertParamValue("5000", "radius", actualParams);
    assertParamValue("en", "language", actualParams);
  }

  @Test
  public void testQueryAutocompletePizzaNearPar() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody(queryAutocompleteResponseBody);
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    AutocompletePrediction[] predictions = PlacesApi.queryAutocomplete(context, QUERY_AUTOCOMPLETE_INPUT).await();

    assertNotNull(predictions);
    assertEquals(predictions.length, 5);

    {
      AutocompletePrediction prediction = predictions[0];
      assertNotNull(prediction);
      assertNotNull(prediction.description);
      assertEquals("pizza near Paris, France", prediction.description);

      assertEquals(3, prediction.matchedSubstrings.length);
      AutocompletePrediction.MatchedSubstring matchedSubstring = prediction.matchedSubstrings[0];
      assertEquals(5, matchedSubstring.length);
      assertEquals(0, matchedSubstring.offset);

      assertEquals(4, prediction.terms.length);
      AutocompletePrediction.Term term = prediction.terms[0];
      assertEquals(0, term.offset);
      assertEquals("pizza", term.value);
    }
  }

  @Test
  public void testQueryAutocompleteWithPlaceId() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody(queryAutocompleteWithPlaceIdResponseBody);
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    AutocompletePrediction[] predictions = PlacesApi.queryAutocomplete(context, QUERY_AUTOCOMPLETE_INPUT).await();

    assertNotNull(predictions);
    assertEquals(predictions.length, 1);

    {
      AutocompletePrediction prediction = predictions[0];
      assertNotNull(prediction);
      assertNotNull(prediction.description);
      assertEquals("Bondi Pizza, Campbell Parade, Sydney, New South Wales, Australia", prediction.description);

      assertEquals(2, prediction.matchedSubstrings.length);
      AutocompletePrediction.MatchedSubstring matchedSubstring = prediction.matchedSubstrings[0];
      assertEquals(5, matchedSubstring.length);
      assertEquals(6, matchedSubstring.offset);

      assertEquals(5, prediction.terms.length);
      AutocompletePrediction.Term term = prediction.terms[0];
      assertEquals(0, term.offset);
      assertEquals("Bondi Pizza", term.value);

      assertEquals("ChIJv0wpwp6tEmsR0Glcf5tugrk", prediction.placeId);
    }
  }

  @Test
  public void testTextSearchRequest() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    LatLng location = new LatLng(10, 20);
    PlacesApi.textSearchQuery(context, "Google Sydney")
        .location(location)
        .radius(3000)
        .minPrice(PriceLevel.INEXPENSIVE)
        .maxPrice(PriceLevel.VERY_EXPENSIVE)
        .name("name")
        .openNow(true)
        .rankby(RankBy.DISTANCE)
        .type(PlaceType.AIRPORT)
        .awaitIgnoreError();

    List<NameValuePair> actualParams = parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue("Google Sydney", "query", actualParams);
    assertParamValue(location.toUrlValue(), "location", actualParams);
    assertParamValue(String.valueOf(3000), "radius", actualParams);
    assertParamValue(String.valueOf(1), "minprice", actualParams);
    assertParamValue(String.valueOf(4), "maxprice", actualParams);
    assertParamValue("name", "name", actualParams);
    assertParamValue("true", "opennow", actualParams);
    assertParamValue(RankBy.DISTANCE.toString(), "rankby", actualParams);
    assertParamValue(PlaceType.AIRPORT.toString(), "type", actualParams);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTextSearchLocationWithoutRadius() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    LatLng location = new LatLng(10, 20);
    PlacesApi.textSearchQuery(context, "query")
        .location(location)
        .await();
  }

  @Test
  public void testTextSearchResponse() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody(textSearchResponseBody);
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    PlacesSearchResponse results = PlacesApi.textSearchQuery(context, "Google Sydney").await();

    assertNotNull(results);
    assertNotNull(results.results);
    assertEquals(1, results.results.length);
    {
      PlacesSearchResult result = results.results[0];
      assertNotNull(result.formattedAddress);
      assertEquals("5, 48 Pirrama Rd, Pyrmont NSW 2009, Australia", result.formattedAddress);
      assertNotNull(result.geometry);
      assertNotNull(result.geometry.location);
      assertEquals(-33.866611, result.geometry.location.lat, 0.0001);
      assertEquals(151.195832, result.geometry.location.lng, 0.0001);
      assertNotNull(result.icon);
      assertEquals(new URI("https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png"),
          result.icon.toURI());
      assertNotNull(result.name);
      assertEquals("Google", result.name);
      assertNotNull(result.openingHours);
      assertFalse(result.openingHours.openNow);
      assertNotNull(result.photos);
      {
        assertEquals(1, result.photos.length);
        Photo photo = result.photos[0];
        assertNotNull(photo);
        assertEquals(2322, photo.height);
        assertEquals(4128, photo.width);
        assertNotNull(photo.htmlAttributions);
        assertEquals(1, photo.htmlAttributions.length);
        assertEquals("<a href=\"https://maps.google.com/maps/contrib/107252953636064841537\">William Stewart</a>",
            photo.htmlAttributions[0]);
        assertEquals("CmRdAAAAa43ZeiQvF4n-Yv5UnEGcIe0KjdTzzTH4g-g1GuKgWas0g8W7793eFDGxkrG4Z5i_Jua0Z-" +
            "Ib88IuYe2iVAZ0W3Q7wUrp4A2mux4BjZmakLFkTkPj_OZ7ek3vSGnrzqExEhBqB3AIn82lmf38RnVSFH1CGhSWrvzN30A_" +
            "ABGNScuiYEU70wau3w", photo.photoReference);
      }
      assertNotNull(result.placeId);
      assertEquals("ChIJN1t_tDeuEmsRUsoyG83frY4", result.placeId);
      assertEquals(4.4, result.rating, 0.0001);
      assertNotNull(result.types);
      assertNotNull(result.types[0]);
      assertEquals("establishment", result.types[0]);
    }
  }

  @Test
  public void testTextSearchNYC() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody(textSearchPizzaInNYCbody);
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    PlacesSearchResponse results = PlacesApi.textSearchQuery(context, "Pizza in New York").await();
    assertNotNull(results.nextPageToken);
    assertEquals("CuQB1wAAANI17eHXt1HpqbLjkj7T5Ti69DEAClo02Qampg7Q6W_O_krFbge7hnTtDR7oVF3asex" +
        "HcGnUtR1ZKjroYd4BTCXxSGPi9LEkjJ0P_zVE7byjEBcHvkdxB6nCHKHAgVNGqe0ZHuwSYKlr3C1-" +
        "kuellMYwMlg3WSe69bJr1Ck35uToNZkUGvo4yjoYxNFRn1lABEnjPskbMdyHAjUDwvBDxzgGxpd8t" +
        "0EzA9UOM8Y1jqWnZGJM7u8gacNFcI4prr0Doh9etjY1yHrgGYI4F7lKPbfLQKiks_wYzoHbcAcdbB" +
        "jkEhAxDHC0XXQ16thDAlwVbEYaGhSaGDw5sHbaZkG9LZIqbcas0IJU8w", results.nextPageToken);
  }

  @Test
  public void testPhotoRequest() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    final String photoReference = "Photo Reference";
    final int width = 200;
    final int height = 100;

    PlacesApi.photo(context, photoReference)
        .maxWidth(width)
        .maxHeight(height)
        .awaitIgnoreError();

    List<NameValuePair> actualParams = parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue(photoReference, "photoreference", actualParams);
    assertParamValue(String.valueOf(width), "maxwidth", actualParams);
    assertParamValue(String.valueOf(height), "maxheight", actualParams);
  }

  @Test
  public void testNearbySearchRequest() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    LatLng location = new LatLng(10, 20);
    PlacesApi.nearbySearchQuery(context, location)
        .radius(5000)
        .rankby(RankBy.PROMINENCE)
        .keyword("keyword")
        .language("en")
        .minPrice(PriceLevel.INEXPENSIVE)
        .maxPrice(PriceLevel.EXPENSIVE)
        .name("name")
        .openNow(true)
        .type(PlaceType.AIRPORT)
        .pageToken("next-page-token")
        .awaitIgnoreError();

    List<NameValuePair> actualParams =
        parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue(location.toUrlValue(), "location", actualParams);
    assertParamValue("5000", "radius", actualParams);
    assertParamValue(RankBy.PROMINENCE.toString(), "rankby", actualParams);
    assertParamValue("keyword", "keyword", actualParams);
    assertParamValue("en", "language", actualParams);
    assertParamValue(PriceLevel.INEXPENSIVE.toString(), "minprice", actualParams);
    assertParamValue(PriceLevel.EXPENSIVE.toString(), "maxprice", actualParams);
    assertParamValue("name", "name", actualParams);
    assertParamValue("true", "opennow", actualParams);
    assertParamValue(PlaceType.AIRPORT.toString(), "type", actualParams);
    assertParamValue("next-page-token", "pagetoken", actualParams);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNearbySearchRadiusAndRankbyDistance() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    LatLng location = new LatLng(10, 20);
    PlacesApi.nearbySearchQuery(context, location)
        .radius(5000)
        .rankby(RankBy.DISTANCE)
        .await();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNearbySearchRankbyDistanceWithoutKeywordNameOrType() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    LatLng location = new LatLng(10, 20);
    PlacesApi.nearbySearchQuery(context, location)
        .rankby(RankBy.DISTANCE)
        .await();
  }

  @Test
  public void testRadarSearchRequest() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    LatLng location = new LatLng(10, 20);
    PlacesApi.radarSearchQuery(context, location, 5000)
        .keyword("keyword")
        .language("en")
        .minPrice(PriceLevel.INEXPENSIVE)
        .maxPrice(PriceLevel.EXPENSIVE)
        .name("name")
        .openNow(true)
        .type(PlaceType.AIRPORT)
        .awaitIgnoreError();

    List<NameValuePair> actualParams =
        parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue(location.toUrlValue(), "location", actualParams);
    assertParamValue("5000", "radius", actualParams);
    assertParamValue("keyword", "keyword", actualParams);
    assertParamValue("en", "language", actualParams);
    assertParamValue(PriceLevel.INEXPENSIVE.toString(), "minprice", actualParams);
    assertParamValue(PriceLevel.EXPENSIVE.toString(), "maxprice", actualParams);
    assertParamValue("name", "name", actualParams);
    assertParamValue("true", "opennow", actualParams);
    assertParamValue(PlaceType.AIRPORT.toString(), "type", actualParams);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRadarSearchLocationWithoutKeywordNameOrType() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    LatLng location = new LatLng(10, 20);
    PlacesApi.radarSearchQuery(context, location, 5000)
        .await();
  }

  @Test
  public void testPlaceAutocompleteRequest() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    LatLng location = new LatLng(10, 20);
    PlacesApi.placeAutocomplete(context, "Sydney Town Hall")
        .offset(4)
        .location(location)
        .radius(5000)
        .type(PlaceType.AIRPORT)
        .components(ComponentFilter.country("AU"))
        .awaitIgnoreError();

    List<NameValuePair> actualParams =
        parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue("Sydney Town Hall", "input", actualParams);
    assertParamValue(Integer.toString(4), "offset", actualParams);
    assertParamValue(location.toUrlValue(), "location", actualParams);
    assertParamValue("5000", "radius", actualParams);
    assertParamValue(PlaceType.AIRPORT.toString(), "type", actualParams);
    assertParamValue(ComponentFilter.country("AU").toString(), "components", actualParams);
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
