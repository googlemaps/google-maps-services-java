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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.maps.errors.InvalidRequestException;
import com.google.maps.errors.RequestDeniedException;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.google.maps.model.LatLngAssert;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(MediumTests.class)
public class ElevationApiTest {

  private static final double SYDNEY_ELEVATION = 19.11174774169922;
  private static final double SYDNEY_POINT_ELEVATION = 19.10829925537109;
  private static final double MELBOURNE_ELEVATION = 25.49982643127441;
  private static final double EPSILON = .00001;
  private static final LatLng SYDNEY = new LatLng(-33.867487, 151.206990);
  private static final LatLng MELBOURNE = new LatLng(-37.814107, 144.963280);
  private static final EncodedPolyline SYD_MELB_ROUTE =
      new EncodedPolyline(
          "rvumEis{y[`NsfA~tAbF`bEj^h{@{KlfA~eA~`AbmEghAt~D|e@jlRpO~yH_\\v}LjbBh~FdvCxu@`nCplDbcBf_B|w"
              + "BhIfhCnqEb~D~jCn_EngApdEtoBbfClf@t_CzcCpoEr_Gz_DxmAphDjjBxqCviEf}B|pEvsEzbE~qGfpExjBlqCx}"
              + "BvmLb`FbrQdpEvkAbjDllD|uDldDj`Ef|AzcEx_Gtm@vuI~xArwD`dArlFnhEzmHjtC~eDluAfkC|eAdhGpJh}N_m"
              + "ArrDlr@h|HzjDbsAvy@~~EdTxpJje@jlEltBboDjJdvKyZpzExrAxpHfg@pmJg[tgJuqBnlIarAh}DbN`hCeOf_Ib"
              + "xA~uFt|A|xEt_ArmBcN|sB|h@b_DjOzbJ{RlxCcfAp~AahAbqG~Gr}AerA`dCwlCbaFo]twKt{@bsG|}A~fDlvBvz"
              + "@tw@rpD_r@rqB{PvbHek@vsHlh@ptNtm@fkD[~xFeEbyKnjDdyDbbBtuA|~Br|Gx_AfxCt}CjnHv`Ew\\lnBdrBfq"
              + "BraD|{BldBxpG|]jqC`mArcBv]rdAxgBzdEb{InaBzyC}AzaEaIvrCzcAzsCtfD~qGoPfeEh]h`BxiB`e@`kBxfAv"
              + "^pyA`}BhkCdoCtrC~bCxhCbgEplKrk@tiAteBwAxbCwuAnnCc]b{FjrDdjGhhGzfCrlDruBzSrnGhvDhcFzw@n{@z"
              + "xAf}Fd{IzaDnbDjoAjqJjfDlbIlzAraBxrB}K~`GpuD~`BjmDhkBp{@r_AxCrnAjrCx`AzrBj{B|r@~qBbdAjtDnv"
              + "CtNzpHxeApyC|GlfM`fHtMvqLjuEtlDvoFbnCt|@xmAvqBkGreFm~@hlHw|AltC}NtkGvhBfaJ|~@riAxuC~gErwC"
              + "ttCzjAdmGuF`iFv`AxsJftD|nDr_QtbMz_DheAf~Buy@rlC`i@d_CljC`gBr|H|nAf_Fh{G|mE~kAhgKviEpaQnu@"
              + "zwAlrA`G~gFnvItz@j{Cng@j{D{]`tEftCdcIsPz{DddE~}PlnE|dJnzG`eG`mF|aJdqDvoAwWjzHv`H`wOtjGzeX"
              + "hhBlxErfCf{BtsCjpEjtD|}Aja@xnAbdDt|ErMrdFh{CzgAnlCnr@`wEM~mE`bA`uD|MlwKxmBvuFlhB|sN`_@fvB"
              + "p`CxhCt_@loDsS|eDlmChgFlqCbjCxk@vbGxmCjbMba@rpBaoClcCk_DhgEzYdzBl\\vsA_JfGztAbShkGtEhlDzh"
              + "C~w@hnB{e@yF}`D`_Ayx@~vGqn@l}CafC");

  private final String directionsAlongPath;

  public ElevationApiTest() {
    directionsAlongPath = retrieveBody("DirectionsAlongPath.json");
  }

