package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.RoadsApi.RoadsResponse;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.StringJoin;
import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;

/** A request to the snap to roads API (part of Roads API). */
public class SnapToRoadsApiRequest
    extends PendingResultBase<SnappedPoint[], SnapToRoadsApiRequest, RoadsResponse> {

  private static final ApiConfig SNAP_TO_ROADS_API_CONFIG =
      new ApiConfig("/v1/snapToRoads")
          .hostName(RoadsApi.API_BASE_URL)
          .supportsClientId(false)
          .fieldNamingPolicy(FieldNamingPolicy.IDENTITY);

  public SnapToRoadsApiRequest(GeoApiContext context) {
    super(context, SNAP_TO_ROADS_API_CONFIG, RoadsResponse.class);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("path")) {
      throw new IllegalArgumentException("Request must contain 'path");
    }
  }

  /**
   * The path from which to snap to roads.
   *
   * @param path the path to be snapped
   * @return returns this {@code SnapToRoadsApiRequest} for call chaining.
   */
  public SnapToRoadsApiRequest path(LatLng... path) {
    return param("path", StringJoin.join('|', path));
  }

  /**
   * Whether to interpolate a path to include all points forming the full road-geometry.
   *
   * @param interpolate if the points should be interpolated or not
   * @return returns this {@code SnapToRoadsApiRequest} for call chaining.
   */
  public SnapToRoadsApiRequest interpolate(boolean interpolate) {
    return param("interpolate", String.valueOf(interpolate));
  }
}
