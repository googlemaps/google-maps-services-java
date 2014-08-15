package com.google.maps.model;

import org.junit.Assert;

/**
 * Testing infrastructure for {@see LatLng}.
 */
public class LatLngAssert {

  private LatLngAssert() {
  }

  public static void assertEquals(LatLng a, LatLng b, double epsilon) {
    Assert.assertEquals(a.lat, b.lat, epsilon);
    Assert.assertEquals(a.lng, b.lng, epsilon);
  }
}
