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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.maps.errors.ZeroResultsException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Category(LargeTests.class)
public class TimeZoneApiTest extends AuthenticatedTest {

  private GeoApiContext context;

  private LatLng sydney;

  public TimeZoneApiTest(GeoApiContext context) {
    this.context = context.setQueryRateLimit(3)
        .setConnectTimeout(1, TimeUnit.SECONDS)
        .setReadTimeout(1, TimeUnit.SECONDS)
        .setWriteTimeout(1, TimeUnit.SECONDS);
  }

  @Before
  public void setUp() throws Exception {
    if (sydney == null) {
      GeocodingResult[] results = GeocodingApi.geocode(context, "Sydney").await();
      sydney = results[0].geometry.location;
    }
  }


  @Test
  public void testGetTimeZone() throws Exception {
    TimeZone tz = TimeZoneApi.getTimeZone(context, sydney).await();
    assertNotNull(tz);
    assertEquals(TimeZone.getTimeZone("Australia/Sydney"), tz);

    // GMT+10
    assertEquals(36000000, tz.getRawOffset());
    // DST is +1h
    assertEquals(3600000, tz.getDSTSavings());

    assertTrue(tz.inDaylightTime(new Date(1388494800000L)));
  }

  @Test(expected = ZeroResultsException.class)
  public void testNoResult() throws Exception {
    TimeZone resp = TimeZoneApi.getTimeZone(context, new LatLng(0, 0)).awaitIgnoreError();
    assertNull(resp);

    TimeZoneApi.getTimeZone(context, new LatLng(0, 0)).await();
  }
}
