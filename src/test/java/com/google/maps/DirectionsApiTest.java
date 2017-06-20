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
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Category(MediumTests.class)
public class DirectionsApiTest {

  private static class ServerContext {
    private final MockWebServer server;
    public final GeoApiContext context;
    private List<NameValuePair> params = null;

    ServerContext(MockWebServer server, GeoApiContext context) {
      this.server = server;
      this.context = context;
    }

    private List<NameValuePair> parseQueryParamsFromRequestLine(String requestLine)
        throws URISyntaxException {
      // Extract the URL part from the HTTP request line
      String[] chunks = requestLine.split("\\s");
      String url = chunks[1];

      return URLEncodedUtils.parse(new URI(url), Charset.forName("UTF-8"));
    }

    private List<NameValuePair> actualParams() throws InterruptedException, URISyntaxException {
      return parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    }

    void assertParamValue(String expectedValue, String paramName)
        throws URISyntaxException, InterruptedException {
      if (this.params == null) {
        this.params = this.actualParams();
      }
      boolean paramFound = false;
      for (NameValuePair pair : params) {
        if (pair.getName().equals(paramName)) {
          paramFound = true;
          assertEquals(expectedValue, pair.getValue());
        }
      }
      assertTrue(paramFound);
    }

    @Override
    protected void finalize() throws Throwable {
      server.shutdown();
    }
  }

  private ServerContext createServerContext(String responseBody) throws IOException {
    GeoApiContext context = new GeoApiContext().setApiKey("AIzaFakeKey");
    MockResponse response = new MockResponse();
    response.setBody(responseBody);
    MockWebServer server = new MockWebServer();
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    return new ServerContext(server, context);
  }

