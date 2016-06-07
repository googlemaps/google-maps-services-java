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

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.errors.NotFoundException;
import com.google.maps.model.AddressType;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.GeocodedWaypointStatus;
import com.google.maps.model.TrafficModel;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TransitRoutingPreference;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.concurrent.TimeUnit;

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
    assertEquals("Sydney NSW, Australia", result.routes[0].legs[0].startAddress);
    assertEquals("Melbourne VIC, Australia", result.routes[0].legs[0].endAddress);
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
        .mode(TravelMode.BICYCLING)
        .origin("Town Hall, Sydney")
        .destination("Parramatta, NSW").await();

    assertNotNull(result.routes);
    assertNotNull(result.routes[0]);
    assertEquals(TravelMode.BICYCLING, result.routes[0].legs[0].steps[0].travelMode);
  }

  @Test
  public void testResponseTimesArePopulatedCorrectly() throws Exception {
    DateTime now = new DateTime();
    DirectionsResult result = DirectionsApi.newRequest(context)
        .mode(TravelMode.TRANSIT)
        .origin("Town Hall, Sydney")
        .destination("Parramatta, NSW")
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
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|Lexington,MA
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
   * A wine tour around Adelaide in South Australia. This shows off how to get Directions Web
   * Service API to find the shortest path amongst a set of way points.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Adelaide,SA&destination=Adelaide,SA&waypoints=optimize:true|Barossa+Valley,SA|Clare,SA|Connawarra,SA|McLaren+Vale,SA}
   */
  @Test
  public void testAdelaideWineTour() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Adelaide,SA")
        .destination("Adelaide,SA")
        .optimizeWaypoints(true)
        .waypoints("Barossa Valley, SA", "Clare, SA", "Connawarra, SA",
            "McLaren Vale, SA")
        .await();

    assertNotNull(result.routes);
    assertEquals(1, result.routes.length);

    // optimize:true returns the waypoint_order of the optimized route.
    // "waypoint_order": [ 1, 0, 2, 3 ]
    assertNotNull(result.routes[0].waypointOrder);
    assertEquals(1, result.routes[0].waypointOrder[0]);
    assertEquals(0, result.routes[0].waypointOrder[1]);
    assertEquals(2, result.routes[0].waypointOrder[2]);
    assertEquals(3, result.routes[0].waypointOrder[3]);
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
   * Testing the alternatives param.
   */
  @Test
  public void testAlternatives() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Sydney Town Hall")
        .destination("Parramatta Town Hall")
        .alternatives(true)
        .await();

    assertNotNull(result.routes);
    assertTrue(result.routes.length > 1);
  }

  /**
   * Tests the {@code traffic_model} and {@code duration_in_traffic} parameters.
   */
  @Test
  public void testTrafficModel() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("Sydney Town Hall")
        .destination("Parramatta Town Hall")
        .mode(TravelMode.DRIVING)
        .departureTime(new DateTime().plus(Duration.standardMinutes(2)))
        .trafficModel(TrafficModel.PESSIMISTIC)
        .await();

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
    assertEquals(AddressType.PREMISE, result.geocodedWaypoints[0].types[0]);
    assertEquals(GeocodedWaypointStatus.OK, result.geocodedWaypoints[1].geocoderStatus);
    assertEquals(AddressType.ROUTE, result.geocodedWaypoints[1].types[0]);

  }

  /**
   * Test {@code local_icon} for Directions in Paris.
   */
  @Test
  public void testLocalIconInParis() throws Exception {
    DirectionsResult result = DirectionsApi.newRequest(context)
        .origin("paris metro bibliotheque francois mitterrand")
        .destination("paris metro pyramides")
        .mode(TravelMode.TRANSIT)
        .await();
    assertNotNull(result);
    assertEquals("//maps.gstatic.com/mapfiles/transit/iw2/6/fr-paris-metro.png",
        result.routes[0].legs[0].steps[1].transitDetails.line.vehicle.localIcon);
  }

}
