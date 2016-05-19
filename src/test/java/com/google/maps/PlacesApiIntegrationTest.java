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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.LatLng;
import com.google.maps.model.Photo;
import com.google.maps.model.PhotoResult;
import com.google.maps.model.PlaceAutocompleteType;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceIdScope;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

@Category(LargeTests.class)
public class PlacesApiIntegrationTest extends KeyOnlyAuthenticatedTest {
  public static final String GOOGLE_SYDNEY = "ChIJN1t_tDeuEmsRUsoyG83frY4";
  public static final LatLng SYDNEY = new LatLng(-33.8650, 151.2094);
  public static final long TWO_SECONDS = 2 * 1000;

  public PlacesApiIntegrationTest(GeoApiContext context) {
    this.context = context
        .setConnectTimeout(2, TimeUnit.SECONDS)
        .setReadTimeout(2, TimeUnit.SECONDS)
        .setWriteTimeout(2, TimeUnit.SECONDS);
  }

  private GeoApiContext context;

  @Test
  public void testPlaceDetailsLookupGoogleSydney() throws Exception {
    PlaceDetails placeDetails = PlacesApi.placeDetails(context, GOOGLE_SYDNEY).await();

    assertNotNull(placeDetails);

    // Address
    assertNotNull(placeDetails.addressComponents);
    assertNotNull(placeDetails.formattedAddress);
    assertEquals("5, 48 Pirrama Rd, Pyrmont NSW 2009, Australia", placeDetails.formattedAddress);
    assertNotNull(placeDetails.vicinity);
    assertEquals("5 48 Pirrama Road, Pyrmont", placeDetails.vicinity);

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
      assertNotNull(review.aspects);
      PlaceDetails.Review.AspectRating aspect = review.aspects[0];
      assertNotNull(aspect);
      assertNotNull(aspect.rating);
      assertNotNull(aspect.type);
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
    PlacesSearchResponse response = PlacesApi.textSearchQuery(context, "Google Sydney").await();

    assertNotNull(response);

    assertNotNull(response.results);
    assertEquals(1, response.results.length);
    {
      PlacesSearchResult result = response.results[0];
      assertNotNull(result);
      assertNotNull(result.formattedAddress);
      assertEquals("5, 48 Pirrama Rd, Pyrmont NSW 2009, Australia", result.formattedAddress);
      assertNotNull(result.placeId);
      assertEquals("ChIJN1t_tDeuEmsRUsoyG83frY4", result.placeId);
    }

  }

  @Test
  public void testPhoto() throws Exception {
    PlaceDetails placeDetails = PlacesApi.placeDetails(context, GOOGLE_SYDNEY).await();

    assertNotNull(placeDetails);
    assertNotNull(placeDetails.photos);
    assertTrue(placeDetails.photos.length > 0);
    assertNotNull(placeDetails.photos[0].photoReference);

    String photoReference = placeDetails.photos[0].photoReference;
    int width = placeDetails.photos[0].width;

    PhotoResult photoResult = PlacesApi.photo(context, photoReference).maxWidth(width).await();
    assertNotNull(photoResult);

    // Assert that the image data represents a real image by parsing it with javax.imageio.
    BufferedImage image = ImageIO.read(new ByteArrayInputStream(photoResult.imageData));
    assertNotNull(image);
  }

  @Test
  public void testPizzaInNewYorkPagination() throws Exception {
    PlacesSearchResponse response = PlacesApi.textSearchQuery(context, "Pizza in New York").await();
    assertNotNull(response);
    assertNotNull(response.results);
    assertEquals(20, response.results.length);
    assertNotNull(response.nextPageToken);

    // The returned page token is not valid for a couple of seconds.
    try {
      Thread.sleep(3 * 1000); // 3 seconds
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }

    PlacesSearchResponse response2 = PlacesApi.textSearchNextPage(context, response.nextPageToken).await();
    assertNotNull(response2);
    assertNotNull(response2.results);
    assertEquals(20, response2.results.length);
    assertNotNull(response2.nextPageToken);

  }

  @Test
  public void testPlaceDetailsInFrench() throws Exception {
    PlaceDetails details = PlacesApi.placeDetails(context, "ChIJ442GNENu5kcRGYUrvgqHw88").language("fr").await();
    assertNotNull(details);
    assertEquals("ChIJ442GNENu5kcRGYUrvgqHw88", details.placeId);
    assertEquals("35 Rue du Chevalier de la Barre, 75018 Paris, France", details.formattedAddress);
    assertEquals("Sacré-Cœur", details.name);
  }

