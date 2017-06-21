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

import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.LatLng;
import com.google.maps.model.Photo;
import com.google.maps.model.PlaceAutocompleteType;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceIdScope;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.maps.TestUtils.retrieveBody;


@Category(LargeTests.class)
public class PlacesApiIntegrationTest {
  private static final String GOOGLE_SYDNEY = "ChIJN1t_tDeuEmsRUsoyG83frY4";
  private static final LatLng SYDNEY = new LatLng(-33.8650, 151.2094);

  private final String placeDetailsLookupGoogleSydney;
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

  public PlacesApiIntegrationTest() {
    placeDetailsLookupGoogleSydney = retrieveBody("PlaceDetailsLookupGoogleSydneyResponse.json");
    placesApiTextSearch = retrieveBody("PlacesApiTextSearchResponse.json");
    placesApiPhoto = retrieveBody("PlacesApiPhotoResponse.json");
    placesApiPizzaInNewYork = retrieveBody("PlacesApiPizzaInNewYorkResponse.json");
    placesApiDetailsInFrench = retrieveBody("PlacesApiDetailsInFrenchResponse.json");
    placesApiNearbySearchRequestByKeyword = retrieveBody("PlacesApiNearbySearchRequestByKeywordResponse.json");
    placesApiNearbySearchRequestByName = retrieveBody("PlacesApiNearbySearchRequestByNameResponse.json");
    placesApiNearbySearchRequestByType = retrieveBody("PlacesApiNearbySearchRequestByTypeResponse.json");
    placesApiRadarSearchRequestByKeyword = retrieveBody("PlacesApiRadarSearchRequestByKeywordResponse.json");
    placesApiRadarSearchRequestByName = retrieveBody("PlacesApiRadarSearchRequestByNameResponse.json");
    placesApiRadarSearchRequestByType = retrieveBody("PlacesApiRadarSearchRequestByTypeResponse.json");
    placesApiPlaceAutocomplete = retrieveBody("PlacesApiPlaceAutocompleteResponse.json");
    placesApiPlaceAutocompleteWithType = retrieveBody("PlacesApiPlaceAutocompleteWithTypeResponse.json");
    placesApiKitaWard = retrieveBody("placesApiKitaWardResponse.json");
  }

