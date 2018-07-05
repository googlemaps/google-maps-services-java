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
import com.google.maps.errors.NotFoundException;
import com.google.maps.model.CellTower;
import com.google.maps.model.GeolocationPayload;
import com.google.maps.model.GeolocationResult;
import com.google.maps.model.WifiAccessPoint;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(LargeTests.class)
public class GeolocationApiTest {

  private final String geolocationDocSample;
  private final String geolocationMinimumWifi;
  private final String geolocationBasic;
  private final String geolocationMaximumWifi;
  private final String geolocationMinimumCellTower;
  private final String geolocationAlternatePayloadBuilder;
  private final String geolocationMaximumCellTower;

  public GeolocationApiTest() {
    geolocationDocSample = retrieveBody("GeolocationDocSampleResponse.json");
    geolocationMinimumWifi = retrieveBody("GeolocationMinimumWifiResponse.json");
    geolocationBasic = retrieveBody("GeolocationBasicResponse.json");
    geolocationMaximumWifi = retrieveBody("GeolocationMaximumWifiResponse.json");
    geolocationMinimumCellTower = retrieveBody("GeolocationMinimumCellTowerResponse.json");
    geolocationAlternatePayloadBuilder = retrieveBody("GeolocationAlternatePayloadBuilder.json");
    geolocationMaximumCellTower = retrieveBody("GeolocationMaximumCellTower.json");
  }

