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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.AutocompletePrediction.MatchedSubstring;
import com.google.maps.model.AutocompleteStructuredFormatting;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.LatLng;
import com.google.maps.model.OpeningHours.Period;
import com.google.maps.model.OpeningHours.Period.OpenClose.DayOfWeek;
import com.google.maps.model.Photo;
import com.google.maps.model.PlaceAutocompleteType;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceDetails.Review.AspectRating.RatingType;
import com.google.maps.model.PlaceIdScope;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.PriceLevel;
import com.google.maps.model.RankBy;
import java.net.URI;
import java.util.Arrays;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

public class PlacesApiTest {

  private static final String GOOGLE_SYDNEY = "ChIJN1t_tDeuEmsRUsoyG83frY4";
  private static final String QUAY_PLACE_ID = "ChIJ02qnq0KuEmsRHUJF4zo1x4I";
  private static final String PERMANENTLY_CLOSED_PLACE_ID = "ChIJZQvy3jAbdkgR9avxegjoCe0";
  private static final String QUERY_AUTOCOMPLETE_INPUT = "pizza near par";
  private static final LatLng SYDNEY = new LatLng(-33.8650, 151.2094);

  private final String autocompletePredictionStructuredFormatting;
  private final String placeDetailResponseBody;
  private final String placeDetailResponseBodyForPermanentlyClosedPlace;
  private final String quayResponseBody;
  private final String queryAutocompleteResponseBody;
  private final String queryAutocompleteWithPlaceIdResponseBody;
  private final String textSearchResponseBody;
  private final String textSearchPizzaInNYCbody;
  private final String placesApiTextSearch;
  private final String placesApiPhoto;
  private final String placesApiPizzaInNewYork;
  private final String placesApiDetailsInFrench;
  private final String placesApiNearbySearchRequestByKeyword;
  private final String placesApiNearbySearchRequestByName;
  private final String placesApiNearbySearchRequestByType;
  private final String placesApiRadarSearchRequestByKeyword;
  private final String placesApiRadarSearchRequestByName;
  private final String placesApiRadarSearchRequestByType;
  private final String placesApiPlaceAutocomplete;
  private final String placesApiPlaceAutocompleteWithType;
  private final String placesApiKitaWard;

  public PlacesApiTest() {
    autocompletePredictionStructuredFormatting =
        retrieveBody("AutocompletePredictionStructuredFormatting.json");
    placeDetailResponseBody = retrieveBody("PlaceDetailsResponse.json");
    placeDetailResponseBodyForPermanentlyClosedPlace =
        retrieveBody("PlaceDetailsResponseForPermanentlyClosedPlace.json");
    quayResponseBody = retrieveBody("PlaceDetailsQuay.json");
    queryAutocompleteResponseBody = retrieveBody("QueryAutocompleteResponse.json");
    queryAutocompleteWithPlaceIdResponseBody =
        retrieveBody("QueryAutocompleteResponseWithPlaceID.json");
    textSearchResponseBody = retrieveBody("TextSearchResponse.json");
    textSearchPizzaInNYCbody = retrieveBody("TextSearchPizzaInNYC.json");
    placesApiTextSearch = retrieveBody("PlacesApiTextSearchResponse.json");
    placesApiPhoto = retrieveBody("PlacesApiPhotoResponse.json");
    placesApiPizzaInNewYork = retrieveBody("PlacesApiPizzaInNewYorkResponse.json");
    placesApiDetailsInFrench = retrieveBody("PlacesApiDetailsInFrenchResponse.json");
    placesApiNearbySearchRequestByKeyword =
        retrieveBody("PlacesApiNearbySearchRequestByKeywordResponse.json");
    placesApiNearbySearchRequestByName =
        retrieveBody("PlacesApiNearbySearchRequestByNameResponse.json");
    placesApiNearbySearchRequestByType =
        retrieveBody("PlacesApiNearbySearchRequestByTypeResponse.json");
    placesApiRadarSearchRequestByKeyword =
        retrieveBody("PlacesApiRadarSearchRequestByKeywordResponse.json");
    placesApiRadarSearchRequestByName =
        retrieveBody("PlacesApiRadarSearchRequestByNameResponse.json");
    placesApiRadarSearchRequestByType =
        retrieveBody("PlacesApiRadarSearchRequestByTypeResponse.json");
    placesApiPlaceAutocomplete = retrieveBody("PlacesApiPlaceAutocompleteResponse.json");
    placesApiPlaceAutocompleteWithType =
        retrieveBody("PlacesApiPlaceAutocompleteWithTypeResponse.json");
    placesApiKitaWard = retrieveBody("placesApiKitaWardResponse.json");
  }

