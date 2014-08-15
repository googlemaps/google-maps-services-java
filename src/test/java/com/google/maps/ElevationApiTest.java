package com.google.maps;

import com.google.maps.errors.InvalidRequestException;
import com.google.maps.errors.RequestDeniedException;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;

@Category(MediumTests.class)
public class ElevationApiTest {

  private MockWebServer server = new MockWebServer();
  private GeoApiContext context = new GeoApiContext().setApiKey("AIzaFakeKey");

  @After
  public void tearDown() throws Exception {
    // Need to shut the server down here as we're using expected exceptions
    server.shutdown();
  }

  @Test(expected = InvalidRequestException.class)
  public void testGetByPointThrowsInvalidRequestExceptionFromResponse() throws Exception {
    // Queue up an invalid response
    MockResponse errorResponse = new MockResponse();
    errorResponse.setBody(""
        + "{\n"
        + "   \"routes\" : [],\n"
        + "   \"status\" : \"INVALID_REQUEST\"\n"
        + "}");
    server.enqueue(errorResponse);
    server.play();

    context.setBaseUrl(server.getUrl("").toString());
    // This should throw the InvalidRequestException
    ElevationApi.getByPoint(context, new LatLng(0, 0)).await();
  }

  @Test(expected = RequestDeniedException.class)
  public void testGetByPointsThrowsRequestDeniedExceptionFromResponse() throws Exception {
    // Queue up an invalid response
    MockResponse errorResponse = new MockResponse();
    errorResponse.setBody(""
        + "{\n"
        + "   \"routes\" : [],\n"
        + "   \"status\" : \"REQUEST_DENIED\",\n"
        + "   \"errorMessage\" : \"Can't do the thing\"\n"
        + "}");
    server.enqueue(errorResponse);
    server.play();

    context.setBaseUrl(server.getUrl("").toString());
    // This should throw the RequestDeniedException
    ElevationApi.getByPoints(context, new EncodedPolyline(Arrays.asList(new LatLng(0, 0)))).await();
  }
}
