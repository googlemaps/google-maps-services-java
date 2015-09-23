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

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.maps.model.AddressComponentType;
import com.google.maps.model.PlaceDetails;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Category(LargeTests.class)
public class PlacesApiIntegrationTest extends KeyOnlyAuthenticatedTest {
  private GeoApiContext context;

  public static final String GOOGLE_SYDNEY = "ChIJN1t_tDeuEmsRUsoyG83frY4";

  public PlacesApiIntegrationTest(GeoApiContext context) {
    this.context = context
        .setConnectTimeout(2, TimeUnit.SECONDS)
        .setReadTimeout(2, TimeUnit.SECONDS)
        .setWriteTimeout(2 , TimeUnit.SECONDS);
  }

  @Test
  public void testPlaceDetailsLookupGoogleSydney() throws Exception {
    PlaceDetails placeDetails = PlacesApi.placeDetails(context, GOOGLE_SYDNEY).await();

    assertNotNull(placeDetails);
    assertNotNull(placeDetails.addressComponents);
    assertEquals(placeDetails.addressComponents[1].longName, "48");
    assertEquals(placeDetails.addressComponents[1].types[0], AddressComponentType.STREET_NUMBER);
    assertEquals(placeDetails.addressComponents[2].shortName, "Pirrama Rd");
    assertEquals(placeDetails.addressComponents[2].types[0], AddressComponentType.ROUTE);
    assertNotNull(placeDetails.formattedAddress);
    assertEquals(placeDetails.formattedAddress, "5, 48 Pirrama Rd, Pyrmont NSW 2009, Australia");
    assertNotNull(placeDetails.formattedPhoneNumber);
    assertEquals(placeDetails.formattedPhoneNumber, "(02) 9374 4000");
    assertNotNull(placeDetails.geometry);
    assertNotNull(placeDetails.geometry.location);
    assertNotNull(placeDetails.geometry.location.lat);
    assertEquals(placeDetails.geometry.location.lat, -33.866611, 0.001);
    assertNotNull(placeDetails.geometry.location.lng);
    assertEquals(placeDetails.geometry.location.lng, 151.195832, 0.001);
    assertNotNull(placeDetails.icon);
    assertEquals(placeDetails.icon.toURI(), new URI("https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png"));
    assertNotNull(placeDetails.internationalPhoneNumber);
    assertEquals(placeDetails.internationalPhoneNumber, "+61 2 9374 4000");
    assertNotNull(placeDetails.name);
    assertEquals(placeDetails.name, "Google");

    assertNotNull(placeDetails.openingHours);
    assertNotNull(placeDetails.openingHours.openNow);
    assertNotNull(placeDetails.openingHours.periods);
    assertNotNull(placeDetails.openingHours.weekdayText);
    assertNotNull(placeDetails.photos);
    assertNotNull(placeDetails.photos[0]);
    assertNotNull(placeDetails.photos[0].photoReference);
    assertNotNull(placeDetails.photos[0].height);
    assertNotNull(placeDetails.photos[0].width);
    assertNotNull(placeDetails.photos[0].htmlAttributions);
    assertNotNull(placeDetails.photos[0].htmlAttributions[0]);

    assertNotNull(placeDetails.placeId);
    assertEquals(placeDetails.placeId, GOOGLE_SYDNEY);

  }
}
