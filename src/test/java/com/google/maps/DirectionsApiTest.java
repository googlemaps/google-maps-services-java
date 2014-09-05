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

import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.errors.NotFoundException;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import org.joda.time.DateTime;
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
    DirectionsRoute[] routes = DirectionsApi.getDirections(context, "Sydney", "Melbourne").await();
    assertNotNull(routes);
    assertNotNull(routes[0]);
    assertThat(routes[0].overviewPolyline.decodePath().size(), not(0));
    assertEquals("Sydney NSW, Australia", routes[0].legs[0].startAddress);
    assertEquals("Melbourne VIC, Australia", routes[0].legs[0].endAddress);
  }

  @Test
  public void testBuilder() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .mode(TravelMode.BICYCLING)
        .avoid(RouteRestriction.HIGHWAYS, RouteRestriction.TOLLS, RouteRestriction.FERRIES)
        .units(Unit.METRIC)
        .region("us")
        .origin("Sydney")
        .destination("Melbourne").await();

    assertNotNull(routes);
    assertNotNull(routes[0]);
  }

  @Test
  public void testTravelModeRoundTrip() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .mode(TravelMode.BICYCLING)
        .origin("Town Hall, Sydney")
        .destination("Parramatta, NSW").await();

    assertNotNull(routes);
    assertNotNull(routes[0]);
    assertEquals(TravelMode.BICYCLING, routes[0].legs[0].steps[0].travelMode);
  }

  @Test
  public void testResponseTimesArePopulatedCorrectly() throws Exception {
    DateTime now = new DateTime();
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .mode(TravelMode.TRANSIT)
        .origin("Town Hall, Sydney")
        .destination("Parramatta, NSW")
        .departureTime(now)
        .await();

    assertNotNull(routes);
    assertNotNull(routes[0]);
    assertNotNull(routes[0].legs);
    assertNotNull(routes[0].legs[0]);
    assertNotNull(routes[0].legs[0].arrivalTime);
    assertNotNull(routes[0].legs[0].departureTime);
  }

  /**
   * A simple query from Toronto to Montreal.
   * {@url http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal}
   */
  @Test
  public void testTorontoToMontreal() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .origin("Toronto")
        .destination("Montreal").await();

    assertNotNull(routes);
  }

  /**
   * Going from Toronto to Montreal by bicycle, avoiding highways.
   * {@url http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&avoid=highways&mode=bicycling}
   */
  @Test
  public void testTorontoToMontrealByBicycleAvoidingHighways() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .origin("Toronto")
        .destination("Montreal")
        .avoid(RouteRestriction.HIGHWAYS)
        .mode(TravelMode.BICYCLING)
        .await();

    assertNotNull(routes);
  }

  /**
   * Brooklyn to Queens by public transport.
   * {@url http://maps.googleapis.com/maps/api/directions/json?origin=Brooklyn&destination=Queens&departure_time=1343641500&mode=transit}
   */
  @Test
  public void testBrooklynToQueensByTransit() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .origin("Brooklyn")
        .destination("Queens")
        .departureTime(new DateTime(1343641500))
        .mode(TravelMode.TRANSIT)
        .await();

    assertNotNull(routes);
  }

  /**
   * Boston to Concord, via Charlestown and Lexington.
   * {@url http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|Lexington,MA
   */
  @Test
  public void testBostonToConcordViaCharlestownAndLexignton() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .origin("Boston,MA")
        .destination("Concord,MA")
        .waypoints("Charlestown,MA", "Lexington,MA")
        .await();

    assertNotNull(routes);
  }

  /**
   * A wine tour around Adelaide in South Australia. This shows off how to get Directions Web
   * Service API to find the shortest path amongst a set of way points.
   * {@url http://maps.googleapis.com/maps/api/directions/json?origin=Adelaide,SA&destination=Adelaide,SA&waypoints=optimize:true|Barossa+Valley,SA|Clare,SA|Connawarra,SA|McLaren+Vale,SA}
   */
  @Test
  public void testAdelaideWineTour() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .origin("Adelaide,SA")
        .destination("Adelaide,SA")
        .optimizeWaypoints(true)
        .waypoints("Barossa Valley, SA", "Clare, SA", "Connawarra, SA",
        "McLaren Vale, SA")
        .await();

    assertNotNull(routes);
    assertEquals(1, routes.length);

    // optimize:true returns the waypoint_order of the optimized route.
    // "waypoint_order": [ 1, 0, 2, 3 ]
    assertNotNull(routes[0].waypointOrder);
    assertEquals(1, routes[0].waypointOrder[0]);
    assertEquals(0, routes[0].waypointOrder[1]);
    assertEquals(2, routes[0].waypointOrder[2]);
    assertEquals(3, routes[0].waypointOrder[3]);
  }

  /**
   * Toledo to Madrid, in Spain. This showcases region biasing results.
   * {@url http://maps.googleapis.com/maps/api/directions/json?origin=Toledo&destination=Madrid&region=es}
   */
  @Test
  public void testToledoToMadridInSpain() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .origin("Toledo")
        .destination("Madrid")
        .region("es")
        .await();

    assertNotNull(routes);
  }

  /**
   * This is the same query above, without region biasing. It returns no routes.
   * {@url http://maps.googleapis.com/maps/api/directions/json?origin=Toledo&destination=Madrid}
   */
  @Test
  public void testToledoToMadridNotSpain() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .origin("Toledo")
        .destination("Madrid")
        .await();

    assertNotNull(routes);
    assertEquals(0, routes.length);
  }

  /**
   * Test the language parameter.
   */
  @Test
  public void testLanguageParameter() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .origin("Toledo")
        .destination("Madrid")
        .region("es")
        .language("es")
        .await();

    assertNotNull(routes);
  }

  /**
   * Testing the alternatives param.
   */
  @Test
  public void testAlternatives() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.newRequest(context)
        .origin("Sydney Town Hall")
        .destination("Parramatta Town Hall")
        .alternatives(true)
        .await();

    assertNotNull(routes);
    assertTrue(routes.length > 1);
  }

  @Test(expected = NotFoundException.class)
  public void testNotFound() throws Exception {
    DirectionsRoute[] routes = DirectionsApi.getDirections(context, "fksjdhgf", "faldfdaf").await();
  }
}
