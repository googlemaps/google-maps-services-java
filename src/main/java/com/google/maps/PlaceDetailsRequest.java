package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.internal.ApiConfig;
import com.google.maps.model.PlaceDetails;

public class PlaceDetailsRequest
    extends PendingResultBase<PlaceDetails, PlaceDetailsRequest, PlacesApi.PlaceDetailsResponse> {

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/place/details/json")
      .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  public PlaceDetailsRequest(GeoApiContext context) {
    super(context, API_CONFIG, PlacesApi.PlaceDetailsResponse.class);
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
}
