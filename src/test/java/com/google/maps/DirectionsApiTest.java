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

import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.errors.NotFoundException;
import com.google.maps.model.AddressType;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodedWaypointStatus;
import com.google.maps.model.LatLng;
import com.google.maps.model.TrafficModel;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TransitRoutingPreference;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Category(LargeTests.class)
public class DirectionsApiTest extends AuthenticatedTest {

  private GeoApiContext context;

  public DirectionsApiTest(GeoApiContext context) {
    this.context = context
        .setQueryRateLimit(3)
        .setConnectTimeout(1, TimeUnit.SECONDS)
        .setReadTimeout(1, TimeUnit.SECONDS)
        .setWriteTimeout(1, TimeUnit.SECONDS);
  }

  @Test
  public void testGetDirections() throws Exception {
    DirectionsResult result = DirectionsApi.getDirections(context, "Sydney, AU",
        "Melbourne, AU").await();
    assertNotNull(result.routes);
    assertNotNull(result.routes[0]);
    assertThat(result.routes[0].overviewPolyline.decodePath().size(), not(0));
    assertTrue(result.routes[0].legs[0].startAddress.startsWith("Sydney NSW"));
    assertTrue(result.routes[0].legs[0].endAddress.startsWith("Melbourne VIC"));
  }

  @Test
  public void testBuilder() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .mode(TravelMode.BICYCLING)
        .avoid(RouteRestriction.HIGHWAYS, RouteRestriction.TOLLS, RouteRestriction.FERRIES)
        .units(Unit.METRIC)
        .region("au")
        .origin("Sydney")
        .destination("Melbourne").await();

