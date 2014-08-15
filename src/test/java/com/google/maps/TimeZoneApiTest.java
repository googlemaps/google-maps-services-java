package com.google.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.maps.errors.ZeroResultsException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.testing.testsize.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class) @LargeTest
public class TimeZoneApiTest {

  private static GeoApiContext context = new GeoApiContext(3)
        .setApiKey("AIzaSyAZ0_yiPw2Zp2huKxug49ZYi-pytL6NZ-c")
        .setConnectTimeout(1, TimeUnit.SECONDS)
        .setReadTimeout(1, TimeUnit.SECONDS)
        .setWriteTimeout(1, TimeUnit.SECONDS);

  private LatLng sydney;

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
