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

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;
import com.google.maps.model.SnappedSpeedLimitResponse;
import com.google.maps.model.SpeedLimit;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.concurrent.TimeUnit;

@Category(LargeTests.class)
public class RoadsApiIntegrationTest extends KeyOnlyAuthenticatedTest {
  private GeoApiContext context;

  public RoadsApiIntegrationTest(GeoApiContext context) {
    this.context = context
        .setConnectTimeout(2, TimeUnit.SECONDS)
        .setReadTimeout(2, TimeUnit.SECONDS)
        .setWriteTimeout(2, TimeUnit.SECONDS);
  }

  @Test
  public void testSnapToRoad() throws Exception {
    SnappedPoint[] points = RoadsApi.snapToRoads(context,
        false,
        new LatLng(-33.865382, 151.192861),
        new LatLng(-33.865837, 151.193376),
        new LatLng(-33.866745, 151.19373),
        new LatLng(-33.867128, 151.19344),
        new LatLng(-33.867547, 151.193676),
        new LatLng(-33.867841, 151.194137),
        new LatLng(-33.868224, 151.194116)).await();

    assertNotNull(points);
    assertEquals(7, points.length);
    assertNotNull(points[0].location.lat);
    assertNotNull(points[0].location.lng);
    assertNotNull(points[0].placeId);
  }

  @Test
  public void testSnapToRoadProvidesOriginalIndexWithInterpolation() throws Exception {
    SnappedPoint[] points = RoadsApi.snapToRoads(context,
        false,
        new LatLng(-33.865382, 151.192861),
        new LatLng(-33.865837, 151.193376),
        new LatLng(-33.866745, 151.19373),
        new LatLng(-33.867128, 151.19344),
        new LatLng(-33.867547, 151.193676),
        new LatLng(-33.867841, 151.194137),
        new LatLng(-33.868224, 151.194116)).await();

    int currentIndex = 0;
    // Interpolated points need to have an incrementing originalIndex, or -1.
    for (SnappedPoint point : points) {
      assertThat(point.originalIndex, either(is(-1)).or(is(currentIndex)));
      if (point.originalIndex != -1) {
        currentIndex++;
      }
    }

    assertEquals(7, currentIndex);  // 7 latlngs, but we ++ after each, so index=7
  }

  @Test
  public void testSpeedLimitsWithLatLngs() throws Exception {
    SpeedLimit[] speeds = RoadsApi.speedLimits(context,
        new LatLng(-33.865382, 151.192861),
        new LatLng(-33.865837, 151.193376),
        new LatLng(-33.866745, 151.19373),
        new LatLng(-33.867128, 151.19344),
        new LatLng(-33.867547, 151.193676),
        new LatLng(-33.867841, 151.194137),
        new LatLng(-33.868224, 151.194116)).await();

    assertNotNull(speeds);
    assertEquals(7, speeds.length);

    for (SpeedLimit speed : speeds) {
      assertNotNull(speed.placeId);
      assertTrue(speed.speedLimit > 0);
    }
  }

  @Test
  public void testSpeedLimitsWithUsaLatLngs() throws Exception {
    SpeedLimit[] speeds = RoadsApi.speedLimits(context,
        new LatLng(33.777489, -84.397805),
        new LatLng(33.777550, -84.395700),
        new LatLng(33.776900, -84.393110),
        new LatLng(33.776860, -84.389550),
        new LatLng(33.775491, -84.388797),
        new LatLng(33.773250, -84.388840),
        new LatLng(33.771991, -84.388840)).await();

    assertNotNull(speeds);
    assertEquals(7, speeds.length);

    for (SpeedLimit speed : speeds) {
      assertNotNull(speed.placeId);
      assertTrue(speed.speedLimit > 0);
    }
  }

  @Test
  public void testSpeedLimitsWithPlaceIds() throws Exception {
    SpeedLimit[] speeds = RoadsApi.speedLimits(context,
        "ChIJOXE4GDauEmsRbeangKX--a0",
        "ChIJOXE4GDauEmsRbeangKX--a0",
        "ChIJua_ZPTauEmsRwK6LHmdHDH4").await();

    assertNotNull(speeds);
    assertEquals(3, speeds.length);
    assertEquals("ChIJua_ZPTauEmsRwK6LHmdHDH4", speeds[2].placeId);

    for (SpeedLimit speed : speeds) {
      assertTrue(speed.speedLimit > 0);
    }
  }

  @Test
  public void testSnappedSpeedLimitRequest() throws Exception {
    SnappedSpeedLimitResponse response = RoadsApi.snappedSpeedLimits(context,
        new LatLng(-33.865382, 151.192861),
        new LatLng(-33.865837, 151.193376),
        new LatLng(-33.866745, 151.19373),
        new LatLng(-33.867128, 151.19344),
        new LatLng(-33.867547, 151.193676),
        new LatLng(-33.867841, 151.194137),
        new LatLng(-33.868224, 151.194116)).await();

    SnappedPoint[] points = response.snappedPoints;
    SpeedLimit[] speeds = response.speedLimits;

    assertEquals(7, points.length);
    assertEquals(7, speeds.length);
  }

  @Test
  public void testSnappedSpeedLimitRequestUsa() throws Exception {
    SnappedSpeedLimitResponse response = RoadsApi.snappedSpeedLimits(context,
        new LatLng(33.777489, -84.397805),
        new LatLng(33.777550, -84.395700),
        new LatLng(33.776900, -84.393110),
        new LatLng(33.776860, -84.389550),
        new LatLng(33.775491, -84.388797),
        new LatLng(33.773250, -84.388840),
        new LatLng(33.771991, -84.388840)).await();

    SnappedPoint[] points = response.snappedPoints;
    SpeedLimit[] speeds = response.speedLimits;

    assertEquals(7, points.length);
    assertEquals(7, speeds.length);
  }
}
