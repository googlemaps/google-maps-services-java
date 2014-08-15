package com.google.maps;

import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;

import java.util.Locale;

/**
 * <p>The Google Elevation API provides you a simple interface to query locations
 * on the earth for elevation data. Additionally, you may request sampled elevation
 * data along paths, allowing you to calculate elevation changes along routes.
 * <p>See <a href="https://developers.google.com/maps/documentation/elevation/">documentation</a>.
 */
public class ElevationApi {
  private static final String BASE = "/maps/api/elevation/json";

  private ElevationApi() {
  }

  /**
   * See <a href="https://developers.google.com/maps/documentation/elevation/#Locations">documentation</a>.
   */
  public static PendingResult<ElevationResult[]> getByPoints(GeoApiContext context,
      LatLng... points) {
    return context.get(MultiResponse.class,
        BASE,
        "locations", shortestParam(points));
  }

  /**
   * See <a href="https://developers.google.com/maps/documentation/elevation/#Paths">documentation</a>.
   */
  public static PendingResult<ElevationResult[]> getByPath(GeoApiContext context,
                                                           int samples,
                                                           LatLng... path) {
    return context.get(MultiResponse.class,
        BASE,
        "samples", String.valueOf(samples),
        "path", shortestParam(path));
  }

  /**
   * See <a href="https://developers.google.com/maps/documentation/elevation/#Paths">documentation</a>.
   */
  public static PendingResult<ElevationResult[]> getByPath(GeoApiContext context,
                                                           int samples,
                                                           EncodedPolyline encodedPolyline) {
    return context.get(MultiResponse.class,
        BASE,
        "samples", String.valueOf(samples),
        "path", "enc:" + encodedPolyline.getEncodedPath());
  }

  /**
   * Chooses the shortest param (only a guess, since the
   * length is different after URL encoding).
   */
  private static String shortestParam(LatLng[] points) {
    String joined = joinPoints(points, '|');
    String encoded = "enc:" + PolylineEncoding.encode(points);
    return joined.length() < encoded.length() ? joined : encoded;
  }

  private static String joinPoints(LatLng[] points, char separator) {
    StringBuilder sb = new StringBuilder();
    for (LatLng p : points) {
      sb.append(p);
      sb.append(separator);
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  /**
   * Retrieve the elevation of a single point.
   *
   * <p>For more detail, please see the
   * <a href="https://developers.google.com/maps/documentation/elevation/#Locations">documentation</a>.
   */
  public static PendingResult<ElevationResult> getByPoint(GeoApiContext context, LatLng point) {
    // Enforcing English locale on floating point number to string conversion to avoid
    // location parsing confusion on server side.
    String location = String.format(Locale.ENGLISH, "%f,%f", point.lat, point.lng);
    return context.get(SingularResponse.class, BASE, "locations", location);
  }

  private static class SingularResponse implements ApiResponse<ElevationResult> {
    public String status;
    public String errorMessage;
    public ElevationResult[] results;

    @Override
    public boolean successful() {
      return "OK".equals(status);
    }

    @Override
    public ElevationResult getResult() {
      return results[0];
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
   * Retrieve the elevations of an encoded polyline path.
   *
   * <p>See <a href="https://developers.google.com/maps/documentation/elevation/#Locations">documentation</a>.
   */
  public static PendingResult<ElevationResult[]> getByPoints(GeoApiContext context,
                                                      EncodedPolyline encodedPolyline) {
    return context.get(MultiResponse.class,
        BASE, "locations", "enc:" + encodedPolyline.getEncodedPath());
  }

  private static class MultiResponse implements ApiResponse<ElevationResult[]> {
    public String status;
    public String errorMessage;
    public ElevationResult[] results;

    @Override
    public boolean successful() {
      return "OK".equals(status);
    }

    @Override
    public ElevationResult[] getResult() {
      return results;
    }

    @Override
    public ApiException getError() {
      if (successful()) {
        return null;
      }
      return ApiException.from(status, errorMessage);
    }
  }
}
