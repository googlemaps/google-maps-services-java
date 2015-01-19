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
package com.google.maps.business;

import com.google.maps.PendingResult;
import com.google.maps.model.CellTower;
import com.google.maps.model.GeolocationResult;
import com.google.maps.model.WiFiAccessPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gavin.Lin
 */
public class GeolocationApiTest extends BusiAuthenticatedTest {

  public static final double EPSILON = 0.000001;

  private GeoBusiApiContext context;
  private CellTower cellTower = new CellTower(40495, 310, 260).setCellId(39627456).setAge(0).setSignalStrength(-95);
  private WiFiAccessPoint wiFiAccessPoint = new WiFiAccessPoint("01:23:45:67:89:AB").setSignalStrength(8).setChannel(8).setSignalToNoiseRatio(-65).setAge(0);

  public GeolocationApiTest(GeoBusiApiContext context) {
    this.context = context
            .setQueryRateLimit(3)
            .setConnectTimeout(1, TimeUnit.SECONDS)
            .setReadTimeout(1, TimeUnit.SECONDS)
            .setWriteTimeout(1, TimeUnit.SECONDS);
  }

  @Test
  public void testSimpleGeocode() throws Exception {
    GeolocationResult result = GeolocationApi.newRequest(context)
            .homeMobileCountryCode(310)
            .homeMobileNetworkCode(260)
            .radioType("gsm")
            .carrier("T-Mobile")
            .cellTowers(cellTower)
            .wiFiAccessPoints(wiFiAccessPoint)
            .await();
    checkSydneyResult(result);
  }

  @Test
  public void testAsync() throws Exception {
    final List<GeolocationResult> resps = new ArrayList<GeolocationResult>();

    PendingResult.Callback<GeolocationResult> callback
            = new PendingResult.Callback<GeolocationResult>() {
              @Override
              public void onResult(GeolocationResult result) {
                resps.add(result);
              }

              @Override
              public void onFailure(Throwable e) {
                fail("Got error when expected success.");
              }
            };
    GeolocationApi.newRequest(context)
            .homeMobileCountryCode(310)
            .homeMobileNetworkCode(260)
            .radioType("gsm")
            .carrier("T-Mobile")
            .cellTowers(cellTower)
            .wiFiAccessPoints(wiFiAccessPoint)
            .setCallback(callback);

    Thread.sleep(2500);

    assertFalse(resps.isEmpty());
    assertNotNull(resps.get(0));
    checkSydneyResult(resps.get(0));
  }

  private void checkSydneyResult(GeolocationResult result) {
    assertNotNull(result);
    assertNotNull(result.accuracy);
    assertNotNull(result.location);
    assertEquals(1145.0, result.accuracy, EPSILON);
    assertEquals(37.4248297, result.location.lat, EPSILON);
    assertEquals(-122.07346549999998, result.location.lng, EPSILON);
  }

  @Test
  public void testBadKey() throws Exception {
    GeoBusiApiContext badContext = new GeoBusiApiContext()
            .setApiKey("AIza.........");

    GeolocationResult results = GeolocationApi.newRequest(badContext)
            .homeMobileCountryCode(310)
            .homeMobileNetworkCode(260)
            .radioType("gsm")
            .carrier("T-Mobile")
            .cellTowers(cellTower)
            .wiFiAccessPoints(wiFiAccessPoint)
            .awaitIgnoreError();
    assertNull(results);

    try {
      results = GeolocationApi.newRequest(badContext).homeMobileCountryCode(310)
              .homeMobileNetworkCode(260)
              .radioType("gsm")
              .carrier("T-Mobile")
              .cellTowers(cellTower)
              .wiFiAccessPoints(wiFiAccessPoint)
              .await();
      assertNull(results);
      fail("Expected exception REQUEST_DENIED");
    } catch (Exception e) {
      assertEquals("Server Error: 400 Bad Request", e.getMessage());
    }
  }

}
