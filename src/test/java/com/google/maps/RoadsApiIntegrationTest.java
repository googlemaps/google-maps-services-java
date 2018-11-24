/*
 * Copyright 2015 Google Inc. All rights reserved.
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
import static com.google.maps.internal.StringJoin.join;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;
import com.google.maps.model.SnappedSpeedLimitResponse;
import com.google.maps.model.SpeedLimit;
import java.util.Arrays;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(LargeTests.class)
public class RoadsApiIntegrationTest {

  private final String snapToRoadResponse;
  private final String speedLimitsResponse;
  private final String speedLimitsUSAResponse;
  private final String speedLimitsWithPlaceIdsResponse;
  private final String snappedSpeedLimitResponse;
  private final String nearestRoadsResponse;

  public RoadsApiIntegrationTest() {
    snapToRoadResponse = retrieveBody("RoadsApiSnapToRoadResponse.json");
    speedLimitsResponse = retrieveBody("RoadsApiSpeedLimitsResponse.json");
    speedLimitsUSAResponse = retrieveBody("RoadsApiSpeedLimitsUSAResponse.json");
    speedLimitsWithPlaceIdsResponse = retrieveBody("RoadsApiSpeedLimitsWithPlaceIds.json");
    snappedSpeedLimitResponse = retrieveBody("RoadsApiSnappedSpeedLimitResponse.json");
    nearestRoadsResponse = retrieveBody("RoadsApiNearestRoadsResponse.json");
  }

  @Test
  public void testSnapToRoad() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(snapToRoadResponse)) {
      LatLng[] path =
          new LatLng[] {
            new LatLng(-33.865382, 151.192861),
            new LatLng(-33.865837, 151.193376),
            new LatLng(-33.866745, 151.19373),
            new LatLng(-33.867128, 151.19344),
            new LatLng(-33.867547, 151.193676),
            new LatLng(-33.867841, 151.194137),
            new LatLng(-33.868224, 151.194116)
          };
      SnappedPoint[] points = RoadsApi.snapToRoads(sc.context, false, path).await();

      assertNotNull(Arrays.toString(points));
      sc.assertParamValue(join('|', path), "path");
      sc.assertParamValue("false", "interpolate");
      assertEquals(7, points.length);
      assertEquals(-33.86523340256843, points[0].location.lat, 0.0001);
      assertEquals(151.19288612197704, points[0].location.lng, 0.0001);
      assertEquals("ChIJjXkMCDauEmsRp5xab4Ske6k", points[0].placeId);
    }
  }

  @Test
  public void testSpeedLimitsWithLatLngs() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(speedLimitsResponse)) {
      LatLng[] path =
          new LatLng[] {
            new LatLng(-33.865382, 151.192861),
            new LatLng(-33.865837, 151.193376),
            new LatLng(-33.866745, 151.19373),
            new LatLng(-33.867128, 151.19344),
            new LatLng(-33.867547, 151.193676),
            new LatLng(-33.867841, 151.194137),
            new LatLng(-33.868224, 151.194116)
          };
      SpeedLimit[] speeds = RoadsApi.speedLimits(sc.context, path).await();

      assertNotNull(Arrays.toString(speeds));
      assertEquals("/v1/speedLimits", sc.path());
      sc.assertParamValue(join('|', path), "path");
      assertEquals(7, speeds.length);

      for (SpeedLimit speed : speeds) {
        assertNotNull(speed.placeId);
        assertEquals(40.0, speed.speedLimit, 0.001);
      }
    }
  }

  @Test
  public void testSpeedLimitsWithUsaLatLngs() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(speedLimitsUSAResponse)) {
      LatLng[] path =
          new LatLng[] {
            new LatLng(33.777489, -84.397805),
            new LatLng(33.777550, -84.395700),
            new LatLng(33.776900, -84.393110),
            new LatLng(33.776860, -84.389550),
            new LatLng(33.775491, -84.388797),
            new LatLng(33.773250, -84.388840),
            new LatLng(33.771991, -84.388840)
          };
      SpeedLimit[] speeds = RoadsApi.speedLimits(sc.context, path).await();

      assertNotNull(Arrays.toString(speeds));
      assertEquals("/v1/speedLimits", sc.path());
      sc.assertParamValue(join('|', path), "path");
      assertEquals(7, speeds.length);

      for (SpeedLimit speed : speeds) {
        assertNotNull(speed.placeId);
        assertTrue(speed.speedLimit > 0);
      }
    }
  }

  @Test
  public void testSpeedLimitsWithPlaceIds() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(speedLimitsWithPlaceIdsResponse)) {
      String[] placeIds =
          new String[] {
            "ChIJrfDjZYoE9YgRLpb3bOhcPno",
            "ChIJyU-E2mEE9YgRftyNXxcfQYw",
            "ChIJc0BrC2EE9YgR71DvaFzNgrA"
          };
      SpeedLimit[] speeds = RoadsApi.speedLimits(sc.context, placeIds).await();

      assertNotNull(Arrays.toString(speeds));
      assertEquals("/v1/speedLimits", sc.path());
      assertEquals(3, speeds.length);
      assertEquals("ChIJc0BrC2EE9YgR71DvaFzNgrA", speeds[2].placeId);

      for (SpeedLimit speed : speeds) {
        assertTrue(speed.speedLimit > 0);
      }
    }
  }

  @Test
  public void testSnappedSpeedLimitRequest() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(snappedSpeedLimitResponse)) {
      LatLng[] path =
          new LatLng[] {
            new LatLng(-33.865382, 151.192861),
            new LatLng(-33.865837, 151.193376),
            new LatLng(-33.866745, 151.19373),
            new LatLng(-33.867128, 151.19344),
            new LatLng(-33.867547, 151.193676),
            new LatLng(-33.867841, 151.194137),
            new LatLng(-33.868224, 151.194116)
          };
      SnappedSpeedLimitResponse response = RoadsApi.snappedSpeedLimits(sc.context, path).await();

      assertNotNull(response.toString());
      assertEquals("/v1/speedLimits", sc.path());
      sc.assertParamValue(join('|', path), "path");
      assertEquals(path.length, response.snappedPoints.length);
      assertEquals(path.length, response.speedLimits.length);
    }
  }

  @Test
  public void testNearestRoads() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(nearestRoadsResponse)) {
      LatLng[] path =
          new LatLng[] {
            new LatLng(-33.865382, 151.192861),
            new LatLng(-33.865837, 151.193376),
            new LatLng(-33.866745, 151.19373),
            new LatLng(-33.867128, 151.19344),
            new LatLng(-33.867547, 151.193676),
            new LatLng(-33.867841, 151.194137),
            new LatLng(-33.868224, 151.194116)
          };
      SnappedPoint[] points = RoadsApi.nearestRoads(sc.context, path).await();

      assertNotNull(Arrays.toString(points));
      assertEquals("/v1/nearestRoads", sc.path());
      sc.assertParamValue(join('|', path), "points");
      assertEquals(13, points.length);
      assertEquals(-33.86543615612047, points[0].location.lat, 0.0001);
      assertEquals(151.1930101572747, points[0].location.lng, 0.0001);
      assertEquals("ChIJ0XXACjauEmsRUduC5Wd9ARM", points[0].placeId);
    }
  }
}
