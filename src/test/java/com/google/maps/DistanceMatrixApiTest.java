package com.google.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;
import java.util.List;

@Category(MediumTests.class)
public class DistanceMatrixApiTest {

  private GeoApiContext context = new GeoApiContext().setApiKey("AIzaFakeKey");

  @Test(expected = IllegalArgumentException.class)
  public void testTravelModeRejectsInvalidMode() throws Exception {
    String[] origins = {"Perth, WA"};
    String[] destinations = {"Sydney, NSW"};

    DistanceMatrixApi.newRequest(context)
        .origins(origins)
        .destinations(destinations)
        .mode(TravelMode.TRANSIT)
        .await();
  }

  @Test
  public void testLatLngOriginDestinations() throws Exception {
    MockResponse response = new MockResponse();
    response.setBody("");
    MockWebServer server = new MockWebServer();
    server.enqueue(response);
    server.play();
    context.setBaseUrl(server.getUrl("").toString());

    DistanceMatrixApi.newRequest(context)
        .origins(new LatLng(-31.9522, 115.8589),
                 new LatLng(-37.8136, 144.9631))
        .destinations(new LatLng(-25.344677, 131.036692),
                      new LatLng(-13.092297, 132.394057))
        .awaitIgnoreError();

    List<NameValuePair> actualParams =
        parseQueryParamsFromRequestLine(server.takeRequest().getRequestLine());
    assertParamValue("-31.952200,115.858900|-37.813600,144.963100", "origins", actualParams);
    assertParamValue("-25.344677,131.036692|-13.092297,132.394057", "destinations", actualParams);

    server.shutdown();
  }

  private void assertParamValue(String expectedValue, String paramName, List<NameValuePair> params)
      throws Exception {
    boolean paramFound = false;
    for (NameValuePair pair : params) {
      if (pair.getName().equals(paramName)) {
        paramFound = true;
        assertEquals(expectedValue, pair.getValue());
      }
    }
    assertTrue(paramFound);
  }

  private List<NameValuePair> parseQueryParamsFromRequestLine(String requestLine) throws Exception {
    // Extract the URL part from the HTTP request line
    String[] chunks = requestLine.split("\\s");
    String url = chunks[1];

    return URLEncodedUtils.parse(new URI(url), "UTF-8");
  }
}