  @Test
  public void testPlaceDetailsLookupGoogleSydney() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placeDetailsLookupGoogleSydney);
    PlaceDetails placeDetails = PlacesApi.placeDetails(sc.context, GOOGLE_SYDNEY).await();

    sc.assertParamValue("ChIJN1t_tDeuEmsRUsoyG83frY4", "placeid");

    // Address
    assertNotNull(placeDetails.addressComponents);
    assertNotNull(placeDetails.formattedAddress);
    assertEquals("5, 48 Pirrama Rd, Pyrmont NSW 2009, Australia", placeDetails.formattedAddress);
    assertNotNull(placeDetails.vicinity);
    assertEquals("5, 48 Pirrama Road, Pyrmont", placeDetails.vicinity);

    // Phone numbers
    assertNotNull(placeDetails.formattedPhoneNumber);
    assertEquals("(02) 9374 4000", placeDetails.formattedPhoneNumber);
    assertNotNull(placeDetails.internationalPhoneNumber);
    assertEquals("+61 2 9374 4000", placeDetails.internationalPhoneNumber);

    // Geometry
    assertNotNull(placeDetails.geometry);
    assertNotNull(placeDetails.geometry.location);
    assertNotNull(placeDetails.geometry.location.lat);
    assertEquals(-33.866611, placeDetails.geometry.location.lat, 0.001);
    assertNotNull(placeDetails.geometry.location.lng);
    assertEquals(151.195832, placeDetails.geometry.location.lng, 0.001);

    // URLs
    assertNotNull(placeDetails.icon);
    assertEquals(
        new URI("https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png"),
        placeDetails.icon.toURI());
    assertNotNull(placeDetails.url);
    assertEquals(new URI("https://maps.google.com/?cid=10281119596374313554"),
        placeDetails.url.toURI());
    assertNotNull(placeDetails.website);
    assertEquals(new URI("https://www.google.com.au/about/careers/locations/sydney/"),
        placeDetails.website.toURI());

    // Name
    assertNotNull(placeDetails.name);
    assertEquals("Google", placeDetails.name);

    // Sydney can be either UTC+10 or UTC+11
    assertTrue(placeDetails.utcOffset == 600 || placeDetails.utcOffset == 660);

    // Photos
    {
      assertNotNull(placeDetails.photos);
      Photo photo = placeDetails.photos[0];
      assertNotNull(photo);
      assertNotNull(photo.photoReference);
      assertNotNull(photo.height);
      assertNotNull(photo.width);
      assertNotNull(photo.htmlAttributions);
      assertNotNull(photo.htmlAttributions[0]);
    }

    // Reviews
    {
      assertNotNull(placeDetails.reviews);
      PlaceDetails.Review review = placeDetails.reviews[0];
      assertNotNull(review);
      assertNotNull(review.authorName);
      assertNotNull(review.authorUrl);
      assertNotNull(review.language);
      assertNotNull(review.rating);
    }
    // Place ID
    assertNotNull(placeDetails.placeId);
    assertEquals(GOOGLE_SYDNEY, placeDetails.placeId);
    assertNotNull(placeDetails.scope);
    assertEquals(PlaceIdScope.GOOGLE, placeDetails.scope);
    assertNotNull(placeDetails.types);
    assertNotNull(placeDetails.rating);
  }

  @Test
  public void testTextSearch() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiTextSearch);
    PlacesSearchResponse response = PlacesApi.textSearchQuery(sc.context, "Google Sydney").await();

    sc.assertParamValue("Google Sydney", "query");

    assertEquals(1, response.results.length);
    PlacesSearchResult result = response.results[0];
    assertEquals("5, 48 Pirrama Rd, Pyrmont NSW 2009, Australia", result.formattedAddress);
    assertEquals("ChIJN1t_tDeuEmsRUsoyG83frY4", result.placeId);
  }

  @Test
  public void testPhoto() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiPhoto);
    PlaceDetails placeDetails = PlacesApi.placeDetails(sc.context, GOOGLE_SYDNEY).await();

    sc.assertParamValue("ChIJN1t_tDeuEmsRUsoyG83frY4", "placeid");

    assertEquals(10,placeDetails.photos.length);
    assertEquals("CmRaAAAA-N3w5YTMXWautuDW7IZgX9knz_2fNyyUpCWpvYdVEVb8RurBiisMKvr7AFxMW8dsu2yakYoqjW-IYSFk2cylXVM_c50cCxfm7MlgjPErFxumlcW1bLNOe--SwLYmWlvkEhDxjz75xRqim-CkVlwFyp7sGhTs1fE02MZ6GQcc-TugrepSaeWapA",
        placeDetails.photos[0].photoReference);
    assertEquals(1365, placeDetails.photos[0].height);
    assertEquals(2048, placeDetails.photos[0].width);
  }

  @Test
  public void testPizzaInNewYorkPagination() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiPizzaInNewYork);
    PlacesSearchResponse response = PlacesApi
        .textSearchQuery(sc.context, "Pizza in New York")
        .await();

    sc.assertParamValue("Pizza in New York", "query");

    assertEquals(20, response.results.length);
    assertEquals("CvQB6AAAAPQLwX6KjvGbOw81Y7aYVhXRlHR8M60aCRXFDM9eyflac4BjE5MaNxTj_1T429x3H2kzBd-ztTFXCSu1CPh3kY44Gu0gmL-xfnArnPE9-BgfqXTpgzGPZNeCltB7m341y4LnU-NE2omFPoDWIrOPIyHnyi05Qol9eP2wKW7XPUhMlHvyl9MeVgZ8COBZKvCdENHbhBD1MN1lWlada6A9GPFj06cCp1aqRGW6v98-IHcIcM9RcfMcS4dLAFm6TsgLq4tpeU6E1kSzhrvDiLMBXdJYFlI0qJmytd2wS3vD0t3zKgU6Im_mY-IJL7AwAqhugBIQ8k0X_n6TnacL9BExELBaixoUo8nPOwWm0Nx02haufF2dY0VL-tg",
        response.nextPageToken);
  }

  @Test
  public void testPlaceDetailsInFrench() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiDetailsInFrench);
    PlaceDetails details = PlacesApi
        .placeDetails(sc.context, "ChIJ442GNENu5kcRGYUrvgqHw88")
        .language("fr")
        .await();

    sc.assertParamValue("ChIJ442GNENu5kcRGYUrvgqHw88", "placeid");
    sc.assertParamValue("fr", "language");

    assertEquals("ChIJ442GNENu5kcRGYUrvgqHw88", details.placeId);
    assertEquals("35 Rue du Chevalier de la Barre, 75018 Paris, France", details.formattedAddress);
    assertEquals("Sacré-Cœur", details.name);
  }

  @Test
  public void testNearbySearchRequestByKeyword() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiNearbySearchRequestByKeyword);
    PlacesSearchResponse response = PlacesApi.nearbySearchQuery(sc.context, SYDNEY)
        .radius(10000)
        .keyword("pub")
        .await();

    sc.assertParamValue("10000", "radius");
    sc.assertParamValue("pub", "keyword");
    sc.assertParamValue(SYDNEY.toUrlValue(), "location");

    assertEquals(20, response.results.length);
  }

  @Test
  public void testNearbySearchRequestByName() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiNearbySearchRequestByName);
    PlacesSearchResponse response = PlacesApi
        .nearbySearchQuery(sc.context, SYDNEY)
        .radius(10000)
        .name("Sydney Town Hall")
        .await();

    sc.assertParamValue("Sydney Town Hall", "name");
    sc.assertParamValue(SYDNEY.toUrlValue(), "location");
    sc.assertParamValue("10000", "radius");

    assertEquals("Sydney Town Hall", response.results[0].name);
  }

  @Test
  public void testNearbySearchRequestByType() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiNearbySearchRequestByType);
    PlacesSearchResponse response = PlacesApi
        .nearbySearchQuery(sc.context, SYDNEY)
        .radius(10000)
        .type(PlaceType.BAR)
        .await();

    sc.assertParamValue(SYDNEY.toUrlValue(), "location");
    sc.assertParamValue("10000", "radius");
    sc.assertParamValue(PlaceType.BAR.toUrlValue(), "type");

    assertEquals(20, response.results.length);
  }

  @Test
  public void testRadarSearchRequestByKeyword() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiRadarSearchRequestByKeyword);
    PlacesSearchResponse response = PlacesApi
        .radarSearchQuery(sc.context, SYDNEY, 10000)
        .keyword("pub")
        .await();

    sc.assertParamValue(SYDNEY.toUrlValue(), "location");
    sc.assertParamValue("10000", "radius");
    sc.assertParamValue("pub", "keyword");

    assertTrue(100 < response.results.length);
  }

  @Test
  public void testRadarSearchRequestByName() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiRadarSearchRequestByName);
    PlacesSearchResponse response = PlacesApi
        .radarSearchQuery(sc.context, SYDNEY, 10000)
        .name("Sydney Town Hall")
        .await();

    sc.assertParamValue("Sydney Town Hall", "name");
    sc.assertParamValue("10000", "radius");
    sc.assertParamValue(SYDNEY.toUrlValue(), "location");

    assertEquals("ChIJhSxoJzyuEmsR9gBDBR09ZrE", response.results[0].placeId);
    assertEquals(-33.8731575, response.results[0].geometry.location.lat, 0.001);
    assertEquals(151.2061157, response.results[0].geometry.location.lng, 0.001);
    assertEquals(125, response.results.length);
  }

  @Test
  public void testRadarSearchRequestByType() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiRadarSearchRequestByType);
    PlacesSearchResponse response = PlacesApi
        .radarSearchQuery(sc.context, SYDNEY, 10000)
        .type(PlaceType.BAR)
        .await();

    sc.assertParamValue(SYDNEY.toUrlValue(), "location");
    sc.assertParamValue(PlaceType.BAR.toUrlValue(), "type");
    sc.assertParamValue("10000", "radius");

    assertEquals(197, response.results.length);
  }

  @Test
  public void testPlaceAutocomplete() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiPlaceAutocomplete);
    AutocompletePrediction[] predictions = PlacesApi
        .placeAutocomplete(sc.context, "Sydney Town Ha")
        .await();

    sc.assertParamValue("Sydney Town Ha", "input");

    assertEquals(5, predictions.length);
    assertTrue(predictions[0].description.contains("Town Hall"));
  }

  @Test
  public void testPlaceAutocompleteWithType() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiPlaceAutocompleteWithType);
    AutocompletePrediction[] predictions = PlacesApi.placeAutocomplete(sc.context, "po")
            .components(ComponentFilter.country("nz"))
            .type(PlaceAutocompleteType.REGIONS)
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

  @Test
  public void testKitaWard() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext(placesApiKitaWard);
    String query = "Kita Ward, Kyoto, Kyoto Prefecture, Japan";
    PlacesSearchResponse response = PlacesApi
        .textSearchQuery(sc.context, query)
        .await();

    sc.assertParamValue(query, "query");

    assertEquals("Kita Ward, Kyoto, Kyoto Prefecture, Japan", response.results[0].formattedAddress);
    assertTrue(Arrays.asList(response.results[0].types).contains("ward"));
  }
}
