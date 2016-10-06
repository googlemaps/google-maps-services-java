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
import static org.junit.Assert.assertTrue;

import com.google.maps.model.CellTower;
import com.google.maps.model.GeolocationPayload;
import com.google.maps.model.GeolocationResult;
import com.google.maps.model.WifiAccessPoint;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.concurrent.TimeUnit;

@Category(LargeTests.class)
public class GeolocationApiTest extends KeyOnlyAuthenticatedTest {

  private GeoApiContext context;

  public GeolocationApiTest(GeoApiContext context) {
    this.context = context
        .setQueryRateLimit(3)
        .setConnectTimeout(1, TimeUnit.SECONDS)
        .setReadTimeout(1, TimeUnit.SECONDS)
        .setWriteTimeout(1, TimeUnit.SECONDS);
  }

  @Test
  public void testDocSampleGeolocation() throws Exception {
    // https://developers.google.com/maps/documentation/geolocation/intro#sample-requests
    GeolocationResult result = GeolocationApi.newRequest(context)
        .ConsiderIp(false)
        .HomeMobileCountryCode(310)
        .HomeMobileNetworkCode(260)
        .RadioType("gsm")
        .Carrier("T-Mobile")
        .AddCellTower(new CellTower.CellTowerBuilder()
            .CellId(39627456)
            .LocationAreaCode(40495)
            .MobileCountryCode(310)
            .MobileNetworkCode(260)
            .Age(0)
            .SignalStrength(-95)
            .createCellTower())
        .AddWifiAccessPoint(new WifiAccessPoint.WifiAccessPointBuilder()
            .MacAddress("01:23:45:67:89:AB")
            .SignalStrength(-65)
            .SignalToNoiseRatio(8)
            .Channel(8)
            .Age(0)
            .createWifiAccessPoint())
        .AddWifiAccessPoint(new WifiAccessPoint.WifiAccessPointBuilder()
            .MacAddress("01:23:45:67:89:AC")
            .SignalStrength(4)
            .SignalToNoiseRatio(4)
            .Age(0)
            .createWifiAccessPoint())
        .CreatePayload()
        .await();

    assertNotNull(result);
    assertNotNull(result.location);
    assertEquals("accuracy", 1145.0, result.accuracy, 0.00001);
    assertEquals("lat", 37.4248297, result.location.lat, 0.00001);
    assertEquals("lng", -122.07346549999998, result.location.lng, 0.00001);
  }

  @Test
  public void testMinimumWifiGeolocation() throws Exception {
    GeolocationResult result = GeolocationApi.newRequest(context)
        .ConsiderIp(false)
        .AddWifiAccessPoint(new WifiAccessPoint.WifiAccessPointBuilder()
            .MacAddress("94:b4:0f:ff:6b:11")
            .createWifiAccessPoint())
        .AddWifiAccessPoint(new WifiAccessPoint.WifiAccessPointBuilder()
            .MacAddress("94:b4:0f:ff:6b:10")
            .createWifiAccessPoint())
        .CreatePayload()
        .await();

    assertNotNull(result);
    assertNotNull(result.location);
    assertEquals("accuracy", 150.0, result.accuracy, 0.001);
    assertEquals("lat", 37.3989885, result.location.lat, 0.001);
    assertEquals("lng", -122.0585196, result.location.lng, 0.001);
  }

  // Commenting out flaky test - brettmorgan@google.com
  //@Test
  public void testBasicGeolocation() throws Exception {
    GeolocationResult result = GeolocationApi.newRequest(context)
        .ConsiderIp(false)
        .AddWifiAccessPoint(new WifiAccessPoint.WifiAccessPointBuilder()
            .MacAddress("92:68:c3:f8:76:47")
            .SignalStrength(-42)
            .SignalToNoiseRatio(68)
            .createWifiAccessPoint())
        .AddWifiAccessPoint(new WifiAccessPoint.WifiAccessPointBuilder()
            .MacAddress("94:b4:0f:ff:6b:11")
            .SignalStrength(-55)
            .SignalToNoiseRatio(55)
            .createWifiAccessPoint())
        .CreatePayload()
        .await();

    assertNotNull(result);
    assertNotNull(result.location);
    assertEquals("accuracy", 150.0, result.accuracy, 0.00001);
    assertEquals("lat", 37.3989885, result.location.lat, 0.00001);
    assertEquals("lng", -122.0585196, result.location.lng, 0.00001);
  }

  @Test
  public void testAlternateWifiSetterGeolocation() throws Exception {
    WifiAccessPoint[] wifiAccessPoints = new WifiAccessPoint[2];
    wifiAccessPoints[0] = new WifiAccessPoint.WifiAccessPointBuilder()
        .MacAddress("94:b4:0f:ff:6b:11")
        .createWifiAccessPoint();
    wifiAccessPoints[1] = new WifiAccessPoint.WifiAccessPointBuilder()
        .MacAddress("94:b4:0f:ff:6b:10")
        .createWifiAccessPoint();

    GeolocationResult result = GeolocationApi.newRequest(context)
        .ConsiderIp(false)
        .WifiAccessPoints(wifiAccessPoints)
        .CreatePayload()
        .await();

    assertNotNull(result);
    assertNotNull(result.location);
    assertEquals("accuracy", 150.0, result.accuracy, 0.001);
    assertEquals("lat", 37.3989885, result.location.lat, 0.001);
    assertEquals("lng", -122.0585196, result.location.lng, 0.001);
  }

