package com.google.maps.model;

import com.google.maps.internal.PolylineEncoding;

import java.util.List;

/**
 * Encoded Polylines are used by the API to represent paths.
 *
 * <p>See {@url https://developers.google.com/maps/documentation/utilities/polylinealgorithm} for
 * more detail on the protocol.
 */
public class EncodedPolyline {
  private String points;

  /**
   * @param encodedPath A string representation of a path, encoded with the Polyline Algorithm.
   */
  public EncodedPolyline(String encodedPoints) {
    this.points = encodedPoints;
  }

  /**
   * @param points A path as a collection of {@code LatLng} points.
   */
  public EncodedPolyline(List<LatLng> points) {
    this.points = PolylineEncoding.encode(points);
  }

  public String getEncodedPath() {
    return points;
  }

  public List<LatLng> decodePath() {
    return PolylineEncoding.decode(points);
  }
}