  @Test
  public void testGetDirections() throws Exception {
    ServerContext sc = createServerContext(
        "{\n" +
        "    \"geocoded_waypoints\": [{\n" +
        "            \"geocoder_status\": \"OK\",\n" +
        "            \"place_id\": \"ChIJP3Sa8ziYEmsRUKgyFmh9AQM\",\n" +
        "            \"types\": [\"colloquial_area\", \"locality\", \"political\"]\n" +
        "        },\n" +
        "        {\n" +
        "            \"geocoder_status\": \"OK\",\n" +
        "            \"place_id\": \"ChIJ90260rVG1moRkM2MIXVWBAQ\",\n" +
        "            \"types\": [\"colloquial_area\", \"locality\", \"political\"]\n" +
        "        }\n" +
        "    ],\n" +
        "    \"routes\": [{\n" +
        "        \"legs\": [{\n" +
        "            \"end_address\": \"Melbourne VIC, Australia\",\n" +
        "            \"start_address\": \"Sydney NSW, Australia\"\n" +
        "        }],\n" +
        "        \"overview_polyline\": {\n" +
        "            \"points\": \"f`vmE{`|y[gIqq@daAZxtEtYr~AwLztA~jCxAb{Ey_@djFpZxsQjh@ztJqv@tpJvnAryCzmA~_@ruCraC|fErxDfnC|RvlAd|B~`DvuD|oEroApsEvfChfCvUnzGpzHthFpeDz_GnzExaDriG~{E~`FhfF~vCbhBh_B~xAl}Dpx@tuElpAz|HxxEpcMbbD|a@jmBviBp`GfkFlpD~iAt|BjuCfyAbbC`JphGxzApuE|~@hdFh|CptDzhEp_HrnCleJna@fnFzYznIkw@ffDnOnvIhnEfiC`W|kLhhAlbKrqBbgCbMxeDie@b|MzpApyHbj@ryHoLriHaqAlgGczBv`IlHdtEiO`aGtwAvuFjhCxeHdFvqCbrAluJakAnoJgyB|uDrAh`G{tBd_E_rA~dEa\\\\noGth@trGnvA|sEvmDrmCbPnwB_hApxClRbhEqu@xsGmNh_GxyA~sLLbcT|~AnoD~pCtwBd_CliExaAjxEbuD|lIbnEgQl_E~{D`m@`mClvDbr@thDtCnwA|xArgDhPjlC|uDbiA~`FfpCfbDhOvzHzm@`pE|rDziHzHj{GnqDj`AteBh|CrjDtnD`}Fb}GtxCprJ|nAvr@xlDgmA~dBoq@nqBzi@x|DreCnuAj|BloDldChoEtpEdsCr`AhzBl_B~iEbXveBj}AxkIt~L`uC|}CjXd}Cp~C|rJlc@|zBdtAnvAdfCoDrmCfgB`xC~}A~oAn_ElhCpVnuB|gFv`CjoAnrDd}AfzC~qDpEbsGb~@zdBzAb`Nj{FhFvzEtqAhiFprBnnGzfHfoBdqB`u@xuAcVlyCqi@ziHgqBvaE{NzbHhvBx~IfiA|_AflCtoDjjBnyAxaA~eDrYl_IxiA|_MhbDz~CjkJlhGt`H~sFpuDkKflD~F`hCvfChlBtvI~q@b}Dn{Ff}D`{AxsCt_@zpFjsChqJvkAptGl}AxcAbcCrxA`mDvuIn~@zdFii@ngDjxCfdJpA`sGx~Hn~WlpGxkGf|EjyHbtBpxBv~AbHd|@pzCmTrxFxpB~dEvxEhxKv_DjaPtzB~lHngF~}G|jEf_ExgBlpAznC`dE`UfpErSzqAbmBha@|`DtsA~sIoLpvBljApwCfLjsHttBjrFd`@lbDx_AffJhEpdBnb@~cAxjBrtD~p@`sD}S~fDtxC~rEd`Cd`GlwAxhFztBfiJf[tkBytAf_@qcCbdBouAvkC`DzpDdw@vmByM~WjvJmCnuBpfAdzA~eDxAbHoeEbgBmjAxiEq_@jtCqzB~l@aS\"\n" +
        "        },\n" +
        "        \"summary\": \"M31 and National Highway M31\"\n" +
        "    }],\n" +
        "    \"status\": \"OK\"\n" +
        "}");

    DirectionsResult result = DirectionsApi.getDirections(sc.context, "Sydney, AU",
        "Melbourne, AU")
        .await();

    assertNotNull(result);
    assertNotNull(result.geocodedWaypoints);
    assertEquals(2, result.geocodedWaypoints.length);
    assertEquals("ChIJP3Sa8ziYEmsRUKgyFmh9AQM", result.geocodedWaypoints[0].placeId);
    assertEquals("ChIJ90260rVG1moRkM2MIXVWBAQ", result.geocodedWaypoints[1].placeId);
    assertNotNull(result.routes);
    assertEquals(1, result.routes.length);
    assertNotNull(result.routes[0]);
    assertEquals("M31 and National Highway M31", result.routes[0].summary);
    assertThat(result.routes[0].overviewPolyline.decodePath().size(), not(0));
    assertEquals(1, result.routes[0].legs.length);
    assertEquals("Melbourne VIC, Australia", result.routes[0].legs[0].endAddress);
    assertEquals("Sydney NSW, Australia",result.routes[0].legs[0].startAddress);

    sc.assertParamValue("Sydney, AU", "origin");
    sc.assertParamValue("Melbourne, AU", "destination");
  }