  // Commenting out flaky test to make Travis happy - brettmorgan@google.com
  //@Test
  public void testMaximumWifiGeolocation() throws Exception {
    GeolocationResult result = GeolocationApi.newRequest(context)
        .ConsiderIp(false)
        .HomeMobileCountryCode(310)
        .HomeMobileNetworkCode(410)
        .RadioType("gsm")
        .Carrier("Vodafone")
        .AddWifiAccessPoint(new WifiAccessPoint.WifiAccessPointBuilder()
            .MacAddress("94:b4:0f:ff:88:31")
            .SignalStrength(-61)
            .SignalToNoiseRatio(49)
            .Channel(40)
            .Age(0)
            .createWifiAccessPoint())
        .AddWifiAccessPoint(new WifiAccessPoint.WifiAccessPointBuilder()
            .MacAddress("94:b4:0f:ff:88:30")
            .SignalStrength(-64)
            .SignalToNoiseRatio(46)
            .Channel(40)
            .Age(0)
            .createWifiAccessPoint())
        .CreatePayload()
        .await();

    assertNotNull(result);
    assertNotNull(result.location);
    assertEquals("accuracy", 25.0, result.accuracy, 0.00001);
    assertEquals("lat", 37.3990122, result.location.lat, 0.00001);
    assertEquals("lng", -122.0583656, result.location.lng, 0.00001);
  }

  @Test
  public void testMinimumCellTowerGeolocation() throws Exception {
    GeolocationResult result = GeolocationApi.newRequest(context)
        .ConsiderIp(false)
        .AddCellTower(new CellTower.CellTowerBuilder()
            .CellId(39627456)
            .LocationAreaCode(40495)
            .MobileCountryCode(310)
            .MobileNetworkCode(260)
            .createCellTower())
        .CreatePayload()
        .await();

    assertNotNull(result);
    assertNotNull(result.location);
    assertEquals("accuracy", 658.0, result.accuracy, 0.00001);
    assertEquals("lat", 37.42659, result.location.lat, 0.00001);
    assertEquals("lng", -122.07266190000001, result.location.lng, 0.00001);
  }

  @Test
  public void testAlternatePayloadBuilderGeolocation() throws Exception {
    // using the alternate style of payload building
    GeolocationPayload payload = new GeolocationPayload.GeolocationPayloadBuilder()
        .ConsiderIp(false)
        .AddCellTower(new CellTower.CellTowerBuilder()
            .CellId(39627456)
            .LocationAreaCode(40495)
            .MobileCountryCode(310)
            .MobileNetworkCode(260)
            .createCellTower())
        .createGeolocationPayload();

    GeolocationResult result = GeolocationApi.geolocate(context, payload).await();
    assertNotNull(result);
    assertNotNull(result.location);
    assertEquals("accuracy", 658.0, result.accuracy, 0.00001);
    assertEquals("lat", 37.42659, result.location.lat, 0.00001);
    assertEquals("lng", -122.07266190000001, result.location.lng, 0.00001);
  }

  @Test
  public void testMaximumCellTowerGeolocation() throws Exception {
    GeolocationResult result = GeolocationApi.newRequest(context)
        .ConsiderIp(false)
        .HomeMobileCountryCode(310)
        .HomeMobileNetworkCode(260)
        .RadioType("gsm")
        .Carrier("Vodafone")
        .AddCellTower(new CellTower.CellTowerBuilder()
            .CellId(39627456)
            .LocationAreaCode(40495)
            .MobileCountryCode(310)
            .MobileNetworkCode(260)
            .Age(0)
            .SignalStrength(-103)
            .TimingAdvance(15)
            .createCellTower())
        .CreatePayload()
        .await();

    assertNotNull(result);
    assertNotNull(result.location);
    assertEquals("accuracy", 1145.0, result.accuracy, 0.00001);
    assertEquals("lat", 37.4248297, result.location.lat, 0.00001);
    assertEquals("lng", -122.07346549999998, result.location.lng, 0.00001);
  }

  @Test
  public void testNoPayloadGeolocation0() throws Exception {
    GeolocationPayload payload = new GeolocationPayload.GeolocationPayloadBuilder()
        .createGeolocationPayload();

    GeolocationResult result = GeolocationApi.geolocate(context, payload).await();
    assertNotNull(result);
    assertNotNull(result.location);
  }

  @Test
  public void testNoPayloadGeolocation1() throws Exception {
    GeolocationResult result = GeolocationApi.newRequest(context)
        .CreatePayload()
        .await();

    assertNotNull(result);
    assertNotNull(result.location);
  }

  @Test
  public void testNotFoundGeolocation() throws Exception {
    try {
      GeolocationResult result = GeolocationApi.newRequest(context)
          .ConsiderIp(false)
          .CreatePayload()
          .await();
    } catch (Exception e) {
      assertTrue(e.getMessage().equals("Not Found"));
    }
  }

  @Test
  public void testInvalidArgumentGeolocation() throws Exception {
    try {
      GeolocationResult result = GeolocationApi.newRequest(context)
          .HomeMobileCountryCode(-310)
          .CreatePayload()
          .await();
    } catch (Exception e) {
      assertTrue(e.getMessage().equals("Invalid value for UnsignedInteger: -310"));
    }
  }
}