  @Test
  public void testNearbySearchRequestByKeyword() throws Exception {
    PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, SYDNEY)
        .radius(10000).keyword("pub").await();
    assertNotNull(response);
    assertNotNull(response.results);
    assertEquals(20, response.results.length);
  }

  @Test
  public void testNearbySearchRequestByName() throws Exception {
    PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, SYDNEY)
        .radius(10000).name("Sydney Town Hall").await();
    assertNotNull(response);
    assertNotNull(response.results);
    assertEquals("Sydney Town Hall", response.results[0].name);
  }

  @Test
  public void testNearbySearchRequestByType() throws Exception {
    PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, SYDNEY)
        .radius(10000).type(PlaceType.BAR).await();
    assertNotNull(response);
    assertNotNull(response.results);
    assertEquals(20, response.results.length);
  }

  @Test
  public void testNearbySearchRequestNextPage() throws Exception {
    PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, SYDNEY)
        .radius(10000).type(PlaceType.BAR).await();
    assertNotNull(response);
    assertNotNull(response.results);
    assertEquals(20, response.results.length);
    assertNotNull(response.nextPageToken);

    Thread.sleep(TWO_SECONDS);

    PlacesSearchResponse response2 = PlacesApi.nearbySearchNextPage(context, response.nextPageToken).await();
    assertNotNull(response2);
    assertNotNull(response2.results);
    assertEquals(20, response2.results.length);
    assertNotNull(response2.nextPageToken);
  }

// Flaky test.
//  @Test
//  public void testRadarSearchRequestByKeyword() throws Exception {
//    PlacesSearchResponse response = PlacesApi.radarSearchQuery(context, SYDNEY, 10000)
//        .keyword("pub").await();
//    assertNotNull(response);
//    assertNotNull(response.results);
//    assertTrue(100 < response.results.length);
//  }

  @Test
  public void testRadarSearchRequestByName() throws Exception {
    PlacesSearchResponse response = PlacesApi.radarSearchQuery(context, SYDNEY, 10000)
        .name("Sydney Town Hall").await();
    assertNotNull(response);
    assertNotNull(response.results);
    String placeId = response.results[0].placeId;
    assertNotNull(placeId);

    PlaceDetails placeDetails = PlacesApi.placeDetails(context, placeId).await();
    assertNotNull(placeDetails);
    assertEquals("Sydney Town Hall", placeDetails.name);
  }

  @Test
  public void testRadarSearchRequestByType() throws Exception {
    PlacesSearchResponse response = PlacesApi.radarSearchQuery(context, SYDNEY, 10000)
        .type(PlaceType.BAR).await();
    assertNotNull(response);
    assertNotNull(response.results);
    assertEquals(200, response.results.length);
  }

  @Test
  public void testPlaceAutocomplete() throws Exception {
    AutocompletePrediction[] predictions = PlacesApi.placeAutocomplete(context, "Sydney Town Ha")
        .await();
    assertNotNull(predictions);
    assertTrue(predictions.length > 0);
    assertTrue(predictions[0].description.contains("Town Hall"));
  }

  @Test
  public void testPlaceAutocompleteWithType() throws Exception {
    AutocompletePrediction[] predictions = PlacesApi.placeAutocomplete(context, "po")
            .components(ComponentFilter.country("nz"))
            .type(PlaceAutocompleteType.REGIONS)
            .await();
    assertNotNull(predictions);
    assertTrue(predictions.length > 0);
    for (int i = 0; i < predictions.length; i++) {
      for (int j = 0; j < predictions[i].types.length; j++) {
        assertFalse(predictions[i].types[j].equals("route"));
        assertFalse(predictions[i].types[j].equals("establishment"));
      }
    }
  }

  @Test
  public void testKitaWard() throws Exception {
    PlacesSearchResponse response = PlacesApi.textSearchQuery(context,
        "Kita Ward, Kyoto, Kyoto Prefecture, Japan").await();
    assertNotNull(response);
    assertNotNull(response.results[0]);
    assertEquals("Kita Ward, Kyoto, Kyoto Prefecture, Japan", response.results[0].formattedAddress);
    assertTrue(Arrays.asList(response.results[0].types).contains("ward"));
  }
}