  @Test
  public void testPlaceDetailsRequest() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}")) {
      PlacesApi.placeDetails(sc.context, GOOGLE_SYDNEY).await();

      sc.assertParamValue(GOOGLE_SYDNEY, "placeid");
    }
  }

  @Test
  public void testAutocompletePredictionStructuredFormatting() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(autocompletePredictionStructuredFormatting)) {
      final AutocompletePrediction[] predictions =
          PlacesApi.placeAutocomplete(sc.context, "1").await();

      assertNotNull(predictions);
      assertEquals(1, predictions.length);
      final AutocompletePrediction prediction = predictions[0];
      assertNotNull(prediction);
      assertEquals("1033 Princes Highway, Heathmere, Victoria, Australia", prediction.description);
      final AutocompleteStructuredFormatting structuredFormatting = prediction.structuredFormatting;
      assertNotNull(structuredFormatting);
      assertEquals("1033 Princes Highway", structuredFormatting.mainText);
      assertEquals("Heathmere, Victoria, Australia", structuredFormatting.secondaryText);
      assertEquals(1, structuredFormatting.mainTextMatchedSubstrings.length);
      final MatchedSubstring matchedSubstring = structuredFormatting.mainTextMatchedSubstrings[0];
      assertNotNull(matchedSubstring);
      assertEquals(1, matchedSubstring.length);
      assertEquals(0, matchedSubstring.offset);
    }
  }

  @Test
  public void testPlaceDetailsLookupGoogleSydney() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(placeDetailResponseBody)) {
      PlaceDetails placeDetails = PlacesApi.placeDetails(sc.context, GOOGLE_SYDNEY).await();

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
      assertEquals(
          placeDetails.addressComponents[4].types[0],
          AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1);
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
      assertEquals(
          placeDetails.icon.toURI(),
          new URI("https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png"));
      assertNotNull(placeDetails.url);
      assertEquals(
          placeDetails.url.toURI(),
          new URI("https://plus.google.com/111337342022929067349/about?hl=en-US"));
      assertNotNull(placeDetails.website);
      assertEquals(
          placeDetails.website.toURI(),
          new URI("https://www.google.com.au/about/careers/locations/sydney/"));

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
      assertEquals(
          new URI("https://plus.google.com/118257578392162991040"), review.authorUrl.toURI());
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
      assertEquals(
          "2015-03-08 04:53AM",
          review.time.toString(DateTimeFormat.forPattern("YYYY-MM-dd HH:mmaa")));

      // Place ID
      assertNotNull(placeDetails.placeId);
      assertEquals(placeDetails.placeId, GOOGLE_SYDNEY);
      assertNotNull(placeDetails.scope);
      assertEquals(placeDetails.scope, PlaceIdScope.GOOGLE);
      assertNotNull(placeDetails.types);
      assertEquals(placeDetails.types[0], "establishment");
      assertNotNull(placeDetails.rating);
      assertEquals(placeDetails.rating, 4.4, 0.1);

      // Permanently closed:
      assertFalse(placeDetails.permanentlyClosed);
    }
  }

  @Test
  public void testPlaceDetailsLookupPermanentlyClosedPlace() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(placeDetailResponseBodyForPermanentlyClosedPlace)) {
      PlaceDetails placeDetails =
          PlacesApi.placeDetails(sc.context, PERMANENTLY_CLOSED_PLACE_ID).await();
      assertNotNull(placeDetails);
      assertTrue(placeDetails.permanentlyClosed);
    }
  }

  @Test
  public void testPlaceDetailsLookupQuay() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(quayResponseBody)) {
      PlaceDetails placeDetails = PlacesApi.placeDetails(sc.context, QUAY_PLACE_ID).await();
      assertNotNull(placeDetails);
      assertNotNull(placeDetails.priceLevel);
      assertEquals(PriceLevel.VERY_EXPENSIVE, placeDetails.priceLevel);
      assertNotNull(placeDetails.photos);
      Photo photo = placeDetails.photos[0];
      assertEquals(1944, photo.height);
      assertEquals(2592, photo.width);
      assertEquals(
          "<a href=\"https://maps.google.com/maps/contrib/101719343658521132777\">James Prendergast</a>",
          photo.htmlAttributions[0]);
      assertEquals(
          "CmRdAAAATDVdhv0RdMEZlvO2jNE_EXXZZnCWvenfvLmWCsYqVtCFxZiasbcv1X0CNDTkpaCtrurGzVxTVt8Fqc7egdA7VyFeq1VFaq1GiFatWrFAUm_H0CN9u2wbfjb1Zf0NL9QiEhCj6I5O2h6eFH_2sa5hyVaEGhTdn8b7RWD-2W64OrT3mFGjzzLWlQ",
          photo.photoReference);
    }
  }

  @Test
  public void testQueryAutocompleteRequest() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}")) {
      LatLng location = new LatLng(10, 20);
      PlacesApi.queryAutocomplete(sc.context, QUERY_AUTOCOMPLETE_INPUT)
          .offset(10)
          .location(location)
          .radius(5000)
          .language("en")
          .await();

      sc.assertParamValue(QUERY_AUTOCOMPLETE_INPUT, "input");
      sc.assertParamValue("10", "offset");
      sc.assertParamValue(location.toUrlValue(), "location");
      sc.assertParamValue("5000", "radius");
      sc.assertParamValue("en", "language");
    }
  }

  @Test
  public void testQueryAutocompletePizzaNearPar() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(queryAutocompleteResponseBody)) {
      AutocompletePrediction[] predictions =
          PlacesApi.queryAutocomplete(sc.context, QUERY_AUTOCOMPLETE_INPUT).await();

      assertNotNull(predictions);
      assertEquals(predictions.length, 5);

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
    try (LocalTestServerContext sc =
        new LocalTestServerContext(queryAutocompleteWithPlaceIdResponseBody)) {
      AutocompletePrediction[] predictions =
          PlacesApi.queryAutocomplete(sc.context, QUERY_AUTOCOMPLETE_INPUT).await();

      assertNotNull(predictions);
      assertEquals(predictions.length, 1);

      AutocompletePrediction prediction = predictions[0];
      assertNotNull(prediction);
      assertNotNull(prediction.description);
      assertEquals(
          "Bondi Pizza, Campbell Parade, Sydney, New South Wales, Australia",
          prediction.description);

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
    try (LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}")) {
      LatLng location = new LatLng(10, 20);
      PlacesApi.textSearchQuery(sc.context, "Google Sydney")
          .location(location)
          .radius(3000)
          .minPrice(PriceLevel.INEXPENSIVE)
          .maxPrice(PriceLevel.VERY_EXPENSIVE)
          .name("name")
          .openNow(true)
          .rankby(RankBy.DISTANCE)
          .type(PlaceType.AIRPORT)
          .await();

      sc.assertParamValue("Google Sydney", "query");
      sc.assertParamValue(location.toUrlValue(), "location");
      sc.assertParamValue(String.valueOf(3000), "radius");
      sc.assertParamValue(String.valueOf(1), "minprice");
      sc.assertParamValue(String.valueOf(4), "maxprice");
      sc.assertParamValue("name", "name");
      sc.assertParamValue("true", "opennow");
      sc.assertParamValue(RankBy.DISTANCE.toString(), "rankby");
      sc.assertParamValue(PlaceType.AIRPORT.toString(), "type");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTextSearchLocationWithoutRadius() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}")) {
      LatLng location = new LatLng(10, 20);
      PlacesApi.textSearchQuery(sc.context, "query").location(location).await();
    }
  }

  @Test
  public void testTextSearchResponse() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(textSearchResponseBody)) {
      PlacesSearchResponse results = PlacesApi.textSearchQuery(sc.context, "Google Sydney").await();

      assertNotNull(results);
      assertNotNull(results.results);
      assertEquals(1, results.results.length);

      PlacesSearchResult result = results.results[0];
      assertNotNull(result.formattedAddress);
      assertEquals("5, 48 Pirrama Rd, Pyrmont NSW 2009, Australia", result.formattedAddress);
      assertNotNull(result.geometry);
      assertNotNull(result.geometry.location);
      assertEquals(-33.866611, result.geometry.location.lat, 0.0001);
      assertEquals(151.195832, result.geometry.location.lng, 0.0001);
      assertNotNull(result.icon);
      assertEquals(
          new URI("https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png"),
          result.icon.toURI());
      assertNotNull(result.name);
      assertEquals("Google", result.name);
      assertNotNull(result.openingHours);
      assertFalse(result.openingHours.openNow);
      assertNotNull(result.photos);

      assertEquals(1, result.photos.length);
      Photo photo = result.photos[0];
      assertNotNull(photo);
      assertEquals(2322, photo.height);
      assertEquals(4128, photo.width);
      assertNotNull(photo.htmlAttributions);
      assertEquals(1, photo.htmlAttributions.length);
      assertEquals(
          "<a href=\"https://maps.google.com/maps/contrib/107252953636064841537\">William Stewart</a>",
          photo.htmlAttributions[0]);
      assertEquals(
          "CmRdAAAAa43ZeiQvF4n-Yv5UnEGcIe0KjdTzzTH4g-g1GuKgWas0g8W7793eFDGxkrG4Z5i_Jua0Z-"
              + "Ib88IuYe2iVAZ0W3Q7wUrp4A2mux4BjZmakLFkTkPj_OZ7ek3vSGnrzqExEhBqB3AIn82lmf38RnVSFH1CGhSWrvzN30A_"
              + "ABGNScuiYEU70wau3w",
          photo.photoReference);

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
    try (LocalTestServerContext sc = new LocalTestServerContext(textSearchPizzaInNYCbody)) {
      PlacesSearchResponse results =
          PlacesApi.textSearchQuery(sc.context, "Pizza in New York").await();
      assertNotNull(results.nextPageToken);
      assertEquals(
          "CuQB1wAAANI17eHXt1HpqbLjkj7T5Ti69DEAClo02Qampg7Q6W_O_krFbge7hnTtDR7oVF3asex"
              + "HcGnUtR1ZKjroYd4BTCXxSGPi9LEkjJ0P_zVE7byjEBcHvkdxB6nCHKHAgVNGqe0ZHuwSYKlr3C1-"
              + "kuellMYwMlg3WSe69bJr1Ck35uToNZkUGvo4yjoYxNFRn1lABEnjPskbMdyHAjUDwvBDxzgGxpd8t"
              + "0EzA9UOM8Y1jqWnZGJM7u8gacNFcI4prr0Doh9etjY1yHrgGYI4F7lKPbfLQKiks_wYzoHbcAcdbB"
              + "jkEhAxDHC0XXQ16thDAlwVbEYaGhSaGDw5sHbaZkG9LZIqbcas0IJU8w",
          results.nextPageToken);
    }
  }

  @Test
  public void testPhotoRequest() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("")) {
      final String photoReference = "Photo Reference";
      final int width = 200;
      final int height = 100;

      PlacesApi.photo(sc.context, photoReference)
          .maxWidth(width)
          .maxHeight(height)
          .awaitIgnoreError();

      sc.assertParamValue(photoReference, "photoreference");
      sc.assertParamValue(String.valueOf(width), "maxwidth");
      sc.assertParamValue(String.valueOf(height), "maxheight");
    }
  }

  @Test
  public void testNearbySearchRequest() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}")) {
      LatLng location = new LatLng(10, 20);
      PlacesApi.nearbySearchQuery(sc.context, location)
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
          .await();

      sc.assertParamValue(location.toUrlValue(), "location");
      sc.assertParamValue("5000", "radius");
      sc.assertParamValue(RankBy.PROMINENCE.toString(), "rankby");
      sc.assertParamValue("keyword", "keyword");
      sc.assertParamValue("en", "language");
      sc.assertParamValue(PriceLevel.INEXPENSIVE.toString(), "minprice");
      sc.assertParamValue(PriceLevel.EXPENSIVE.toString(), "maxprice");
      sc.assertParamValue("name", "name");
      sc.assertParamValue("true", "opennow");
      sc.assertParamValue(PlaceType.AIRPORT.toString(), "type");
      sc.assertParamValue("next-page-token", "pagetoken");
    }
  }

  @Test
  public void testNearbySearchRequestWithMultipleType() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}")) {
      LatLng location = new LatLng(10, 20);
      PlacesApi.nearbySearchQuery(sc.context, location)
          .type(PlaceType.AIRPORT, PlaceType.BANK)
          .await();

      sc.assertParamValue(location.toUrlValue(), "location");
      sc.assertParamValue(PlaceType.AIRPORT.toString() + "|" + PlaceType.BANK.toString(), "type");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNearbySearchRadiusAndRankbyDistance() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("")) {
      LatLng location = new LatLng(10, 20);
      PlacesApi.nearbySearchQuery(sc.context, location)
          .radius(5000)
          .rankby(RankBy.DISTANCE)
          .await();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNearbySearchRankbyDistanceWithoutKeywordNameOrType() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("")) {
      LatLng location = new LatLng(10, 20);
      PlacesApi.nearbySearchQuery(sc.context, location).rankby(RankBy.DISTANCE).await();
    }
  }

  @Test
  @SuppressWarnings("deprecation") // radarSearchQuery still supported until 6/30/2018
  public void testRadarSearchRequest() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}")) {
      LatLng location = new LatLng(10, 20);
      PlacesApi.radarSearchQuery(sc.context, location, 5000)
          .keyword("keyword")
          .language("en")
          .minPrice(PriceLevel.INEXPENSIVE)
          .maxPrice(PriceLevel.EXPENSIVE)
          .name("name")
          .openNow(true)
          .type(PlaceType.AIRPORT)
          .await();

      sc.assertParamValue(location.toUrlValue(), "location");
      sc.assertParamValue("5000", "radius");
      sc.assertParamValue("keyword", "keyword");
      sc.assertParamValue("en", "language");
      sc.assertParamValue(PriceLevel.INEXPENSIVE.toString(), "minprice");
      sc.assertParamValue(PriceLevel.EXPENSIVE.toString(), "maxprice");
      sc.assertParamValue("name", "name");
      sc.assertParamValue("true", "opennow");
      sc.assertParamValue(PlaceType.AIRPORT.toString(), "type");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  @SuppressWarnings("deprecation") // radarSearchQuery still supported until 6/30/2018
  public void testRadarSearchLocationWithoutKeywordNameOrType() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("")) {
      LatLng location = new LatLng(10, 20);
      PlacesApi.radarSearchQuery(sc.context, location, 5000).await();
    }
  }

  @Test
  public void testPlaceAutocompleteRequest() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}")) {
      LatLng location = new LatLng(10, 20);
      PlacesApi.placeAutocomplete(sc.context, "Sydney Town Hall")
          .offset(4)
          .location(location)
          .radius(5000)
          .types(PlaceAutocompleteType.ESTABLISHMENT)
          .components(ComponentFilter.country("AU"))
          .await();

      sc.assertParamValue("Sydney Town Hall", "input");
      sc.assertParamValue(Integer.toString(4), "offset");
      sc.assertParamValue(location.toUrlValue(), "location");
      sc.assertParamValue("5000", "radius");
      sc.assertParamValue(PlaceAutocompleteType.ESTABLISHMENT.toString(), "types");
      sc.assertParamValue(ComponentFilter.country("AU").toString(), "components");
    }
  }

  @Test
  public void testTextSearch() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(placesApiTextSearch)) {
      PlacesSearchResponse response =
          PlacesApi.textSearchQuery(sc.context, "Google Sydney").await();

      sc.assertParamValue("Google Sydney", "query");

      assertEquals(1, response.results.length);
      PlacesSearchResult result = response.results[0];
      assertEquals("5, 48 Pirrama Rd, Pyrmont NSW 2009, Australia", result.formattedAddress);
      assertEquals("ChIJN1t_tDeuEmsRUsoyG83frY4", result.placeId);
    }
  }

  @Test
  public void testPhoto() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(placesApiPhoto)) {
      PlaceDetails placeDetails = PlacesApi.placeDetails(sc.context, GOOGLE_SYDNEY).await();

      sc.assertParamValue("ChIJN1t_tDeuEmsRUsoyG83frY4", "placeid");

      assertEquals(10, placeDetails.photos.length);
      assertEquals(
          "CmRaAAAA-N3w5YTMXWautuDW7IZgX9knz_2fNyyUpCWpvYdVEVb8RurBiisMKvr7AFxMW8dsu2yakYoqjW-IYSFk2cylXVM_c50cCxfm7MlgjPErFxumlcW1bLNOe--SwLYmWlvkEhDxjz75xRqim-CkVlwFyp7sGhTs1fE02MZ6GQcc-TugrepSaeWapA",
          placeDetails.photos[0].photoReference);
      assertEquals(1365, placeDetails.photos[0].height);
      assertEquals(2048, placeDetails.photos[0].width);
    }
  }

  @Test
  public void testPizzaInNewYorkPagination() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(placesApiPizzaInNewYork)) {
      PlacesSearchResponse response =
          PlacesApi.textSearchQuery(sc.context, "Pizza in New York").await();

      sc.assertParamValue("Pizza in New York", "query");

      assertEquals(20, response.results.length);
      assertEquals(
          "CvQB6AAAAPQLwX6KjvGbOw81Y7aYVhXRlHR8M60aCRXFDM9eyflac4BjE5MaNxTj_1T429x3H2kzBd-ztTFXCSu1CPh3kY44Gu0gmL-xfnArnPE9-BgfqXTpgzGPZNeCltB7m341y4LnU-NE2omFPoDWIrOPIyHnyi05Qol9eP2wKW7XPUhMlHvyl9MeVgZ8COBZKvCdENHbhBD1MN1lWlada6A9GPFj06cCp1aqRGW6v98-IHcIcM9RcfMcS4dLAFm6TsgLq4tpeU6E1kSzhrvDiLMBXdJYFlI0qJmytd2wS3vD0t3zKgU6Im_mY-IJL7AwAqhugBIQ8k0X_n6TnacL9BExELBaixoUo8nPOwWm0Nx02haufF2dY0VL-tg",
          response.nextPageToken);
    }
  }

  @Test
  public void testPlaceDetailsInFrench() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(placesApiDetailsInFrench)) {
      PlaceDetails details =
          PlacesApi.placeDetails(sc.context, "ChIJ442GNENu5kcRGYUrvgqHw88").language("fr").await();

      sc.assertParamValue("ChIJ442GNENu5kcRGYUrvgqHw88", "placeid");
      sc.assertParamValue("fr", "language");

      assertEquals("ChIJ442GNENu5kcRGYUrvgqHw88", details.placeId);
      assertEquals(
          "35 Rue du Chevalier de la Barre, 75018 Paris, France", details.formattedAddress);
      assertEquals("Sacré-Cœur", details.name);
    }
  }

  @Test
  public void testNearbySearchRequestByKeyword() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(placesApiNearbySearchRequestByKeyword)) {
      PlacesSearchResponse response =
          PlacesApi.nearbySearchQuery(sc.context, SYDNEY).radius(10000).keyword("pub").await();

      sc.assertParamValue("10000", "radius");
      sc.assertParamValue("pub", "keyword");
      sc.assertParamValue(SYDNEY.toUrlValue(), "location");

      assertEquals(20, response.results.length);
    }
  }

  @Test
  public void testNearbySearchRequestByName() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(placesApiNearbySearchRequestByName)) {
      PlacesSearchResponse response =
          PlacesApi.nearbySearchQuery(sc.context, SYDNEY)
              .radius(10000)
              .name("Sydney Town Hall")
              .await();

      sc.assertParamValue("Sydney Town Hall", "name");
      sc.assertParamValue(SYDNEY.toUrlValue(), "location");
      sc.assertParamValue("10000", "radius");

      assertEquals("Sydney Town Hall", response.results[0].name);
    }
  }

  @Test
  public void testNearbySearchRequestByType() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(placesApiNearbySearchRequestByType)) {
      PlacesSearchResponse response =
          PlacesApi.nearbySearchQuery(sc.context, SYDNEY).radius(10000).type(PlaceType.BAR).await();

      sc.assertParamValue(SYDNEY.toUrlValue(), "location");
      sc.assertParamValue("10000", "radius");
      sc.assertParamValue(PlaceType.BAR.toUrlValue(), "type");

      assertEquals(20, response.results.length);
    }
  }

  @Test
  @SuppressWarnings("deprecation") // radarSearchQuery still supported until 6/30/2018
  public void testRadarSearchRequestByKeyword() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(placesApiRadarSearchRequestByKeyword)) {
      PlacesSearchResponse response =
          PlacesApi.radarSearchQuery(sc.context, SYDNEY, 10000).keyword("pub").await();

      sc.assertParamValue(SYDNEY.toUrlValue(), "location");
      sc.assertParamValue("10000", "radius");
      sc.assertParamValue("pub", "keyword");

      assertTrue(100 < response.results.length);
    }
  }

  @Test
  @SuppressWarnings("deprecation") // radarSearchQuery still supported until 6/30/2018
  public void testRadarSearchRequestByName() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(placesApiRadarSearchRequestByName)) {
      PlacesSearchResponse response =
          PlacesApi.radarSearchQuery(sc.context, SYDNEY, 10000).name("Sydney Town Hall").await();

      sc.assertParamValue("Sydney Town Hall", "name");
      sc.assertParamValue("10000", "radius");
      sc.assertParamValue(SYDNEY.toUrlValue(), "location");

      assertEquals("ChIJhSxoJzyuEmsR9gBDBR09ZrE", response.results[0].placeId);
      assertEquals(-33.8731575, response.results[0].geometry.location.lat, 0.001);
      assertEquals(151.2061157, response.results[0].geometry.location.lng, 0.001);
      assertEquals(125, response.results.length);
    }
  }

  @Test
  @SuppressWarnings("deprecation") // radarSearchQuery still supported until 6/30/2018
  public void testRadarSearchRequestByType() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(placesApiRadarSearchRequestByType)) {
      PlacesSearchResponse response =
          PlacesApi.radarSearchQuery(sc.context, SYDNEY, 10000).type(PlaceType.BAR).await();

      sc.assertParamValue(SYDNEY.toUrlValue(), "location");
      sc.assertParamValue(PlaceType.BAR.toUrlValue(), "type");
      sc.assertParamValue("10000", "radius");

      assertEquals(197, response.results.length);
    }
  }

  @Test
  public void testPlaceAutocomplete() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(placesApiPlaceAutocomplete)) {
      AutocompletePrediction[] predictions =
          PlacesApi.placeAutocomplete(sc.context, "Sydney Town Ha").await();

      sc.assertParamValue("Sydney Town Ha", "input");

      assertEquals(5, predictions.length);
      assertTrue(predictions[0].description.contains("Town Hall"));
    }
  }

  @Test
  public void testPlaceAutocompleteWithType() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(placesApiPlaceAutocompleteWithType)) {
      AutocompletePrediction[] predictions =
          PlacesApi.placeAutocomplete(sc.context, "po")
              .components(ComponentFilter.country("nz"))
              .types(PlaceAutocompleteType.REGIONS)
              .await();

      sc.assertParamValue("po", "input");
      sc.assertParamValue("country:nz", "components");
      sc.assertParamValue("(regions)", "types");

      assertEquals(5, predictions.length);
      for (int i = 0; i < predictions.length; i++) {
        for (int j = 0; j < predictions[i].types.length; j++) {
          assertFalse(predictions[i].types[j].equals("route"));
          assertFalse(predictions[i].types[j].equals("establishment"));
        }
      }
    }
  }

  @Test
  public void testPlaceAutocompleteWithStrictBounds() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(placesApiPlaceAutocomplete)) {
      AutocompletePrediction[] predictions =
          PlacesApi.placeAutocomplete(sc.context, "Amoeba")
              .types(PlaceAutocompleteType.ESTABLISHMENT)
              .location(new LatLng(37.76999, -122.44696))
              .radius(500)
              .strictBounds(true)
              .await();

      sc.assertParamValue("Amoeba", "input");
      sc.assertParamValue("establishment", "types");
      sc.assertParamValue("37.76999000,-122.44696000", "location");
      sc.assertParamValue("500", "radius");
      sc.assertParamValue("true", "strictbounds");
    }
  }

  @Test
  public void testKitaWard() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(placesApiKitaWard)) {
      String query = "Kita Ward, Kyoto, Kyoto Prefecture, Japan";
      PlacesSearchResponse response = PlacesApi.textSearchQuery(sc.context, query).await();

      sc.assertParamValue(query, "query");

      assertEquals(
          "Kita Ward, Kyoto, Kyoto Prefecture, Japan", response.results[0].formattedAddress);
      assertTrue(Arrays.asList(response.results[0].types).contains("ward"));
    }
  }
}
