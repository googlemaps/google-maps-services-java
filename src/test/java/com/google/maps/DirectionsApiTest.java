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

import com.google.maps.model.DirectionsResult;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Category(MediumTests.class)
public class DirectionsApiTest {

  private GeoApiContext context = new GeoApiContext().setApiKey("AIzaFakeKey");

  @Test
  public void testGetDirections() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody(
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
    MockWebServer server = new MockWebServer();
    server.enqueue(response);
    server.play();
    context.setBaseUrlForTesting("http://127.0.0.1:" + server.getPort());

    DirectionsResult result = DirectionsApi.getDirections(context, "Sydney, AU",
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


    List<NameValuePair> actualParams =
        parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue("Sydney, AU", "origin", actualParams);
    assertParamValue("Melbourne, AU", "destination", actualParams);

    server.shutdown();
  }

  private void assertParamValue(String expectedValue, String paramName, List<NameValuePair> params)
      throws Exception {
    boolean paramFound = false;
    for (NameValuePair pair : params) {
      if (pair.getName().equals(paramName)) {
        paramFound = true;
        assertEquals(expectedValue, pair.getValue());
      }
    }
    assertTrue(paramFound);
  }


  private List<NameValuePair> parseQueryParamsFromRequestLine(String requestLine) throws Exception {
    // Extract the URL part from the HTTP request line
    String[] chunks = requestLine.split("\\s");
    String url = chunks[1];

    return URLEncodedUtils.parse(new URI(url), "UTF-8");
  }

}
