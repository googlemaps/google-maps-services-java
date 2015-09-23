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
import com.google.maps.model.PlaceDetails;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Category(LargeTests.class)
public class PlacesApiIntegrationTest extends KeyOnlyAuthenticatedTest {
  public static final String GOOGLE_SYDNEY = "ChIJN1t_tDeuEmsRUsoyG83frY4";

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
    assertNotNull(placeDetails.openingHours.periods);
    assertNotNull(placeDetails.openingHours.weekdayText);
    assertNotNull(placeDetails.utcOffset);

    // Sydney varies between GMT+10 and GMT+11
    assertTrue(placeDetails.utcOffset >= 600);
    assertTrue(placeDetails.utcOffset <= 660);

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
    assertNotNull(placeDetails.reviews[0].authorUrl);
    assertNotNull(placeDetails.reviews[0].language);
    assertNotNull(placeDetails.reviews[0].rating);
    assertNotNull(placeDetails.reviews[0].aspects);
    assertNotNull(placeDetails.reviews[0].aspects[0]);
    assertNotNull(placeDetails.reviews[0].aspects[0].rating);
    assertNotNull(placeDetails.reviews[0].aspects[0].type);

    // Place ID
    assertNotNull(placeDetails.placeId);
    assertEquals(placeDetails.placeId, GOOGLE_SYDNEY);
    assertNotNull(placeDetails.scope);
    assertEquals(placeDetails.scope, PlaceDetails.PlaceIdScope.GOOGLE);
    assertNotNull(placeDetails.types);
    assertEquals(placeDetails.types[0], "establishment");
    assertNotNull(placeDetails.rating);
    assertEquals(placeDetails.rating, 4.4, 1.0);
    assertNotNull(placeDetails.userRatingsTotal);
    assertTrue(placeDetails.userRatingsTotal > 95);
  }
}
