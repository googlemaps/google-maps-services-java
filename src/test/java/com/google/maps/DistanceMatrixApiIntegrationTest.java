package com.google.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.google.testing.testsize.LargeTest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class) @LargeTest
public class DistanceMatrixApiIntegrationTest {

  private GeoApiContext context;

  @Before
  public void setUp() {
    context = new GeoApiContext()
        .setApiKey("AIzaSyAZ0_yiPw2Zp2huKxug49ZYi-pytL6NZ-c") // TODO(macd): store elsewhere
        .setConnectTimeout(1, TimeUnit.SECONDS)
        .setReadTimeout(1, TimeUnit.SECONDS)
        .setWriteTimeout(1, TimeUnit.SECONDS);
  }

  @Test
  public void testGetDistanceMatrixWithBasicStringParams() throws Exception {
    String[] origins = new String[] {
        "Perth, Australia", "Sydney, Australia", "Melbourne, Australia",
        "Adelaide, Australia", "Brisbane, Australia", "Darwin, Australia",
        "Hobart, Australia", "Canberra, Australia"
    };
    String[] destinations = new String[] {
        "Uluru, Australia", "Kakadu, Australia", "Blue Mountains, Australia",
        "Bungle Bungles, Australia", "The Pinnacles, Australia"
    };
    DistanceMatrix matrix =
        DistanceMatrixApi.getDistanceMatrix(context, origins, destinations).await();

    // Rows length will match the number of origin elements, regardless of whether they're routable.
    assertEquals(8, matrix.rows.length);
    assertEquals(5, matrix.rows[0].elements.length);
  }

  @Test
  public void testNewRequestWithAllPossibleParams() throws Exception {
    String[] origins = new String[] {
        "Perth, Australia", "Sydney, Australia", "Melbourne, Australia",
        "Adelaide, Australia", "Brisbane, Australia", "Darwin, Australia",
        "Hobart, Australia", "Canberra, Australia"
    };
    String[] destinations = new String[] {
        "Uluru, Australia", "Kakadu, Australia", "Blue Mountains, Australia",
        "Bungle Bungles, Australia", "The Pinnacles, Australia"
    };

    DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
        .origins(origins)
        .destinations(destinations)
        .mode(TravelMode.DRIVING)
        .language("en-AU")
        .avoid(RouteRestriction.TOLLS)
        .units(Unit.IMPERIAL)
        .departureTime(new DateTime().plusDays(5))
        .await();

    assertEquals(8, matrix.rows.length);
    assertEquals(5, matrix.rows[0].elements.length);
    assertTrue(matrix.rows[0].elements[0].distance.humanReadable.endsWith("mi"));
  }

  /**
   * Test the language parameter.
   *
   * <p>Sample request:
   * <a href="http://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Victoria+BC&mode=bicycling&language=fr-FR">
   * origins: Vancouver BC|Seattle, destinations: San Francisco|Victoria BC, mode: bicycling,
   * language: french</a>.
   */
  @Test
  public void testLanguageParameter() throws Exception {
    DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
        .origins("Vancouver BC", "Seattle")
        .destinations("San Francisco", "Victoria BC")
        .mode(TravelMode.BICYCLING)
        .language("fr-FR")
        .await();

    assertNotNull(matrix);
  }



}
