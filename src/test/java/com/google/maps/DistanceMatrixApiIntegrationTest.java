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
import static org.junit.Assert.fail;

import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.TrafficModel;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TransitRoutingPreference;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.concurrent.TimeUnit;

@Category(LargeTests.class)
public class DistanceMatrixApiIntegrationTest extends AuthenticatedTest {

  private GeoApiContext context;

  public DistanceMatrixApiIntegrationTest(GeoApiContext context) {
    this.context = context
        .setConnectTimeout(1, TimeUnit.SECONDS)
        .setReadTimeout(1, TimeUnit.SECONDS)
        .setWriteTimeout(1, TimeUnit.SECONDS);
  }

  @Test
  public void testGetDistanceMatrixWithBasicStringParams() throws Exception {
    String[] origins = new String[]{
        "Perth, Australia", "Sydney, Australia", "Melbourne, Australia",
        "Adelaide, Australia", "Brisbane, Australia", "Darwin, Australia",
        "Hobart, Australia", "Canberra, Australia"
    };
    String[] destinations = new String[]{
        "Uluru, Australia", "Kakadu, Australia", "Blue Mountains, Australia",
        "Bungle Bungles, Australia", "The Pinnacles, Australia"
    };
    DistanceMatrix matrix =
        DistanceMatrixApi.getDistanceMatrix(context, origins, destinations).await();

    // Rows length will match the number of origin elements, regardless of whether they're routable.
    assertEquals(8, matrix.rows.length);
    assertEquals(5, matrix.rows[0].elements.length);
    assertEquals(DistanceMatrixElementStatus.OK, matrix.rows[0].elements[0].status);
  }

  @Test
  public void testNewRequestWithAllPossibleParams() throws Exception {
    String[] origins = new String[]{
        "Perth, Australia", "Sydney, Australia", "Melbourne, Australia",
        "Adelaide, Australia", "Brisbane, Australia", "Darwin, Australia",
        "Hobart, Australia", "Canberra, Australia"
    };
    String[] destinations = new String[]{
        "Uluru, Australia", "Kakadu, Australia", "Blue Mountains, Australia",
        "Bungle Bungles, Australia", "The Pinnacles, Australia"
    };

    DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
        .origins(origins)
        .destinations(destinations)
        .mode(TravelMode.DRIVING)
        .language("en-AU")
        .avoid(RouteRestriction.TOLLS)
        .units(Unit.IMPERIAL)
        .departureTime(new DateTime().plusMinutes(2))  // this is ignored when an API key is used
        .await();

    assertEquals(8, matrix.rows.length);
    assertEquals(5, matrix.rows[0].elements.length);
    assertTrue(matrix.rows[0].elements[0].distance.humanReadable.endsWith("mi"));
  }

  /**
   * Test the language parameter.
   *
   * <p>Sample request:
   * <a href="http://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Victoria+BC&mode=bicycling&language=fr-FR">
   * origins: Vancouver BC|Seattle, destinations: San Francisco|Victoria BC, mode: bicycling,
   * language: french</a>.
   */
  @Test
  public void testLanguageParameter() throws Exception {
    DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
        .origins("Vancouver BC", "Seattle")
        .destinations("San Francisco", "Victoria BC")
        .mode(TravelMode.BICYCLING)
        .language("fr-FR")
        .await();

    assertNotNull(matrix);
  }

  /**
   * Test transit without arrival or departure times specified.
   */
  @Test
  public void testTransitWithoutSpecifyingTime() throws Exception {
    DistanceMatrixApi.newRequest(context)
        .origins("Fisherman's Wharf, San Francisco", "Union Square, San Francisco")
        .destinations("Mikkeller Bar, San Francisco", "Moscone Center, San Francisco")
        .mode(TravelMode.TRANSIT)
        .await();

    // Since this test may run at different times-of-day, it's entirely valid to return zero
    // routes, but the main thing to catch is that no exception is thrown.
  }


  /**
   * Test duration in traffic with traffic model set.
   */
  @Test
  public void testDurationInTrafficWithTrafficModel() throws Exception {
    final long ONE_HOUR_MILLIS = 60 * 60 * 1000;
    DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context)
            .origins("Fisherman's Wharf, San Francisco")
            .destinations("San Francisco International Airport, San Francisco, CA")
            .mode(TravelMode.DRIVING)
            .trafficModel(TrafficModel.PESSIMISTIC)
            .departureTime(new DateTime(System.currentTimeMillis() + ONE_HOUR_MILLIS));
    assertTrue("pessimistic".equals(request.params().get("traffic_model")));
    DistanceMatrix matrix = request.await();
    assertNotNull(matrix);
    assertEquals(DistanceMatrixElementStatus.OK, matrix.rows[0].elements[0].status);
    assertTrue(0 < matrix.rows[0].elements[0].durationInTraffic.inSeconds);
  }
}
