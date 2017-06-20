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

import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.LatLng;
import com.google.maps.model.TrafficModel;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(MediumTests.class)
public class DistanceMatrixApiTest {

  @Test
  public void testLatLngOriginDestinations() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}");
    DistanceMatrixApi.newRequest(sc.context)
        .origins(new LatLng(-31.9522, 115.8589),
            new LatLng(-37.8136, 144.9631))
        .destinations(new LatLng(-25.344677, 131.036692),
            new LatLng(-13.092297, 132.394057))
        .awaitIgnoreError();

    sc.assertParamValue("-31.95220000,115.85890000|-37.81360000,144.96310000", "origins");
    sc.assertParamValue("-25.34467700,131.03669200|-13.09229700,132.39405700", "destinations");
  }

  @Test
  public void testGetDistanceMatrixWithBasicStringParams() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext("\n"
        + "{\n"
        + "   \"destination_addresses\" : [\n"
        + "      \"Uluru, Petermann NT 0872, Australia\",\n"
        + "      \"Kakadu NT 0822, Australia\",\n"
        + "      \"Blue Mountains, New South Wales, Australia\",\n"
        + "      \"Purnululu National Park, Western Australia 6770, Australia\",\n"
        + "      \"Pinnacles Drive, Cervantes WA 6511, Australia\"\n"
        + "   ],\n"
        + "   \"origin_addresses\" : [\n"
        + "      \"Perth WA, Australia\",\n"
        + "      \"Sydney NSW, Australia\",\n"
        + "      \"Melbourne VIC, Australia\",\n"
        + "      \"Adelaide SA, Australia\",\n"
        + "      \"Brisbane QLD, Australia\",\n"
        + "      \"Darwin NT, Australia\",\n"
        + "      \"Hobart TAS 7000, Australia\",\n"
        + "      \"Canberra ACT 2601, Australia\"\n"
        + "   ],\n"
        + "   \"rows\" : [\n"
        + "      {\n"
        + "         \"elements\" : [\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,670 km\",\n"
        + "                  \"value\" : 3669839\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 14 hours\",\n"
        + "                  \"value\" : 137846\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"4,173 km\",\n"
        + "                  \"value\" : 4172519\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 20 hours\",\n"
        + "                  \"value\" : 157552\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,820 km\",\n"
        + "                  \"value\" : 3819685\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 16 hours\",\n"
        + "                  \"value\" : 144484\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,112 km\",\n"
        + "                  \"value\" : 3111879\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 8 hours\",\n"
        + "                  \"value\" : 116918\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"194 km\",\n"
        + "                  \"value\" : 193530\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"2 hours 20 mins\",\n"
        + "                  \"value\" : 8428\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            }\n"
        + "         ]\n"
        + "      },\n"
        + "      {\n"
        + "         \"elements\" : [\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"2,835 km\",\n"
        + "                  \"value\" : 2835495\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 6 hours\",\n"
        + "                  \"value\" : 106882\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,996 km\",\n"
        + "                  \"value\" : 3995751\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 20 hours\",\n"
        + "                  \"value\" : 158372\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"129 km\",\n"
        + "                  \"value\" : 129162\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 hour 55 mins\",\n"
        + "                  \"value\" : 6915\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"4,458 km\",\n"
        + "                  \"value\" : 4458286\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"2 days 3 hours\",\n"
        + "                  \"value\" : 182989\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"4,082 km\",\n"
        + "                  \"value\" : 4081644\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 18 hours\",\n"
        + "                  \"value\" : 152261\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            }\n"
        + "         ]\n"
        + "      },\n"
        + "      {\n"
        + "         \"elements\" : [\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"2,320 km\",\n"
        + "                  \"value\" : 2319610\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 1 hour\",\n"
        + "                  \"value\" : 89337\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,776 km\",\n"
        + "                  \"value\" : 3776081\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 17 hours\",\n"
        + "                  \"value\" : 146992\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"857 km\",\n"
        + "                  \"value\" : 856860\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"9 hours 12 mins\",\n"
        + "                  \"value\" : 33138\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"4,239 km\",\n"
        + "                  \"value\" : 4238615\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"2 days 0 hours\",\n"
        + "                  \"value\" : 171609\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,566 km\",\n"
        + "                  \"value\" : 3565759\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 13 hours\",\n"
        + "                  \"value\" : 134716\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            }\n"
        + "         ]\n"
        + "      },\n"
        + "      {\n"
        + "         \"elements\" : [\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"1,595 km\",\n"
        + "                  \"value\" : 1595141\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"17 hours 2 mins\",\n"
        + "                  \"value\" : 61329\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,052 km\",\n"
        + "                  \"value\" : 3051611\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 9 hours\",\n"
        + "                  \"value\" : 118984\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"1,252 km\",\n"
        + "                  \"value\" : 1251530\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"13 hours 36 mins\",\n"
        + "                  \"value\" : 48937\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,514 km\",\n"
        + "                  \"value\" : 3514145\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 16 hours\",\n"
        + "                  \"value\" : 143602\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"2,841 km\",\n"
        + "                  \"value\" : 2841289\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 6 hours\",\n"
        + "                  \"value\" : 106708\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            }\n"
        + "         ]\n"
        + "      },\n"
        + "      {\n"
        + "         \"elements\" : [\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,237 km\",\n"
        + "                  \"value\" : 3236842\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 11 hours\",\n"
        + "                  \"value\" : 124400\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,448 km\",\n"
        + "                  \"value\" : 3448098\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 14 hours\",\n"
        + "                  \"value\" : 136447\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"1,009 km\",\n"
        + "                  \"value\" : 1008759\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"11 hours 28 mins\",\n"
        + "                  \"value\" : 41292\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,911 km\",\n"
        + "                  \"value\" : 3910632\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 21 hours\",\n"
        + "                  \"value\" : 161064\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"4,483 km\",\n"
        + "                  \"value\" : 4482990\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 23 hours\",\n"
        + "                  \"value\" : 169779\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            }\n"
        + "         ]\n"
        + "      },\n"
        + "      {\n"
        + "         \"elements\" : [\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"1,958 km\",\n"
        + "                  \"value\" : 1958480\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"21 hours 52 mins\",\n"
        + "                  \"value\" : 78694\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"210 km\",\n"
        + "                  \"value\" : 210387\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"2 hours 16 mins\",\n"
        + "                  \"value\" : 8142\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,850 km\",\n"
        + "                  \"value\" : 3849813\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 17 hours\",\n"
        + "                  \"value\" : 149004\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"1,118 km\",\n"
        + "                  \"value\" : 1118071\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"13 hours 44 mins\",\n"
        + "                  \"value\" : 49447\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,970 km\",\n"
        + "                  \"value\" : 3969954\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 17 hours\",\n"
        + "                  \"value\" : 147229\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            }\n"
        + "         ]\n"
        + "      },\n"
        + "      {\n"
        + "         \"elements\" : [\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,031 km\",\n"
        + "                  \"value\" : 3030901\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 16 hours\",\n"
        + "                  \"value\" : 144120\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"4,487 km\",\n"
        + "                  \"value\" : 4487372\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"2 days 8 hours\",\n"
        + "                  \"value\" : 201775\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"1,575 km\",\n"
        + "                  \"value\" : 1575309\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 1 hour\",\n"
        + "                  \"value\" : 88234\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"4,950 km\",\n"
        + "                  \"value\" : 4949906\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"2 days 15 hours\",\n"
        + "                  \"value\" : 226392\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"4,277 km\",\n"
        + "                  \"value\" : 4277050\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"2 days 5 hours\",\n"
        + "                  \"value\" : 189499\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            }\n"
        + "         ]\n"
        + "      },\n"
        + "      {\n"
        + "         \"elements\" : [\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"2,620 km\",\n"
        + "                  \"value\" : 2619695\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 4 hours\",\n"
        + "                  \"value\" : 99764\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,962 km\",\n"
        + "                  \"value\" : 3962495\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 19 hours\",\n"
        + "                  \"value\" : 154830\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"300 km\",\n"
        + "                  \"value\" : 299573\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"3 hours 47 mins\",\n"
        + "                  \"value\" : 13623\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"4,425 km\",\n"
        + "                  \"value\" : 4425029\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"2 days 2 hours\",\n"
        + "                  \"value\" : 179447\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            },\n"
        + "            {\n"
        + "               \"distance\" : {\n"
        + "                  \"text\" : \"3,866 km\",\n"
        + "                  \"value\" : 3865843\n"
        + "               },\n"
        + "               \"duration\" : {\n"
        + "                  \"text\" : \"1 day 16 hours\",\n"
        + "                  \"value\" : 145143\n"
        + "               },\n"
        + "               \"status\" : \"OK\"\n"
        + "            }\n"
        + "         ]\n"
        + "      }\n"
        + "   ],\n"
        + "   \"status\" : \"OK\"\n"
        + "}\n");
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
        DistanceMatrixApi.getDistanceMatrix(sc.context, origins, destinations).await();

    // Rows length will match the number of origin elements, regardless of whether they're routable.
    assertEquals(8, matrix.rows.length);
    assertEquals(5, matrix.rows[0].elements.length);
    assertEquals(DistanceMatrixElementStatus.OK, matrix.rows[0].elements[0].status);

    assertEquals("Perth WA, Australia", matrix.originAddresses[0]);
    assertEquals("Sydney NSW, Australia", matrix.originAddresses[1]);
    assertEquals("Melbourne VIC, Australia", matrix.originAddresses[2]);
    assertEquals("Adelaide SA, Australia", matrix.originAddresses[3]);
    assertEquals("Brisbane QLD, Australia", matrix.originAddresses[4]);
    assertEquals("Darwin NT, Australia", matrix.originAddresses[5]);
    assertEquals("Hobart TAS 7000, Australia", matrix.originAddresses[6]);
    assertEquals("Canberra ACT 2601, Australia", matrix.originAddresses[7]);

    assertEquals("Uluru, Petermann NT 0872, Australia", matrix.destinationAddresses[0]);
    assertEquals("Kakadu NT 0822, Australia", matrix.destinationAddresses[1]);
    assertEquals("Blue Mountains, New South Wales, Australia", matrix.destinationAddresses[2]);
    assertEquals("Purnululu National Park, Western Australia 6770, Australia", matrix.destinationAddresses[3]);
    assertEquals("Pinnacles Drive, Cervantes WA 6511, Australia", matrix.destinationAddresses[4]);

    sc.assertParamValue(StringUtils.join(origins, "|"), "origins");
    sc.assertParamValue(StringUtils.join(destinations, "|"), "destinations");
  }

  @Test
  public void testNewRequestWithAllPossibleParams() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}");
    String[] origins = new String[]{
        "Perth, Australia", "Sydney, Australia", "Melbourne, Australia",
        "Adelaide, Australia", "Brisbane, Australia", "Darwin, Australia",
        "Hobart, Australia", "Canberra, Australia"
    };
    String[] destinations = new String[]{
        "Uluru, Australia", "Kakadu, Australia", "Blue Mountains, Australia",
        "Bungle Bungles, Australia", "The Pinnacles, Australia"
    };

    DistanceMatrix matrix = DistanceMatrixApi.newRequest(sc.context)
        .origins(origins)
        .destinations(destinations)
        .mode(TravelMode.DRIVING)
        .language("en-AU")
        .avoid(RouteRestriction.TOLLS)
        .units(Unit.IMPERIAL)
        .departureTime(new DateTime().plusMinutes(2))  // this is ignored when an API key is used
        .await();

    sc.assertParamValue(StringUtils.join(origins, "|"), "origins");
    sc.assertParamValue(StringUtils.join(destinations, "|"), "destinations");
    sc.assertParamValue(TravelMode.DRIVING.toUrlValue(), "mode");
    sc.assertParamValue("en-AU", "language");
    sc.assertParamValue(RouteRestriction.TOLLS.toUrlValue(), "avoid");
    sc.assertParamValue(Unit.IMPERIAL.toUrlValue(), "units");
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
    LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}");
    String[] origins = new String[]{"Vancouver BC", "Seattle"};
    String[] destinations = new String[]{"San Francisco", "Victoria BC"};
    DistanceMatrixApi.newRequest(sc.context)
        .origins(origins)
        .destinations(destinations)
        .mode(TravelMode.BICYCLING)
        .language("fr-FR")
        .await();

    sc.assertParamValue(StringUtils.join(origins, "|"), "origins");
    sc.assertParamValue(StringUtils.join(destinations, "|"), "destinations");
    sc.assertParamValue(TravelMode.BICYCLING.toUrlValue(), "mode");
    sc.assertParamValue("fr-FR", "language");
  }

  /**
   * Test transit without arrival or departure times specified.
   */
  @Test
  public void testTransitWithoutSpecifyingTime() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}");
    String[] origins = new String[]{"Fisherman's Wharf, San Francisco", "Union Square, San Francisco"};
    String[] destinations = new String[]{"Mikkeller Bar, San Francisco", "Moscone Center, San Francisco"};
    DistanceMatrixApi.newRequest(sc.context)
        .origins(origins)
        .destinations(destinations)
        .mode(TravelMode.TRANSIT)
        .await();

    sc.assertParamValue(StringUtils.join(origins, "|"), "origins");
    sc.assertParamValue(StringUtils.join(destinations, "|"), "destinations");
    sc.assertParamValue(TravelMode.TRANSIT.toUrlValue(), "mode");
  }

  /**
   * Test duration in traffic with traffic model set.
   */
  @Test
  public void testDurationInTrafficWithTrafficModel() throws Exception {
    LocalTestServerContext sc = new LocalTestServerContext("{\"status\" : \"OK\"}");
    final long ONE_HOUR_MILLIS = 60 * 60 * 1000;
    DistanceMatrixApi.newRequest(sc.context)
        .origins("Fisherman's Wharf, San Francisco")
        .destinations("San Francisco International Airport, San Francisco, CA")
        .mode(TravelMode.DRIVING)
        .trafficModel(TrafficModel.PESSIMISTIC)
        .departureTime(new DateTime(System.currentTimeMillis() + ONE_HOUR_MILLIS))
        .await();

    sc.assertParamValue("Fisherman's Wharf, San Francisco", "origins");
    sc.assertParamValue("San Francisco International Airport, San Francisco, CA", "destinations");
    sc.assertParamValue(TravelMode.DRIVING.toUrlValue(), "mode");
    sc.assertParamValue(TrafficModel.PESSIMISTIC.toUrlValue(), "traffic_model");
  }

}
