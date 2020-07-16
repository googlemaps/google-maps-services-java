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
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(MediumTests.class)
public class DirectionsApiTest {

  private final String getDirectionsResponse;
  private final String builderResponse;
  private final String responseTimesArePopulatedCorrectly;

  public DirectionsApiTest() {
    getDirectionsResponse = retrieveBody("GetDirectionsResponse.json");
    builderResponse = retrieveBody("DirectionsApiBuilderResponse.json");
    responseTimesArePopulatedCorrectly = retrieveBody("ResponseTimesArePopulatedCorrectly.json");
  }

  @Test
  public void testGetDirections() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(getDirectionsResponse)) {
      DirectionsResult result =
          DirectionsApi.getDirections(sc.context, "Sydney, AU", "Melbourne, AU").await();

      assertNotNull(result);
      assertNotNull(result.toString(), "result.toString() succeeded");
      assertNotNull(result.geocodedWaypoints);
      assertNotNull(Arrays.toString(result.geocodedWaypoints));
      assertEquals(2, result.geocodedWaypoints.length);
      assertEquals("ChIJP3Sa8ziYEmsRUKgyFmh9AQM", result.geocodedWaypoints[0].placeId);
      assertEquals("ChIJ90260rVG1moRkM2MIXVWBAQ", result.geocodedWaypoints[1].placeId);
      assertNotNull(result.routes);
      assertNotNull(Arrays.toString(result.routes));
      assertEquals(1, result.routes.length);
      assertNotNull(result.routes[0]);
      assertEquals("M31 and National Highway M31", result.routes[0].summary);
      assertThat(result.routes[0].overviewPolyline.decodePath().size(), not(0));
      assertEquals(1, result.routes[0].legs.length);
      assertEquals("Melbourne VIC, Australia", result.routes[0].legs[0].endAddress);
      assertEquals("Sydney NSW, Australia", result.routes[0].legs[0].startAddress);

      sc.assertParamValue("Sydney, AU", "origin");
      sc.assertParamValue("Melbourne, AU", "destination");
    }
  }

  @Test
  public void testBuilder() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(builderResponse)) {
      DirectionsResult result =
          DirectionsApi.newRequest(sc.context)
              .mode(TravelMode.BICYCLING)
              .avoid(
                  DirectionsApi.RouteRestriction.HIGHWAYS,
                  DirectionsApi.RouteRestriction.TOLLS,
                  DirectionsApi.RouteRestriction.FERRIES)
              .units(Unit.METRIC)
              .region("au")
              .origin("Sydney")
              .destination("Melbourne")
              .await();

      assertNotNull(result.routes);
      assertEquals(1, result.routes.length);

      sc.assertParamValue(TravelMode.BICYCLING.toUrlValue(), "mode");
      sc.assertParamValue(
          DirectionsApi.RouteRestriction.HIGHWAYS.toUrlValue()
              + "|"
              + DirectionsApi.RouteRestriction.TOLLS.toUrlValue()
              + "|"
              + DirectionsApi.RouteRestriction.FERRIES.toUrlValue(),
          "avoid");
      sc.assertParamValue(Unit.METRIC.toUrlValue(), "units");
      sc.assertParamValue("au", "region");
      sc.assertParamValue("Sydney", "origin");
      sc.assertParamValue("Melbourne", "destination");
    }
  }

  @Test
  public void testResponseTimesArePopulatedCorrectly() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(responseTimesArePopulatedCorrectly)) {
      DirectionsResult result =
          DirectionsApi.newRequest(sc.context)
              .mode(TravelMode.TRANSIT)
              .origin("483 George St, Sydney NSW 2000, Australia")
              .destination("182 Church St, Parramatta NSW 2150, Australia")
              .await();

      assertEquals(1, result.routes.length);
      assertEquals(1, result.routes[0].legs.length);
      DateTimeFormatter fmt = DateTimeFormatter.ofPattern("h:mm a");
      assertEquals("1:54 pm", fmt.format(result.routes[0].legs[0].arrivalTime).toLowerCase());
      assertEquals("1:21 pm", fmt.format(result.routes[0].legs[0].departureTime).toLowerCase());

      sc.assertParamValue(TravelMode.TRANSIT.toUrlValue(), "mode");
      sc.assertParamValue("483 George St, Sydney NSW 2000, Australia", "origin");
      sc.assertParamValue("182 Church St, Parramatta NSW 2150, Australia", "destination");
    }
  }

  /**
   * A simple query from Toronto to Montreal.
   *
   * <p>{@code
   * http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal}
   */
  @Test
  public void testTorontoToMontreal() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsApi.newRequest(sc.context).origin("Toronto").destination("Montreal").await();

      sc.assertParamValue("Toronto", "origin");
      sc.assertParamValue("Montreal", "destination");
    }
  }

  /**
   * Going from Toronto to Montreal by bicycle, avoiding highways.
   *
   * <p>{@code
   * http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&avoid=highways&mode=bicycling}
   */
  @Test
  public void testTorontoToMontrealByBicycleAvoidingHighways() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsApi.newRequest(sc.context)
          .origin("Toronto")
          .destination("Montreal")
          .avoid(DirectionsApi.RouteRestriction.HIGHWAYS)
          .mode(TravelMode.BICYCLING)
          .await();

      sc.assertParamValue("Toronto", "origin");
      sc.assertParamValue("Montreal", "destination");
      sc.assertParamValue(RouteRestriction.HIGHWAYS.toUrlValue(), "avoid");
      sc.assertParamValue(TravelMode.BICYCLING.toUrlValue(), "mode");
    }
  }

  @Test
  public void testSanFranciscoToSeattleByBicycleAvoidingIndoor() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsApi.newRequest(sc.context)
          .origin("San Francisco")
          .destination("Seattle")
          .avoid(RouteRestriction.INDOOR)
          .mode(TravelMode.BICYCLING)
          .await();

      sc.assertParamValue("San Francisco", "origin");
      sc.assertParamValue("Seattle", "destination");
      sc.assertParamValue(RouteRestriction.INDOOR.toUrlValue(), "avoid");
      sc.assertParamValue(TravelMode.BICYCLING.toUrlValue(), "mode");
    }
  }

  /**
   * Brooklyn to Queens by public transport.
   *
   * <p>{@code
   * http://maps.googleapis.com/maps/api/directions/json?origin=Brooklyn&destination=Queens&departure_time=1343641500&mode=transit}
   */
  @Test
  public void testBrooklynToQueensByTransit() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsApi.newRequest(sc.context)
          .origin("Brooklyn")
          .destination("Queens")
          .mode(TravelMode.TRANSIT)
          .await();

      sc.assertParamValue("Brooklyn", "origin");
      sc.assertParamValue("Queens", "destination");
      sc.assertParamValue(TravelMode.TRANSIT.toUrlValue(), "mode");
    }
  }

  /**
   * Boston to Concord, via Charlestown and Lexington.
   *
   * <p>{@code
   * http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|Lexington,MA}
   */
  @Test
  public void testBostonToConcordViaCharlestownAndLexington() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsApi.newRequest(sc.context)
          .origin("Boston,MA")
          .destination("Concord,MA")
          .waypoints("Charlestown,MA", "Lexington,MA")
          .await();

      sc.assertParamValue("Boston,MA", "origin");
      sc.assertParamValue("Concord,MA", "destination");
      sc.assertParamValue("Charlestown,MA|Lexington,MA", "waypoints");
    }
  }

  /**
   * Boston to Concord, via Charlestown and Lexington, using non-stopover waypoints.
   *
   * <p>{@code
   * http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=via:Charlestown,MA|via:Lexington,MA}
   */
  @Test
  public void testBostonToConcordViaCharlestownAndLexingtonNonStopover() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsApi.newRequest(sc.context)
          .origin("Boston,MA")
          .destination("Concord,MA")
          .waypoints(
              new DirectionsApiRequest.Waypoint("Charlestown,MA", false),
              new DirectionsApiRequest.Waypoint("Lexington,MA", false))
          .await();

      sc.assertParamValue("Boston,MA", "origin");
      sc.assertParamValue("Concord,MA", "destination");
      sc.assertParamValue("via:Charlestown,MA|via:Lexington,MA", "waypoints");
    }
  }

  /**
   * Boston to Concord, via Charlestown and Lexington, but using exact latitude and longitude
   * coordinates for the waypoints.
   *
   * <p>{@code
   * http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=42.379322,-71.063384|42.444303,-71.229087}
   */
  @Test
  public void testBostonToConcordViaCharlestownAndLexingtonLatLng() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsApi.newRequest(sc.context)
          .origin("Boston,MA")
          .destination("Concord,MA")
          .waypoints(new LatLng(42.379322, -71.063384), new LatLng(42.444303, -71.229087))
          .await();

      sc.assertParamValue("Boston,MA", "origin");
      sc.assertParamValue("Concord,MA", "destination");
      sc.assertParamValue("42.37932200,-71.06338400|42.44430300,-71.22908700", "waypoints");
    }
  }

  /**
   * Boston to Concord, via Charlestown and Lexington, but using exact latitude and longitude
   * coordinates for the waypoints, using non-stopover waypoints.
   *
   * <p>{@code
   * http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=via:42.379322,-71.063384|via:42.444303,-71.229087}
   */
  @Test
  public void testBostonToConcordViaCharlestownAndLexingtonLatLngNonStopoever() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsApi.newRequest(sc.context)
          .origin("Boston,MA")
          .destination("Concord,MA")
          .waypoints(
              new DirectionsApiRequest.Waypoint(new LatLng(42.379322, -71.063384), false),
              new DirectionsApiRequest.Waypoint(new LatLng(42.444303, -71.229087), false))
          .await();

      sc.assertParamValue("Boston,MA", "origin");
      sc.assertParamValue("Concord,MA", "destination");
      sc.assertParamValue("via:42.37932200,-71.06338400|via:42.44430300,-71.22908700", "waypoints");
    }
  }

  /**
   * Toledo to Madrid, in Spain. This showcases region biasing results.
   *
   * <p>{@code
   * http://maps.googleapis.com/maps/api/directions/json?origin=Toledo&destination=Madrid&region=es}
   */
  @Test
  public void testToledoToMadridInSpain() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsApi.newRequest(sc.context)
          .origin("Toledo")
          .destination("Madrid")
          .region("es")
          .await();

      sc.assertParamValue("Toledo", "origin");
      sc.assertParamValue("Madrid", "destination");
      sc.assertParamValue("es", "region");
    }
  }

  /** Test the language parameter. */
  @Test
  public void testLanguageParameter() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsResult result =
          DirectionsApi.newRequest(sc.context)
              .origin("Toledo")
              .destination("Madrid")
              .region("es")
              .language("es")
              .await();

      sc.assertParamValue("Toledo", "origin");
      sc.assertParamValue("Madrid", "destination");
      sc.assertParamValue("es", "region");
      sc.assertParamValue("es", "language");

      assertNotNull(result.toString());
    }
  }

  /** Tests the {@code traffic_model} and {@code duration_in_traffic} parameters. */
  @Test
  public void testTrafficModel() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsResult result =
          DirectionsApi.newRequest(sc.context)
              .origin("48 Pirrama Road, Pyrmont NSW 2009")
              .destination("182 Church St, Parramatta NSW 2150")
              .mode(TravelMode.DRIVING)
              .departureTime(Instant.now().plus(Duration.ofMinutes(2)))
              .trafficModel(TrafficModel.PESSIMISTIC)
              .await();

      sc.assertParamValue("48 Pirrama Road, Pyrmont NSW 2009", "origin");
      sc.assertParamValue("182 Church St, Parramatta NSW 2150", "destination");
      sc.assertParamValue(TravelMode.DRIVING.toUrlValue(), "mode");
      sc.assertParamValue(TrafficModel.PESSIMISTIC.toUrlValue(), "traffic_model");

      assertNotNull(result.toString());
    }
  }

  /** Test transit without arrival or departure times specified. */
  @Test
  public void testTransitWithoutSpecifyingTime() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsResult result =
          DirectionsApi.newRequest(sc.context)
              .origin("Fisherman's Wharf, San Francisco")
              .destination("Union Square, San Francisco")
              .mode(TravelMode.TRANSIT)
              .await();

      sc.assertParamValue("Fisherman's Wharf, San Francisco", "origin");
      sc.assertParamValue("Union Square, San Francisco", "destination");
      sc.assertParamValue(TravelMode.TRANSIT.toUrlValue(), "mode");

      assertNotNull(result.toString());
    }
  }

  /** Test the extended transit parameters: mode and routing preference. */
  @Test
  public void testTransitParams() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsResult result =
          DirectionsApi.newRequest(sc.context)
              .origin("Fisherman's Wharf, San Francisco")
              .destination("Union Square, San Francisco")
              .mode(TravelMode.TRANSIT)
              .transitMode(TransitMode.BUS, TransitMode.TRAM)
              .transitRoutingPreference(TransitRoutingPreference.LESS_WALKING)
              .await();

      sc.assertParamValue("Fisherman's Wharf, San Francisco", "origin");
      sc.assertParamValue("Union Square, San Francisco", "destination");
      sc.assertParamValue(TravelMode.TRANSIT.toUrlValue(), "mode");
      sc.assertParamValue(
          TransitMode.BUS.toUrlValue() + "|" + TransitMode.TRAM.toUrlValue(), "transit_mode");
      sc.assertParamValue(
          TransitRoutingPreference.LESS_WALKING.toUrlValue(), "transit_routing_preference");

      assertNotNull(result.toString());
    }
  }

  @Test
  public void testTravelModeWalking() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      DirectionsResult result =
          DirectionsApi.newRequest(sc.context)
              .mode(TravelMode.WALKING)
              .origin("483 George St, Sydney NSW 2000, Australia")
              .destination("182 Church St, Parramatta NSW 2150, Australia")
              .await();

      assertNotNull(result.toString());
      assertNotNull(result.routes);
      assertNotNull(result.routes[0]);

      sc.assertParamValue(TravelMode.WALKING.toUrlValue(), "mode");
      sc.assertParamValue("483 George St, Sydney NSW 2000, Australia", "origin");
      sc.assertParamValue("182 Church St, Parramatta NSW 2150, Australia", "destination");

      assertNotNull(result.toString());
    }
  }

  @Test(expected = NotFoundException.class)
  public void testNotFound() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "{\n"
                + "   \"geocoded_waypoints\" : [\n"
                + "      {\n"
                + "         \"geocoder_status\" : \"ZERO_RESULTS\"\n"
                + "      },\n"
                + "      {\n"
                + "         \"geocoder_status\" : \"ZERO_RESULTS\"\n"
                + "      }\n"
                + "   ],\n"
                + "   \"routes\" : [],\n"
                + "   \"status\" : \"NOT_FOUND\"\n"
                + "}")) {
      DirectionsApi.getDirections(sc.context, "fksjdhgf", "faldfdaf").await();
    }
  }

  /** Test GeocodedWaypoints results. */
  @Test
  public void testGeocodedWaypoints() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "{"
                + "   \"geocoded_waypoints\" : [\n"
                + "      {\n"
                + "         \"geocoder_status\" : \"OK\"\n"
                + "      },\n"
                + "      {\n"
                + "         \"geocoder_status\" : \"OK\",\n"
                + "         \"types\" : [\"route\"]\n"
                + "      }\n"
                + "   ],\n"
                + "   \"routes\": [{}],\n"
                + "   \"status\": \"OK\"\n"
                + "}")) {
      DirectionsResult result =
          DirectionsApi.newRequest(sc.context)
              .origin("48 Pirrama Rd, Pyrmont NSW")
              .destination("Airport Dr, Sydney NSW")
              .mode(TravelMode.DRIVING)
              .await();

      assertNotNull(result.toString());
      assertNotNull(result.geocodedWaypoints);
      assertEquals(2, result.geocodedWaypoints.length);
      assertEquals(GeocodedWaypointStatus.OK, result.geocodedWaypoints[0].geocoderStatus);
      assertEquals(GeocodedWaypointStatus.OK, result.geocodedWaypoints[1].geocoderStatus);
      assertEquals(AddressType.ROUTE, result.geocodedWaypoints[1].types[0]);

      assertNotNull(result.toString());
    }
  }

  /** Tests that calling {@code optimizeWaypoints(true)} works in either order. */
  @Test
  public void testOptimizeWaypointsBeforeWaypoints() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      List<LatLng> waypoints = getOptimizationWaypoints();
      LatLng origin = waypoints.get(0);
      LatLng destination = waypoints.get(1);
      DirectionsResult result =
          DirectionsApi.newRequest(sc.context)
              .origin(origin)
              .destination(destination)
              .departureTime(Instant.now())
              .optimizeWaypoints(true)
              .waypoints(waypoints.subList(2, waypoints.size()).toArray(new LatLng[0]))
              .await();

      sc.assertParamValue(origin.toUrlValue(), "origin");
      sc.assertParamValue(destination.toUrlValue(), "destination");
      sc.assertParamValue(
          "optimize:true|"
              + waypoints.get(2).toUrlValue()
              + "|"
              + waypoints.get(3).toUrlValue()
              + "|"
              + waypoints.get(4).toUrlValue()
              + "|"
              + waypoints.get(5).toUrlValue(),
          "waypoints");

      assertNotNull(result.toString());
    }
  }

  /** Tests that calling {@code optimizeWaypoints(true)} works in either order. */
  @Test
  public void testOptimizeWaypointsAfterWaypoints() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("{\"routes\": [{}],\"status\": \"OK\"}")) {
      List<LatLng> waypoints = getOptimizationWaypoints();
      LatLng origin = waypoints.get(0);
      LatLng destination = waypoints.get(1);
      DirectionsResult result =
          DirectionsApi.newRequest(sc.context)
              .origin(origin)
              .destination(destination)
              .departureTime(Instant.now())
              .waypoints(waypoints.subList(2, waypoints.size()).toArray(new LatLng[0]))
              .optimizeWaypoints(true)
              .await();

      sc.assertParamValue(origin.toUrlValue(), "origin");
      sc.assertParamValue(destination.toUrlValue(), "destination");
      sc.assertParamValue(
          "optimize:true|"
              + waypoints.get(2).toUrlValue()
              + "|"
              + waypoints.get(3).toUrlValue()
              + "|"
              + waypoints.get(4).toUrlValue()
              + "|"
              + waypoints.get(5).toUrlValue(),
          "waypoints");

      assertNotNull(result.toString());
    }
  }

  /** Coordinates in Mexico City. */
  private List<LatLng> getOptimizationWaypoints() {
    List<LatLng> waypoints = new ArrayList<>();
    waypoints.add(new LatLng(19.431676, -99.133999));
    waypoints.add(new LatLng(19.427915, -99.138939));
    waypoints.add(new LatLng(19.435436, -99.139145));
    waypoints.add(new LatLng(19.396436, -99.157176));
    waypoints.add(new LatLng(19.427705, -99.198858));
    waypoints.add(new LatLng(19.425869, -99.160716));
    return waypoints;
  }
}