  @Test(expected = InvalidRequestException.class)
  public void testGetByPointThrowsInvalidRequestExceptionFromResponse() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "{\n   \"routes\" : [],\n   \"status\" : \"INVALID_REQUEST\"\n}")) {

      // This should throw the InvalidRequestException
      ElevationApi.getByPoint(sc.context, new LatLng(0, 0)).await();
    }
  }

  @Test(expected = RequestDeniedException.class)
  public void testGetByPointsThrowsRequestDeniedExceptionFromResponse() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            ""
                + "{\n"
                + "   \"routes\" : [],\n"
                + "   \"status\" : \"REQUEST_DENIED\",\n"
                + "   \"errorMessage\" : \"Can't do the thing\"\n"
                + "}")) {

      // This should throw the RequestDeniedException
      ElevationApi.getByPoints(
              sc.context, new EncodedPolyline(Collections.singletonList(new LatLng(0, 0))))
          .await();
    }
  }

  @Test
  public void testGetPoint() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            ""
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"elevation\" : 19.10829925537109,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -33.867487,\n"
                + "            \"lng\" : 151.20699\n"
                + "         },\n"
                + "         \"resolution\" : 4.771975994110107\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      ElevationResult result = ElevationApi.getByPoint(sc.context, SYDNEY).await();

      assertNotNull(result);
      assertNotNull(result.toString());
      assertEquals(SYDNEY_POINT_ELEVATION, result.elevation, EPSILON);

      sc.assertParamValue(SYDNEY.toUrlValue(), "locations");
    }
  }

  @Test
  public void testGetPoints() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            ""
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"elevation\" : 19.11174774169922,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -33.86749,\n"
                + "            \"lng\" : 151.20699\n"
                + "         },\n"
                + "         \"resolution\" : 4.771975994110107\n"
                + "      },\n"
                + "      {\n"
                + "         \"elevation\" : 25.49982643127441,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -37.81411,\n"
                + "            \"lng\" : 144.96328\n"
                + "         },\n"
                + "         \"resolution\" : 152.7032318115234\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      ElevationResult[] results = ElevationApi.getByPoints(sc.context, SYDNEY, MELBOURNE).await();

      assertNotNull(results);
      assertEquals(2, results.length);
      assertEquals(SYDNEY_ELEVATION, results[0].elevation, EPSILON);
      assertEquals(MELBOURNE_ELEVATION, results[1].elevation, EPSILON);

      sc.assertParamValue("enc:xvumEur{y[jyaWdnbe@", "locations");
    }
  }

  @Test
  public void testGetPath() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            ""
                + "{\n"
                + "   \"results\" : [\n"
                + "      {\n"
                + "         \"elevation\" : 19.11174774169922,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -33.86749,\n"
                + "            \"lng\" : 151.20699\n"
                + "         },\n"
                + "         \"resolution\" : 4.771975994110107\n"
                + "      },\n"
                + "      {\n"
                + "         \"elevation\" : 456.7416381835938,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -34.32145720949158,\n"
                + "            \"lng\" : 150.5433152252451\n"
                + "         },\n"
                + "         \"resolution\" : 152.7032318115234\n"
                + "      },\n"
                + "      {\n"
                + "         \"elevation\" : 677.8786010742188,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -34.77180578055915,\n"
                + "            \"lng\" : 149.8724504366625\n"
                + "         },\n"
                + "         \"resolution\" : 152.7032318115234\n"
                + "      },\n"
                + "      {\n"
                + "         \"elevation\" : 672.6239624023438,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -35.21843425947625,\n"
                + "            \"lng\" : 149.1942540405992\n"
                + "         },\n"
                + "         \"resolution\" : 152.7032318115234\n"
                + "      },\n"
                + "      {\n"
                + "         \"elevation\" : 1244.74755859375,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -35.66123890186951,\n"
                + "            \"lng\" : 148.5085849619781\n"
                + "         },\n"
                + "         \"resolution\" : 152.7032318115234\n"
                + "      },\n"
                + "      {\n"
                + "         \"elevation\" : 317.3624572753906,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -36.10011364524662,\n"
                + "            \"lng\" : 147.815302885111\n"
                + "         },\n"
                + "         \"resolution\" : 152.7032318115234\n"
                + "      },\n"
                + "      {\n"
                + "         \"elevation\" : 797.5011596679688,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -36.53495008485245,\n"
                + "            \"lng\" : 147.1142685138642\n"
                + "         },\n"
                + "         \"resolution\" : 152.7032318115234\n"
                + "      },\n"
                + "      {\n"
                + "         \"elevation\" : 684.0189819335938,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -36.9656374532439,\n"
                + "            \"lng\" : 146.4053438519865\n"
                + "         },\n"
                + "         \"resolution\" : 152.7032318115234\n"
                + "      },\n"
                + "      {\n"
                + "         \"elevation\" : 351.05712890625,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -37.39206260399896,\n"
                + "            \"lng\" : 145.6883925043725\n"
                + "         },\n"
                + "         \"resolution\" : 152.7032318115234\n"
                + "      },\n"
                + "      {\n"
                + "         \"elevation\" : 25.49982643127441,\n"
                + "         \"location\" : {\n"
                + "            \"lat\" : -37.81411,\n"
                + "            \"lng\" : 144.96328\n"
                + "         },\n"
                + "         \"resolution\" : 152.7032318115234\n"
                + "      }\n"
                + "   ],\n"
                + "   \"status\" : \"OK\"\n"
                + "}\n")) {
      ElevationResult[] results = ElevationApi.getByPath(sc.context, 10, SYDNEY, MELBOURNE).await();

      assertNotNull(results);
      assertEquals(10, results.length);
      assertEquals(SYDNEY_ELEVATION, results[0].elevation, EPSILON);
      assertEquals(MELBOURNE_ELEVATION, results[9].elevation, EPSILON);

      sc.assertParamValue("10", "samples");
      sc.assertParamValue("enc:xvumEur{y[jyaWdnbe@", "path");
    }
  }

  @Test
  public void testDirectionsAlongPath() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(directionsAlongPath)) {
      ElevationResult[] elevation = ElevationApi.getByPath(sc.context, 100, SYD_MELB_ROUTE).await();
      assertEquals(100, elevation.length);

      List<LatLng> overviewPolylinePath = SYD_MELB_ROUTE.decodePath();
      LatLng lastDirectionsPoint = overviewPolylinePath.get(overviewPolylinePath.size() - 1);
      LatLng lastElevationPoint = elevation[elevation.length - 1].location;

      LatLngAssert.assertEquals(lastDirectionsPoint, lastElevationPoint, EPSILON);

      sc.assertParamValue("100", "samples");
      sc.assertParamValue("enc:" + SYD_MELB_ROUTE.getEncodedPath(), "path");
    }
  }
}