  @Test
  public void testBuilder() throws Exception {
    ServerContext sc = createServerContext("{\n" +
        "    \"routes\": [{\n" +
        "        \"legs\": [{\n" +
        "            \"end_address\": \"Melbourne VIC, Australia\",\n" +
        "            \"start_address\": \"Sydney NSW, Australia\"\n" +
        "        }],\n" +
        "        \"overview_polyline\": {\n" +
        "            \"points\": \"f`vmE{`|y[gIqq@daAZxtEtYr~AwLztA~jCxAb{Ey_@djFpZxsQjh@ztJqv@tpJvnAryCzmA~_@ruCraC|fErxDfnC|RvlAd|B~`DvuD|oEroApsEvfChfCvUnzGpzHthFpeDz_GnzExaDriG~{E~`FhfF~vCbhBh_B~xAl}Dpx@tuElpAz|HxxEpcMbbD|a@jmBviBp`GfkFlpD~iAt|BjuCfyAbbC`JphGxzApuE|~@hdFh|CptDzhEp_HrnCleJna@fnFzYznIkw@ffDnOnvIhnEfiC`W|kLhhAlbKrqBbgCbMxeDie@b|MzpApyHbj@ryHoLriHaqAlgGczBv`IlHdtEiO`aGtwAvuFjhCxeHdFvqCbrAluJakAnoJgyB|uDrAh`G{tBd_E_rA~dEa\\\\noGth@trGnvA|sEvmDrmCbPnwB_hApxClRbhEqu@xsGmNh_GxyA~sLLbcT|~AnoD~pCtwBd_CliExaAjxEbuD|lIbnEgQl_E~{D`m@`mClvDbr@thDtCnwA|xArgDhPjlC|uDbiA~`FfpCfbDhOvzHzm@`pE|rDziHzHj{GnqDj`AteBh|CrjDtnD`}Fb}GtxCprJ|nAvr@xlDgmA~dBoq@nqBzi@x|DreCnuAj|BloDldChoEtpEdsCr`AhzBl_B~iEbXveBj}AxkIt~L`uC|}CjXd}Cp~C|rJlc@|zBdtAnvAdfCoDrmCfgB`xC~}A~oAn_ElhCpVnuB|gFv`CjoAnrDd}AfzC~qDpEbsGb~@zdBzAb`Nj{FhFvzEtqAhiFprBnnGzfHfoBdqB`u@xuAcVlyCqi@ziHgqBvaE{NzbHhvBx~IfiA|_AflCtoDjjBnyAxaA~eDrYl_IxiA|_MhbDz~CjkJlhGt`H~sFpuDkKflD~F`hCvfChlBtvI~q@b}Dn{Ff}D`{AxsCt_@zpFjsChqJvkAptGl}AxcAbcCrxA`mDvuIn~@zdFii@ngDjxCfdJpA`sGx~Hn~WlpGxkGf|EjyHbtBpxBv~AbHd|@pzCmTrxFxpB~dEvxEhxKv_DjaPtzB~lHngF~}G|jEf_ExgBlpAznC`dE`UfpErSzqAbmBha@|`DtsA~sIoLpvBljApwCfLjsHttBjrFd`@lbDx_AffJhEpdBnb@~cAxjBrtD~p@`sD}S~fDtxC~rEd`Cd`GlwAxhFztBfiJf[tkBytAf_@qcCbdBouAvkC`DzpDdw@vmByM~WjvJmCnuBpfAdzA~eDxAbHoeEbgBmjAxiEq_@jtCqzB~l@aS\"\n" +
        "        },\n" +
        "        \"summary\": \"M31 and National Highway M31\"\n" +
        "    }],\n" +
        "    \"status\": \"OK\"\n" +
        "}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .mode(TravelMode.BICYCLING)
        .avoid(DirectionsApi.RouteRestriction.HIGHWAYS, DirectionsApi.RouteRestriction.TOLLS, DirectionsApi.RouteRestriction.FERRIES)
        .units(Unit.METRIC)
        .region("au")
        .origin("Sydney")
        .destination("Melbourne").await();

    assertNotNull(result.routes);
    assertEquals(1, result.routes.length);

    sc.assertParamValue(TravelMode.BICYCLING.toUrlValue(), "mode");
    sc.assertParamValue(DirectionsApi.RouteRestriction.HIGHWAYS.toUrlValue() + "|" +
        DirectionsApi.RouteRestriction.TOLLS.toUrlValue() + "|" +
        DirectionsApi.RouteRestriction.FERRIES.toUrlValue(), "avoid");
    sc.assertParamValue(Unit.METRIC.toUrlValue(), "units");
    sc.assertParamValue("au", "region");
    sc.assertParamValue("Sydney", "origin");
    sc.assertParamValue("Melbourne", "destination");
  }

  /**
   * A simple query from Toronto to Montreal.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal}
   */
  @Test
  public void testTorontoToMontreal() throws Exception {
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .origin("Toronto")
        .destination("Montreal").await();

    sc.assertParamValue("Toronto", "origin");
    sc.assertParamValue("Montreal", "destination");
  }

  /**
   * Going from Toronto to Montreal by bicycle, avoiding highways.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&avoid=highways&mode=bicycling}
   */
  @Test
  public void testTorontoToMontrealByBicycleAvoidingHighways() throws Exception {
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
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

  /**
   * Brooklyn to Queens by public transport.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Brooklyn&destination=Queens&departure_time=1343641500&mode=transit}
   */
  @Test
  public void testBrooklynToQueensByTransit() throws Exception {
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .origin("Brooklyn")
        .destination("Queens")
        .mode(TravelMode.TRANSIT)
        .await();

    sc.assertParamValue("Brooklyn", "origin");
    sc.assertParamValue("Queens", "destination");
    sc.assertParamValue(TravelMode.TRANSIT.toUrlValue(), "mode");
  }

  /**
   * Boston to Concord, via Charlestown and Lexington.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|Lexington,MA}
   */
  @Test
  public void testBostonToConcordViaCharlestownAndLexignton() throws Exception {
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .origin("Boston,MA")
        .destination("Concord,MA")
        .waypoints("Charlestown,MA", "Lexington,MA")
        .await();

    sc.assertParamValue("Boston,MA", "origin");
    sc.assertParamValue("Concord,MA", "destination");
    sc.assertParamValue("Charlestown,MA|Lexington,MA", "waypoints");
  }

  /**
   * Boston to Concord, via Charlestown and Lexington, but using exact latitude and longitude coordinates for the waypoints.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=42.379322,-71.063384|42.444303,-71.229087}
   */
  @Test
  public void testBostonToConcordViaCharlestownAndLexigntonLatLng() throws Exception {
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .origin("Boston,MA")
        .destination("Concord,MA")
        .waypoints(new LatLng(42.379322, -71.063384), new LatLng(42.444303, -71.229087))
        .await();

    sc.assertParamValue("Boston,MA", "origin");
    sc.assertParamValue("Concord,MA", "destination");
    sc.assertParamValue("42.37932200,-71.06338400|42.44430300,-71.22908700", "waypoints");
  }

  /**
   * Toledo to Madrid, in Spain. This showcases region biasing results.
   *
   * {@code http://maps.googleapis.com/maps/api/directions/json?origin=Toledo&destination=Madrid&region=es}
   */
  @Test
  public void testToledoToMadridInSpain() throws Exception {
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .origin("Toledo")
        .destination("Madrid")
        .region("es")
        .await();

    sc.assertParamValue("Toledo", "origin");
    sc.assertParamValue("Madrid", "destination");
    sc.assertParamValue("es", "region");
  }

  /**
   * Test the language parameter.
   */
  @Test
  public void testLanguageParameter() throws Exception {
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .origin("Toledo")
        .destination("Madrid")
        .region("es")
        .language("es")
        .await();

    sc.assertParamValue("Toledo", "origin");
    sc.assertParamValue("Madrid", "destination");
    sc.assertParamValue("es", "region");
    sc.assertParamValue("es", "language");
  }

  /**
   * Tests the {@code traffic_model} and {@code duration_in_traffic} parameters.
   */
  @Test
  public void testTrafficModel() throws Exception {
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .origin("48 Pirrama Road, Pyrmont NSW 2009")
        .destination("182 Church St, Parramatta NSW 2150")
        .mode(TravelMode.DRIVING)
        .departureTime(new DateTime().plus(Duration.standardMinutes(2)))
        .trafficModel(TrafficModel.PESSIMISTIC)
        .await();

    sc.assertParamValue("48 Pirrama Road, Pyrmont NSW 2009", "origin");
    sc.assertParamValue("182 Church St, Parramatta NSW 2150", "destination");
    sc.assertParamValue(TravelMode.DRIVING.toUrlValue(), "mode");
    sc.assertParamValue(TrafficModel.PESSIMISTIC.toUrlValue(), "traffic_model");
  }

  /**
   * Test transit without arrival or departure times specified.
   */
  @Test
  public void testTransitWithoutSpecifyingTime() throws Exception {
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    DirectionsApi.newRequest(sc.context)
        .origin("Fisherman's Wharf, San Francisco")
        .destination("Union Square, San Francisco")
        .mode(TravelMode.TRANSIT)
        .await();

    sc.assertParamValue("Fisherman's Wharf, San Francisco", "origin");
    sc.assertParamValue("Union Square, San Francisco", "destination");
    sc.assertParamValue(TravelMode.TRANSIT.toUrlValue(), "mode");
  }

  /**
   * Test the extended transit parameters: mode and routing preference.
   */
  @Test
  public void testTransitParams() throws Exception {
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .origin("Fisherman's Wharf, San Francisco")
        .destination("Union Square, San Francisco")
        .mode(TravelMode.TRANSIT)
        .transitMode(TransitMode.BUS, TransitMode.TRAM)
        .transitRoutingPreference(TransitRoutingPreference.LESS_WALKING)
        .await();

    sc.assertParamValue("Fisherman's Wharf, San Francisco", "origin");
    sc.assertParamValue("Union Square, San Francisco", "destination");
    sc.assertParamValue(TravelMode.TRANSIT.toUrlValue(), "mode");
    sc.assertParamValue(TransitMode.BUS.toUrlValue()+"|"+TransitMode.TRAM.toUrlValue(), "transit_mode");
    sc.assertParamValue(TransitRoutingPreference.LESS_WALKING.toUrlValue(), "transit_routing_preference");
  }


  @Test
  public void testTravelModeWalking() throws Exception {
    ServerContext sc = createServerContext("{\n" +
        "    \"routes\": [{\n" +
        "    }],\n" +
        "    \"status\": \"OK\"\n" +
        "}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .mode(TravelMode.WALKING)
        .origin("483 George St, Sydney NSW 2000, Australia")
        .destination("182 Church St, Parramatta NSW 2150, Australia").await();

    assertNotNull(result.routes);
    assertNotNull(result.routes[0]);

    sc.assertParamValue(TravelMode.WALKING.toUrlValue(), "mode");
    sc.assertParamValue("483 George St, Sydney NSW 2000, Australia", "origin");
    sc.assertParamValue("182 Church St, Parramatta NSW 2150, Australia", "destination");
  }

  @Test
  public void testResponseTimesArePopulatedCorrectly() throws Exception {
    ServerContext sc = createServerContext("\n"
        + "{\n"
        + "   \"routes\" : [\n"
        + "      {\n"
        + "         \"legs\" : [\n"
        + "            {\n"
        + "               \"arrival_time\" : {\n"
        + "                  \"text\" : \"1:54pm\",\n"
        + "                  \"time_zone\" : \"Australia/Sydney\",\n"
        + "                  \"value\" : 1497930863\n"
        + "               },\n"
        + "               \"departure_time\" : {\n"
        + "                  \"text\" : \"1:21pm\",\n"
        + "                  \"time_zone\" : \"Australia/Sydney\",\n"
        + "                  \"value\" : 1497928860\n"
        + "               },\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"24.8 km\",\n"
        + "                  \"value\" : 24785\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"33 mins\",\n"
        + "                  \"value\" : 2003\n"
        + "               },\n"
        + "               \"end_address\" : \"182 Church St, Parramatta NSW 2150, Australia\",\n"
        + "               \"start_address\" : \"483 George St, Sydney NSW 2000, Australia\"\n"
        + "            }\n"
        + "         ],\n"
        + "         \"overview_polyline\" : {\n"
        + "            \"points\" : \"b}vmEir{y[APbAHpAJjDh@Z?^Cp@Mj@Uf@]fAeAv@wAz@cAt@i@x@c@`Bk@t@SnCUpAPb@@zB\\\\v@LvDh@`BZtBf@nC`AbCtAd@VM^EJ`@V~AlAbDnD~EbHr@zAl@x@r@jApAdBx@|@p@n@|C~BnBzA|FdFdAjAjBdCjAnB~A|CZt@lAxChBnFv@zCZdBf@jDd@hCXnAfBtHZtAn@pCz@fDhBhGb@bB`@tB\\\\rBjAnFh@zD\\\\tDHvF@hJMrDE|DSbDWbBy@`DqAnDoBrDi@dA_@z@s@|Bc@bByBjIeA~DShAUlBM`BEbDDtCVxE`@bFf@pFD|CO`DW~BaAfFa@fB[|BY`DCpBD~BRrCj@pGNtBFjBE~CU|Da@nEc@xDe@zBoAnFcAzEsAzFg@lBm@rCiA~E}AdGgAhD}@bDg@dCUjBa@bDMpDM`GChACvABz@ClACdAItBQjCYfCStA]`BaB|GqBbIUt@s@`Dw@nDg@lBqAfFu@lDu@bDiBnH]jAy@bCs@fB{AhDgAdCc@hAuBrFm@hA}DxF}B~Dy@tA_BzBwBrCcBxBkAbBy@pAq@pAk@jAs@lB[bA_@dB{@jEc@bCS|AcA`Ji@hD_AxISnA_@~Ae@zAi@tA_ApBu@pAgAvAcAfAaB|Ae@f@eBvAo@n@e@j@}@~@y@xAo@tA}@jBi@x@}@fA}A~As@~@{@rAkC|EaJvPe@|@_@z@u@pBuAxDu@`C}@`Eo@nCoAhGsB`Ka@|BQxAOfBIzAEhB?lBDfBFzAPpBf@`GThFJpD?bBKpDSjEGlAYhCgAjFYzBw@lHOvBO`DIpDExD?nCFxIRnEf@xDv@pFLjBDl@DlEEjGKvLO|OBhACdBEvBMdCO|AO~@Kn@c@pB_@pAs@pBe@rAc@~@i@z@_AvAoA~AwAxAgA|@oBpAeC|Ao@XaAj@gAd@}@j@gC~AoHrEkGzD}JjG}JlGsEbD{@n@oB`BaC`CeBhBeAdAqEtE_BfBmEzEyGnHqAvAuCvCqBrBsBlB}DbEuBhCmClD{B`Du@fAkAvBcArBgB~Cc@~@[l@o@`AsC~EqBxD_GvKeBzCsAjCiDtGgA`CaCrEg@x@S^w@v@iBtAgAr@w@^sClAaAXqAb@{Bn@sATo@LQB}ALaDHoBEC??@a@AqBIgAMwE{@sASm@C{CI_A@eCVw@PsAb@m@`@_Aj@mBrAsBlB]^kA`Bg@bAUj@OK\\\\dAYl@[p@Un@Gb@EZI@a@BG?KEEGSNi@Vy@NW?S@\"\n"
        + "         }\n"
        + "      }\n"
        + "   ],\n"
        + "   \"status\" : \"OK\"\n"
        + "}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
        .mode(TravelMode.TRANSIT)
        .origin("483 George St, Sydney NSW 2000, Australia")
        .destination("182 Church St, Parramatta NSW 2150, Australia")
        .await();

    assertEquals(1, result.routes.length);
    assertEquals(1, result.routes[0].legs.length);
    DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm a");
    assertEquals("1:54 pm", fmt.print(result.routes[0].legs[0].arrivalTime).toLowerCase());
    assertEquals("1:21 pm", fmt.print(result.routes[0].legs[0].departureTime).toLowerCase());

    sc.assertParamValue(TravelMode.TRANSIT.toUrlValue(), "mode");
    sc.assertParamValue("483 George St, Sydney NSW 2000, Australia", "origin");
    sc.assertParamValue("182 Church St, Parramatta NSW 2150, Australia", "destination");
  }

  @Test(expected = NotFoundException.class)
  public void testNotFound() throws Exception {
    ServerContext sc = createServerContext("{\n"
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
        + "}");
    DirectionsApi.getDirections(sc.context, "fksjdhgf", "faldfdaf").await();
  }

  /**
   * Test GeocodedWaypoints results.
   */
  @Test
  public void testGeocodedWaypoints() throws Exception {
    ServerContext sc = createServerContext("{"
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
        + "}");
    DirectionsResult result = DirectionsApi.newRequest(sc.context)
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
   * Tests that calling optimizeWaypoints works in either order.
   */
  @Test
  public void testOptimizeWaypoints() throws Exception {
    List<LatLng> waypoints = getOptimizationWaypoints();
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    LatLng origin = waypoints.get(0);
    LatLng destination = waypoints.get(1);
    DirectionsApi.newRequest(sc.context)
        .origin(origin)
        .destination(destination)
        .departureTime(Instant.now())
        .optimizeWaypoints(true)
        .waypoints(waypoints.subList(2, waypoints.size()).toArray(new LatLng[0]))
        .await();

    sc.assertParamValue(origin.toUrlValue(), "origin");
    sc.assertParamValue(destination.toUrlValue(), "destination");
    sc.assertParamValue("optimize:true|" +
        waypoints.get(2).toUrlValue() + "|" +
        waypoints.get(3).toUrlValue() + "|" +
        waypoints.get(4).toUrlValue() + "|" +
        waypoints.get(5).toUrlValue() , "waypoints");

  }

  /**
   * Tests that calling optimizeWaypoints works in either order.
   */
  @Test
  public void testOptimizeWaypointsAlternateCallOrder() throws Exception {
    List<LatLng> waypoints = getOptimizationWaypoints();
    ServerContext sc = createServerContext("{\"routes\": [{}],\"status\": \"OK\"}");
    LatLng origin = waypoints.get(0);
    LatLng destination = waypoints.get(1);
    DirectionsApi.newRequest(sc.context)
        .origin(origin)
        .destination(destination)
        .departureTime(Instant.now())
        .waypoints(waypoints.subList(2, waypoints.size()).toArray(new LatLng[0]))
        .optimizeWaypoints(true)
        .await();

    sc.assertParamValue(origin.toUrlValue(), "origin");
    sc.assertParamValue(destination.toUrlValue(), "destination");
    sc.assertParamValue("optimize:true|" +
        waypoints.get(2).toUrlValue() + "|" +
        waypoints.get(3).toUrlValue() + "|" +
        waypoints.get(4).toUrlValue() + "|" +
        waypoints.get(5).toUrlValue() , "waypoints");

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