  @Test
  public void testDocSampleGeolocation() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(geolocationDocSample)) {
      GeolocationResult result =
          GeolocationApi.newRequest(sc.context)
              .ConsiderIp(false)
              .HomeMobileCountryCode(310)
              .HomeMobileNetworkCode(260)
              .RadioType("gsm")
              .Carrier("T-Mobile")
              .AddCellTower(
                  new CellTower.CellTowerBuilder()
                      .CellId(39627456)
                      .LocationAreaCode(40495)
                      .MobileCountryCode(310)
                      .MobileNetworkCode(260)
                      .Age(0)
                      .SignalStrength(-95)
                      .createCellTower())
              .AddWifiAccessPoint(
                  new WifiAccessPoint.WifiAccessPointBuilder()
                      .MacAddress("01:23:45:67:89:AB")
                      .SignalStrength(-65)
                      .SignalToNoiseRatio(8)
                      .Channel(8)
                      .Age(0)
                      .createWifiAccessPoint())
              .AddWifiAccessPoint(
                  new WifiAccessPoint.WifiAccessPointBuilder()
                      .MacAddress("01:23:45:67:89:AC")
                      .SignalStrength(4)
                      .SignalToNoiseRatio(4)
                      .Age(0)
                      .createWifiAccessPoint())
              .CreatePayload()
              .await();

      assertNotNull(result.toString());

      JSONObject body = sc.requestBody();
      assertEquals(false, body.get("considerIp"));
      assertEquals(310, body.get("homeMobileCountryCode"));
      assertEquals(260, body.get("homeMobileNetworkCode"));
      assertEquals("gsm", body.get("radioType"));
      assertEquals("T-Mobile", body.get("carrier"));
      assertEquals("accuracy", 1145.0, result.accuracy, 0.00001);
      assertEquals("lat", 37.4248297, result.location.lat, 0.00001);
      assertEquals("lng", -122.07346549999998, result.location.lng, 0.00001);
    }
  }

  @Test
  public void testMinimumWifiGeolocation() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(geolocationMinimumWifi)) {
      GeolocationResult result =
          GeolocationApi.newRequest(sc.context)
              .ConsiderIp(false)
              .AddWifiAccessPoint(
                  new WifiAccessPoint.WifiAccessPointBuilder()
                      .MacAddress("94:b4:0f:ff:6b:11")
                      .createWifiAccessPoint())
              .AddWifiAccessPoint(
                  new WifiAccessPoint.WifiAccessPointBuilder()
                      .MacAddress("94:b4:0f:ff:6b:10")
                      .createWifiAccessPoint())
              .CreatePayload()
              .await();

      assertNotNull(result.toString());

      JSONObject body = sc.requestBody();
      assertEquals(false, body.get("considerIp"));
      JSONArray wifiAccessPoints = body.getJSONArray("wifiAccessPoints");
      assertEquals("94:b4:0f:ff:6b:11", wifiAccessPoints.getJSONObject(0).get("macAddress"));
      assertEquals("94:b4:0f:ff:6b:10", wifiAccessPoints.getJSONObject(1).get("macAddress"));
      assertEquals("accuracy", 150.0, result.accuracy, 0.001);
      assertEquals("lat", 37.3989885, result.location.lat, 0.001);
      assertEquals("lng", -122.0585196, result.location.lng, 0.001);
    }
  }

  @Test
  public void testBasicGeolocation() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(geolocationBasic)) {
      GeolocationResult result =
          GeolocationApi.newRequest(sc.context)
              .ConsiderIp(false)
              .AddWifiAccessPoint(
                  new WifiAccessPoint.WifiAccessPointBuilder()
                      .MacAddress("92:68:c3:f8:76:47")
                      .SignalStrength(-42)
                      .SignalToNoiseRatio(68)
                      .createWifiAccessPoint())
              .AddWifiAccessPoint(
                  new WifiAccessPoint.WifiAccessPointBuilder()
                      .MacAddress("94:b4:0f:ff:6b:11")
                      .SignalStrength(-55)
                      .SignalToNoiseRatio(55)
                      .createWifiAccessPoint())
              .CreatePayload()
              .await();

      assertNotNull(result.toString());

      JSONObject body = sc.requestBody();
      assertEquals(false, body.get("considerIp"));
      JSONArray wifiAccessPoints = body.getJSONArray("wifiAccessPoints");
      JSONObject wifi0 = wifiAccessPoints.getJSONObject(0);
      JSONObject wifi1 = wifiAccessPoints.getJSONObject(1);
      assertEquals("92:68:c3:f8:76:47", wifi0.get("macAddress"));
      assertEquals(-42, wifi0.get("signalStrength"));
      assertEquals(68, wifi0.get("signalToNoiseRatio"));
      assertEquals("94:b4:0f:ff:6b:11", wifi1.get("macAddress"));
      assertEquals(-55, wifi1.get("signalStrength"));
      assertEquals(55, wifi1.get("signalToNoiseRatio"));
      assertEquals("accuracy", 150.0, result.accuracy, 0.00001);
      assertEquals("lat", 37.3989885, result.location.lat, 0.00001);
      assertEquals("lng", -122.0585196, result.location.lng, 0.00001);
    }
  }

  @Test
  public void testAlternateWifiSetterGeolocation() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(geolocationBasic)) {
      WifiAccessPoint[] wifiAccessPoints = new WifiAccessPoint[2];
      wifiAccessPoints[0] =
          new WifiAccessPoint.WifiAccessPointBuilder()
              .MacAddress("94:b4:0f:ff:6b:11")
              .createWifiAccessPoint();
      wifiAccessPoints[1] =
          new WifiAccessPoint.WifiAccessPointBuilder()
              .MacAddress("94:b4:0f:ff:6b:10")
              .createWifiAccessPoint();

      GeolocationResult result =
          GeolocationApi.newRequest(sc.context)
              .ConsiderIp(false)
              .WifiAccessPoints(wifiAccessPoints)
              .CreatePayload()
              .await();

      assertNotNull(result.toString());

      JSONObject body = sc.requestBody();
      assertEquals(false, body.get("considerIp"));
      JSONArray wifiAccessPointsResponse = body.getJSONArray("wifiAccessPoints");
      JSONObject wifi0 = wifiAccessPointsResponse.getJSONObject(0);
      JSONObject wifi1 = wifiAccessPointsResponse.getJSONObject(1);
      assertEquals("94:b4:0f:ff:6b:11", wifi0.get("macAddress"));
      assertEquals("94:b4:0f:ff:6b:10", wifi1.get("macAddress"));
      assertEquals("accuracy", 150.0, result.accuracy, 0.001);
      assertEquals("lat", 37.3989885, result.location.lat, 0.001);
      assertEquals("lng", -122.0585196, result.location.lng, 0.001);
    }
  }

  @Test
  public void testMaximumWifiGeolocation() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(geolocationMaximumWifi)) {
      GeolocationResult result =
          GeolocationApi.newRequest(sc.context)
              .ConsiderIp(false)
              .HomeMobileCountryCode(310)
              .HomeMobileNetworkCode(410)
              .RadioType("gsm")
              .Carrier("Vodafone")
              .AddWifiAccessPoint(
                  new WifiAccessPoint.WifiAccessPointBuilder()
                      .MacAddress("94:b4:0f:ff:88:31")
                      .SignalStrength(-61)
                      .SignalToNoiseRatio(49)
                      .Channel(40)
                      .Age(0)
                      .createWifiAccessPoint())
              .AddWifiAccessPoint(
                  new WifiAccessPoint.WifiAccessPointBuilder()
                      .MacAddress("94:b4:0f:ff:88:30")
                      .SignalStrength(-64)
                      .SignalToNoiseRatio(46)
                      .Channel(40)
                      .Age(0)
                      .createWifiAccessPoint())
              .CreatePayload()
              .await();

      assertNotNull(result.toString());

      JSONObject body = sc.requestBody();
      assertEquals(false, body.get("considerIp"));
      assertEquals(310, body.get("homeMobileCountryCode"));
      assertEquals(410, body.get("homeMobileNetworkCode"));
      assertEquals("gsm", body.get("radioType"));
      assertEquals("Vodafone", body.get("carrier"));
      JSONArray wifiAccessPointsResponse = body.getJSONArray("wifiAccessPoints");
      JSONObject wifi0 = wifiAccessPointsResponse.getJSONObject(0);
      assertEquals("94:b4:0f:ff:88:31", wifi0.get("macAddress"));
      assertEquals(-61, wifi0.get("signalStrength"));
      assertEquals(49, wifi0.get("signalToNoiseRatio"));
      assertEquals(40, wifi0.get("channel"));
      assertEquals(0, wifi0.get("age"));
      JSONObject wifi1 = wifiAccessPointsResponse.getJSONObject(1);
      assertEquals("94:b4:0f:ff:88:30", wifi1.get("macAddress"));
      assertEquals(-64, wifi1.get("signalStrength"));
      assertEquals(46, wifi1.get("signalToNoiseRatio"));
      assertEquals(40, wifi1.get("channel"));
      assertEquals(0, wifi1.get("age"));
      assertEquals("accuracy", 25.0, result.accuracy, 0.00001);
      assertEquals("lat", 37.3990122, result.location.lat, 0.00001);
      assertEquals("lng", -122.0583656, result.location.lng, 0.00001);
    }
  }

  @Test
  public void testMinimumCellTowerGeolocation() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(geolocationMinimumCellTower)) {
      GeolocationResult result =
          GeolocationApi.newRequest(sc.context)
              .ConsiderIp(false)
              .AddCellTower(
                  new CellTower.CellTowerBuilder()
                      .CellId(39627456)
                      .LocationAreaCode(40495)
                      .MobileCountryCode(310)
                      .MobileNetworkCode(260)
                      .createCellTower())
              .CreatePayload()
              .await();

      assertNotNull(result.toString());

      JSONObject body = sc.requestBody();
      assertEquals(false, body.get("considerIp"));
      JSONObject cellTower = body.getJSONArray("cellTowers").getJSONObject(0);
      assertEquals(39627456, cellTower.get("cellId"));
      assertEquals(40495, cellTower.get("locationAreaCode"));
      assertEquals(310, cellTower.get("mobileCountryCode"));
      assertEquals(260, cellTower.get("mobileNetworkCode"));
      assertEquals("accuracy", 658.0, result.accuracy, 0.00001);
      assertEquals("lat", 37.42659, result.location.lat, 0.00001);
      assertEquals("lng", -122.07266190000001, result.location.lng, 0.00001);
    }
  }

  @Test
  public void testAlternatePayloadBuilderGeolocation() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(geolocationAlternatePayloadBuilder)) {
      GeolocationPayload payload =
          new GeolocationPayload.GeolocationPayloadBuilder()
              .ConsiderIp(false)
              .AddCellTower(
                  new CellTower.CellTowerBuilder()
                      .CellId(39627456)
                      .LocationAreaCode(40495)
                      .MobileCountryCode(310)
                      .MobileNetworkCode(260)
                      .createCellTower())
              .createGeolocationPayload();

      GeolocationResult result = GeolocationApi.geolocate(sc.context, payload).await();
      assertNotNull(result.toString());
      JSONObject body = sc.requestBody();
      assertEquals(false, body.get("considerIp"));
      JSONObject cellTower = body.getJSONArray("cellTowers").getJSONObject(0);
      assertEquals(39627456, cellTower.get("cellId"));
      assertEquals(40495, cellTower.get("locationAreaCode"));
      assertEquals(310, cellTower.get("mobileCountryCode"));
      assertEquals(260, cellTower.get("mobileNetworkCode"));
      assertEquals("accuracy", 658.0, result.accuracy, 0.00001);
      assertEquals("lat", 37.42659, result.location.lat, 0.00001);
      assertEquals("lng", -122.07266190000001, result.location.lng, 0.00001);
    }
  }

  @Test
  public void testMaximumCellTowerGeolocation() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(geolocationMaximumCellTower)) {
      GeolocationResult result =
          GeolocationApi.newRequest(sc.context)
              .ConsiderIp(false)
              .HomeMobileCountryCode(310)
              .HomeMobileNetworkCode(260)
              .RadioType("gsm")
              .Carrier("Vodafone")
              .AddCellTower(
                  new CellTower.CellTowerBuilder()
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

      assertNotNull(result.toString());

      JSONObject body = sc.requestBody();
      assertEquals(false, body.get("considerIp"));
      assertEquals(310, body.get("homeMobileCountryCode"));
      assertEquals(260, body.get("homeMobileNetworkCode"));
      assertEquals("gsm", body.get("radioType"));
      assertEquals("Vodafone", body.get("carrier"));
      JSONObject cellTower = body.getJSONArray("cellTowers").getJSONObject(0);
      assertEquals(39627456, cellTower.get("cellId"));
      assertEquals(40495, cellTower.get("locationAreaCode"));
      assertEquals(310, cellTower.get("mobileCountryCode"));
      assertEquals(260, cellTower.get("mobileNetworkCode"));
      assertEquals(0, cellTower.get("age"));
      assertEquals(-103, cellTower.get("signalStrength"));
      assertEquals(15, cellTower.get("timingAdvance"));
      assertEquals("accuracy", 1145.0, result.accuracy, 0.00001);
      assertEquals("lat", 37.4248297, result.location.lat, 0.00001);
      assertEquals("lng", -122.07346549999998, result.location.lng, 0.00001);
    }
  }

  @Test
  public void testNoPayloadGeolocation0() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(geolocationBasic)) {
      GeolocationPayload payload =
          new GeolocationPayload.GeolocationPayloadBuilder().createGeolocationPayload();

      GeolocationResult result = GeolocationApi.geolocate(sc.context, payload).await();
      assertNotNull(result);
      assertNotNull(result.toString());
      assertNotNull(result.location);
    }
  }

  @Test
  public void testNoPayloadGeolocation1() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(geolocationBasic)) {
      GeolocationResult result = GeolocationApi.newRequest(sc.context).CreatePayload().await();

      assertNotNull(result);
      assertNotNull(result.toString());
      assertNotNull(result.location);
    }
  }

  @Test(expected = NotFoundException.class)
  public void testNotFoundGeolocation() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            ""
                + "{\n"
                + " \"error\": {\n"
                + "  \"errors\": [\n"
                + "   {\n"
                + "    \"domain\": \"geolocation\",\n"
                + "    \"reason\": \"notFound\""
                + "   }\n"
                + "  ],\n"
                + "  \"code\": 404\n"
                + " }\n"
                + "}")) {
      GeolocationApi.newRequest(sc.context).ConsiderIp(false).CreatePayload().await();
    }
  }

  @Test(expected = InvalidRequestException.class)
  public void testInvalidArgumentGeolocation() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            ""
                + "{\n"
                + " \"error\": {\n"
                + "  \"errors\": [\n"
                + "   {\n"
                + "    \"domain\": \"global\",\n"
                + "    \"reason\": \"parseError\",\n"
                + "    \"message\": \"Parse Error\"\n"
                + "   }\n"
                + "  ],\n"
                + "  \"code\": 400,\n"
                + "  \"message\": \"Parse Error\"\n"
                + " }\n"
                + "}")) {
      GeolocationApi.newRequest(sc.context).HomeMobileCountryCode(-310).CreatePayload().await();
    }
  }
}
