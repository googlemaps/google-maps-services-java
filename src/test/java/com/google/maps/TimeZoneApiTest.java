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
import com.google.maps.model.LatLng;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(MediumTests.class)
public class TimeZoneApiTest {

  @Test
  public void testGetTimeZone() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(
            "\n"
                + "{\n"
                + "   \"dstOffset\" : 0,\n"
                + "   \"rawOffset\" : 36000,\n"
                + "   \"status\" : \"OK\",\n"
                + "   \"timeZoneId\" : \"Australia/Sydney\",\n"
                + "   \"timeZoneName\" : \"Australian Eastern Standard Time\"\n"
                + "}\n")) {
      LatLng sydney = new LatLng(-33.8688, 151.2093);
      TimeZone tz = TimeZoneApi.getTimeZone(sc.context, sydney).await();

      assertNotNull(tz);
      assertEquals(TimeZone.getTimeZone("Australia/Sydney"), tz);

      // GMT+10
      assertEquals(36000000, tz.getRawOffset());
      // DST is +1h
      assertEquals(3600000, tz.getDSTSavings());

      assertTrue(tz.inDaylightTime(new Date(1388494800000L)));

      sc.assertParamValue(sydney.toUrlValue(), "location");
    }
  }

  @Test(expected = ZeroResultsException.class)
  public void testNoResult() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext("\n{\n   \"status\" : \"ZERO_RESULTS\"\n}\n")) {
      TimeZone resp = TimeZoneApi.getTimeZone(sc.context, new LatLng(0, 0)).awaitIgnoreError();
      assertNull(resp);

      sc.assertParamValue("0.00000000,0.00000000", "location");

      try (LocalTestServerContext sc2 =
          new LocalTestServerContext("\n{\n   \"status\" : \"ZERO_RESULTS\"\n}\n")) {
        TimeZoneApi.getTimeZone(sc2.context, new LatLng(0, 0)).await();
      }
    }
  }
}
