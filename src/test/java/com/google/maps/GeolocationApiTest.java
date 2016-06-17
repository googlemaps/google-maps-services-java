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
import static org.junit.Assert.assertNotNull;

import com.google.maps.model.CellTower;
import com.google.maps.model.GeolocationPostPayload;
import com.google.maps.model.GeolocationResult;
import com.google.maps.model.WifiAccessPoint;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.concurrent.TimeUnit;

@Category(LargeTests.class)
public class GeolocationApiTest extends AuthenticatedTest {

  private GeoApiContext context;

  public GeolocationApiTest (GeoApiContext context) {
    this.context = context
        .setQueryRateLimit(3)
        .setConnectTimeout(1, TimeUnit.SECONDS)
        .setReadTimeout(1, TimeUnit.SECONDS)
        .setWriteTimeout(1, TimeUnit.SECONDS);
  }

  @Test
  public void testSimpleGeolocation() throws Exception {
    GeolocationPostPayload payload = new GeolocationPostPayload();
    payload.homeMobileCountryCode = 310;
    payload.homeMobileNetworkCode = 410;
    payload.radioType = "gsm";
    payload.carrier = "Vodafone";
    payload.considerIp = true;

    WifiAccessPoint[] wifiAccessPoints = new WifiAccessPoint[1];
    wifiAccessPoints[0] = new WifiAccessPoint();
    wifiAccessPoints[0].macAddress ="01:23:45:67:89:AB";
    wifiAccessPoints[0].signalStrength = -65;
    wifiAccessPoints[0].age = 0;
    wifiAccessPoints[0].channel = 11;
    wifiAccessPoints[0].signalToNoiseRatio = 40;

    payload.wifiAccessPoints = wifiAccessPoints;

    /*
    CellTower[] cellTowers = new CellTower[1];
    cellTowers[0] =  new CellTower();
    cellTowers[0].cellId = 42;
    cellTowers[0].locationAreaCode = 415;
    cellTowers[0].mobileCountryCode = 310;
    cellTowers[0].mobileNetworkCode = 410;
    cellTowers[0].age = 0;
    cellTowers[0].signalStrength = -60;
    cellTowers[0].timingAdvance = 15;
    payload.cellTowers = cellTowers;
    */

    GeolocationResult result = GeolocationApi.geolocate(context, payload).await();

    assertNotNull(result);
    assertNotNull(result.location);
    assertEquals("accuracy", 2855.0, result.accuracy, 0.01);
    assertEquals("lat", 37.428433999999996, result.location.lat, 0.01);
    assertEquals("lng", -122.0723816, result.location.lng, 0.01);



  }
}
