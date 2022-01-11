package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.RoadsApi.SpeedLimitsResponse;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.StringJoin;
import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedSpeedLimitResult;

/** A request to the speed limits API (part of Roads API). */
public class SpeedLimitsApiRequest
    extends PendingResultBase<SnappedSpeedLimitResult, SpeedLimitsApiRequest, SpeedLimitsResponse> {

  private static final ApiConfig SPEEDS_API_CONFIG =
      new ApiConfig("/v1/speedLimits")
          .hostName(RoadsApi.API_BASE_URL)
          .supportsClientId(false)
          .fieldNamingPolicy(FieldNamingPolicy.IDENTITY);

  public SpeedLimitsApiRequest(GeoApiContext context) {
    super(context, SPEEDS_API_CONFIG, SpeedLimitsResponse.class);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("path") && !params().containsKey("placeId")) {
      throw new IllegalArgumentException("Request must contain either 'path' or 'placeId'");
    }
  }

  /**
   * A list of up to 100 lat/long pairs representing a path.
   *
   * @param path the path
   * @return a {@code SpeedLimitsApiRequest} for call chaining.
   */
  public SpeedLimitsApiRequest path(LatLng... path) {
    return param("path", StringJoin.join('|', path));
  }

  /**
   * A list of place ID/s representing one or more road segments.
   *
   * @param placeIds the place ID/s
   * @return a {@code SpeedLimitsApiRequest} for call chaining.
   */
  public SpeedLimitsApiRequest placeIds(String... placeIds) {
    for (String placeId : placeIds) {
      paramAddToList("placeId", placeId);
    }
    return this;
  }
}
