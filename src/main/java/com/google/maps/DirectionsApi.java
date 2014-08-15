package com.google.maps;

import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.StringJoin.UrlValue;
import com.google.maps.model.DirectionsRoute;

/**
 * <p>The Google Directions API is a service that calculates directions between locations
 * using an HTTP request. You can search for directions for several modes of transportation,
 * include transit, driving, walking or cycling. Directions may specify origins, destinations
 * and waypoints either as text strings (e.g. "Chicago, IL" or "Darwin, NT, Australia") or as
 * latitude/longitude coordinates. The Directions API can return multi-part directions using
 * a series of waypoints.
 * <p>See <a href="https://developers.google.com/maps/documentation/directions/">documentation</a>.
 */
public class DirectionsApi {
  static final String BASE = "/maps/api/directions/json";

  private DirectionsApi() {
  }

  public static DirectionsApiRequest newRequest(GeoApiContext context) {
    return new DirectionsApiRequest(context);
  }

  public static DirectionsApiRequest getDirections(GeoApiContext context,
                                                   String origin,
                                                   String destination) {
    return newRequest(context).origin(origin).destination(destination);
  }

  static class Response implements ApiResponse<DirectionsRoute[]> {
    public String status;
    public String errorMessage;
    public DirectionsRoute[] routes;

    @Override
    public boolean successful() {
      return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }

    @Override
    public DirectionsRoute[] getResult() {
      return routes;
    }

    @Override
    public ApiException getError() {
      if (successful()) {
        return null;
      }
      return ApiException.from(status, errorMessage);
    }
  }

  /**
   * Directions may be calculated that adhere to certain restrictions. This is configured by
   * calling {@link com.google.maps.DirectionsApiRequest#avoid} or
   * {@link com.google.maps.DistanceMatrixApiRequest#avoid}.
   *
   * @see <a href="https://developers.google.com/maps/documentation/directions/#Restrictions">
   *      Restrictions in the Directions API</a>
   * @see <a href="https://developers.google.com/maps/documentation/distancematrix/#Restrictions">
   *      Restrictions in the Distance Matrix API></a>
   */
  public enum RouteRestriction implements UrlValue {

    /**
     * {@code TOLLS} indicates that the calculated route should avoid toll roads/bridges.
     */
    TOLLS("tolls"),

    /**
     * {@code HIGHWAYS} indicates that the calculated route should avoid highways.
     */
    HIGHWAYS("highways"),

    /**
     * {@code FERRIES} indicates that the calculated route should avoid ferries.
     */
    FERRIES("ferries");

    private final String restriction;

    RouteRestriction(String restriction) {
      this.restriction = restriction;
    }

    @Override
    public String toString() {
      return restriction;
    }
  }
}
