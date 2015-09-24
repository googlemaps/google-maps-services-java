package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.PlacesSearchResponse;

public class TextSearchRequest
    extends PendingResultBase<PlacesSearchResponse, TextSearchRequest, TextSearchRequest.Response>{

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/place/textsearch/json")
      .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  protected TextSearchRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  public TextSearchRequest query(String query) {
    return param("query", query);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("query")) {
      throw new IllegalArgumentException("Request must contain  'query'.");
    }
  }

  public static class Response implements ApiResponse<PlacesSearchResponse> {

    public String status;
    public String errorMessage;

    @Override
    public boolean successful() {
      return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }

    @Override
    public PlacesSearchResponse getResult() {
      return null;
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