    assertNotNull(result.routes);
    assertNotNull(result.routes[0]);
  }

  @Test
  public void testTravelModeRoundTrip() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .mode(TravelMode.WALKING)
        .origin("483 George St, Sydney NSW 2000, Australia")
        .destination("182 Church St, Parramatta NSW 2150, Australia").await();

    assertNotNull(result.routes);
    assertNotNull(result.routes[0]);
    assertEquals(TravelMode.WALKING, result.routes[0].legs[0].steps[0].travelMode);
  }

  @Test
  public void testResponseTimesArePopulatedCorrectly() throws Exception {
    DateTime now = new DateTime();
    DirectionsResult result = DirectionsApi.newRequest(context)
        .mode(TravelMode.TRANSIT)
        .origin("483 George St, Sydney NSW 2000, Australia")
        .destination("182 Church St, Parramatta NSW 2150, Australia")
        .departureTime(now)
        .await();

    assertNotNull(result.routes);
    assertNotNull(result.routes[0]);
    assertNotNull(result.routes[0].legs);
    assertNotNull(result.routes[0].legs[0]);
    assertNotNull(result.routes[0].legs[0].arrivalTime);
    assertNotNull(result.routes[0].legs[0].departureTime);
  }

  /**
   * A simple query from Toronto to Montreal.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal}
   */
  @Test
  public void testTorontoToMontreal() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Toronto")
        .destination("Montreal").await();

    assertNotNull(result.routes);
  }

  /**
   * Going from Toronto to Montreal by bicycle, avoiding highways.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&avoid=highways&mode=bicycling}
   */
  @Test
  public void testTorontoToMontrealByBicycleAvoidingHighways() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Toronto")
        .destination("Montreal")
        .avoid(RouteRestriction.HIGHWAYS)
        .mode(TravelMode.BICYCLING)
        .await();

    assertNotNull(result.routes);
  }

  /**
   * Brooklyn to Queens by public transport.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Brooklyn&destination=Queens&departure_time=1343641500&mode=transit}
   */
  @Test
  public void testBrooklynToQueensByTransit() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Brooklyn")
        .destination("Queens")
        .departureTime(new DateTime(1343641500))
        .mode(TravelMode.TRANSIT)
        .await();

    assertNotNull(result.routes);
  }

  /**
   * Boston to Concord, via Charlestown and Lexington.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|Lexington,MA}
   */
  @Test
  public void testBostonToConcordViaCharlestownAndLexignton() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Boston,MA")
        .destination("Concord,MA")
        .waypoints("Charlestown,MA", "Lexington,MA")
        .await();

    assertNotNull(result.routes);
  }

  /**
   * Boston to Concord, via Charlestown and Lexington, but using exact latitude and longitude coordinates for the waypoints.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=42.379322,-71.063384|42.444303,-71.229087}
   */
  @Test
  public void testBostonToConcordViaCharlestownAndLexigntonLatLng() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Boston,MA")
        .destination("Concord,MA")
        .waypoints(new LatLng(42.379322, -71.063384), new LatLng(42.444303, -71.229087))
        .await();

    assertNotNull(result.routes);
  }

  /**
   * Toledo to Madrid, in Spain. This showcases region biasing results.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Toledo&destination=Madrid&region=es}
   */
  @Test
  public void testToledoToMadridInSpain() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Toledo")
        .destination("Madrid")
        .region("es")
        .await();

    assertNotNull(result.routes);
  }

  /**
   * This is the same query above, without region biasing. It returns no routes.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Toledo&destination=Madrid}
   */
  @Test
  public void testToledoToMadridNotSpain() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Toledo")
        .destination("Madrid")
        .await();

    assertNotNull(result.routes);
    assertEquals(0, result.routes.length);
  }

  /**
   * Test the language parameter.
   */
  @Test
  public void testLanguageParameter() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Toledo")
        .destination("Madrid")
        .region("es")
        .language("es")
        .await();

    assertNotNull(result.routes);
  }

  /**
   * Tests the {@code traffic_model} and {@code duration_in_traffic} parameters.
   */
  @Test
  public void testTrafficModel() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("48 Pirrama Road, Pyrmont NSW 2009")
        .destination("182 Church St, Parramatta NSW 2150")
        .mode(TravelMode.DRIVING)
        .departureTime(new DateTime().plus(Duration.standardMinutes(2)))
        .trafficModel(TrafficModel.PESSIMISTIC)
        .await();

    assertNotNull(result);
    assertTrue(result.routes.length > 0);
    assertTrue(result.routes[0].legs.length > 0);
    assertNotNull(result.routes[0].legs[0].durationInTraffic);
  }

  /**
   * Test transit without arrival or departure times specified.
   */
  @Test
  public void testTransitWithoutSpecifyingTime() throws Exception {
    DirectionsApi.newRequest(context)
        .origin("Fisherman's Wharf, San Francisco")
        .destination("Union Square, San Francisco")
        .mode(TravelMode.TRANSIT)
        .await();

    // Since this test may run at different times-of-day, it's entirely valid to return zero
    // routes, but the main thing to catch is that no exception is thrown.
  }

  /**
   * Test the extended transit parameters: mode and routing preference.
   */
  @Test
  public void testTransitParams() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Fisherman's Wharf, San Francisco")
        .destination("Union Square, San Francisco")
        .mode(TravelMode.TRANSIT)
        .transitMode(TransitMode.BUS, TransitMode.TRAM)
        .transitRoutingPreference(TransitRoutingPreference.LESS_WALKING)
        .await();

    assertTrue(result.routes.length > 0);
  }

  @Test(expected = NotFoundException.class)
  public void testNotFound() throws Exception {
    DirectionsApi.getDirections(context, "fksjdhgf", "faldfdaf").await();
  }

  /**
   * Test GeocodedWaypoints results.
   */
  @Test
  public void testGeocodedWaypoints() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("48 Pirrama Rd, Pyrmont NSW")
        .destination("Airport Dr, Sydney NSW")
        .mode(TravelMode.DRIVING)
        .await();

    assertNotNull(result.geocodedWaypoints);
    assertEquals(2, result.geocodedWaypoints.length);
    assertEquals(GeocodedWaypointStatus.OK, result.geocodedWaypoints[0].geocoderStatus);
    assertEquals(GeocodedWaypointStatus.OK, result.geocodedWaypoints[1].geocoderStatus);
    assertEquals(AddressType.ROUTE, result.geocodedWaypoints[1].types[0]);

  }

  /**
   * Tests that calling optimizeWaypoints before waypoints works and results in reordered waypoints.
   */
  @Test
  public void testOptimizeWaypointsCallOrder1() {
    List<LatLng> waypoints = getOptimizationWaypoints();
    DirectionsApiRequest request = DirectionsApi.newRequest(context)
        .origin(waypoints.get(0))
        .destination(waypoints.get(1))
        .departureTime(Instant.now())
        .optimizeWaypoints(true)
        .waypoints(waypoints.subList(2, waypoints.size()).toArray(new LatLng[0]));
    DirectionsResult result = request.awaitIgnoreError();
    assertWaypointsOptimized(result);
  }

  /**
   * Tests that calling optimizeWaypoints after waypoints works and results in reordered waypoints.
   */
  @Test
  public void testOptimizeWaypointsCallOrder2() {
    List<LatLng> waypoints = getOptimizationWaypoints();
    DirectionsApiRequest request = DirectionsApi.newRequest(context)
        .origin(waypoints.get(0))
        .destination(waypoints.get(1))
        .departureTime(Instant.now())
        .waypoints(waypoints.subList(2, waypoints.size()).toArray(new LatLng[0]))
        .optimizeWaypoints(true);
    DirectionsResult result = request.awaitIgnoreError();
    assertWaypointsOptimized(result);
  }

  private void assertWaypointsOptimized(DirectionsResult result) {
    int[] waypointOrder = result.routes[0].waypointOrder;
    assertNotNull(waypointOrder);
    assertTrue(waypointOrder.length > 0);
    for (int i = 0; i < waypointOrder.length; i++) {
      if (i != waypointOrder[i]) {
        return;
      }
    }
    fail("Waypoints do not appear to have been reordered.");
  }

  /**
   * Coordinates in Mexico City. Waypoints are out of order, so when optimized their order should change.
   */
  private List<LatLng> getOptimizationWaypoints() {
    List<LatLng> waypoints = new ArrayList<LatLng>();
    waypoints.add(new LatLng(19.431676,-99.133999));
    waypoints.add(new LatLng(19.427915,-99.138939));
    waypoints.add(new LatLng(19.435436,-99.139145));
    waypoints.add(new LatLng(19.396436,-99.157176));
    waypoints.add(new LatLng(19.427705,-99.198858));
    waypoints.add(new LatLng(19.425869,-99.160716));
    return waypoints;
  }

}
