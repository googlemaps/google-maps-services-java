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

import static com.google.maps.model.LatLngAssert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.maps.model.ElevationResult;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Category(LargeTests.class)
public class ElevationApiIntegrationTest extends AuthenticatedTest {

  public static final double SYDNEY_ELEVATION = 19.11174774169922;
  public static final double SYDNEY_POINT_ELEVATION = 19.10829925537109;
  public static final double MELBOURNE_ELEVATION = 9.253130912780762;
  private static final double EPSILON = .00001;
  private static final LatLng SYDNEY = new LatLng(-33.867487, 151.206990);
  private static final LatLng MELBOURNE = new LatLng(-37.814107, 144.963280);
  private static final EncodedPolyline SYD_MELB_ROUTE = new EncodedPolyline(
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
  private GeoApiContext context;

  public ElevationApiIntegrationTest(GeoApiContext context) {
    this.context = context
        .setQueryRateLimit(3)
        .setConnectTimeout(1, TimeUnit.SECONDS)
        .setReadTimeout(1, TimeUnit.SECONDS)
        .setWriteTimeout(1, TimeUnit.SECONDS);
  }

  @Test
  public void testGetPoint() throws Exception {
    ElevationResult result = ElevationApi.getByPoint(context, SYDNEY).await();

    assertNotNull(result);
    assertEquals(SYDNEY_POINT_ELEVATION, result.elevation, EPSILON);
  }

  @Test
  public void testGetPoints() throws Exception {
    ElevationResult[] results = ElevationApi.getByPoints(context, SYDNEY, MELBOURNE).await();

    assertNotNull(results);
    assertEquals(2, results.length);
    assertEquals(SYDNEY_ELEVATION, results[0].elevation, EPSILON);
    assertEquals(MELBOURNE_ELEVATION, results[1].elevation, EPSILON);
  }

  @Test
  public void testGetPath() throws Exception {
    ElevationResult[] results = ElevationApi.getByPath(context, 10, SYDNEY, MELBOURNE).await();

    assertNotNull(results);
    assertEquals(10, results.length);
    assertEquals(SYDNEY_ELEVATION, results[0].elevation, EPSILON);
    assertEquals(MELBOURNE_ELEVATION, results[9].elevation, EPSILON);
  }

  @Test
  public void testDirectionsAlongPath() throws Exception {
    ElevationResult[] elevation = ElevationApi.getByPath(context, 100, SYD_MELB_ROUTE).await();
    assertEquals(100, elevation.length);

    List<LatLng> overviewPolylinePath = SYD_MELB_ROUTE.decodePath();
    LatLng lastDirectionsPoint = overviewPolylinePath.get(overviewPolylinePath.size() - 1);
    LatLng lastElevationPoint = elevation[elevation.length - 1].location;

    assertEquals(lastDirectionsPoint, lastElevationPoint, EPSILON);
  }
}
