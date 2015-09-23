package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;

public class QueryAutocompleteRequest
    extends PendingResultBase<QueryAutocompletePrediction[], QueryAutocompleteRequest, QueryAutocompleteRequest.Response> {

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/place/queryautocomplete/json")
      .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  protected QueryAutocompleteRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  @Override
  protected void validateRequest() {

  }

  public static class Response implements ApiResponse<QueryAutocompletePrediction[]> {
    @Override
    public boolean successful() {
      return false;
    }

    @Override
    public QueryAutocompletePrediction[] getResult() {
      return new QueryAutocompletePrediction[0];
    }

    @Override
    public ApiException getError() {
      return null;
    }
  }
}
