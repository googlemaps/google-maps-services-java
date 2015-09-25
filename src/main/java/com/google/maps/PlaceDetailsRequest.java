package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.PlaceDetails;

public class PlaceDetailsRequest
    extends PendingResultBase<PlaceDetails, PlaceDetailsRequest, PlaceDetailsRequest.Response> {

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/place/details/json")
      .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  public PlaceDetailsRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  /** Get the Place Details for the specified Place ID. Required. */
  PlaceDetailsRequest placeId(String placeId) {
    return param("placeid", placeId);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("placeid")) {
      throw new IllegalArgumentException("Request must contain  'place_id'.");
    }
  }

  static class Response implements ApiResponse<PlaceDetails> {
    public String status;
    public PlaceDetails result;
    public String[] htmlAttributions;
    public String errorMessage;

    @Override
    public boolean successful() {
      return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }

    @Override
    public PlaceDetails getResult() {
      if (result != null) {
        result.htmlAttributions = htmlAttributions;
      }
      return result;
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