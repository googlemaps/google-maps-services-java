package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

public class TextSearchRequest
    extends PendingResultBase<PlacesSearchResponse, TextSearchRequest, TextSearchRequest.Response>{

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/place/textsearch/json")
      .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  protected TextSearchRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  /**
   * query is the text string on which to search, for example: "restaurant".
   */
  public TextSearchRequest query(String query) {
    return param("query", query);
  }

  /**
   * location is the latitude/longitude around which to retrieve place information.
   */
  public TextSearchRequest location(LatLng location) {
    return param("location", location);
  }

  /**
   * radius defines the distance (in meters) within which to bias place results.
   */
  public TextSearchRequest radius(int radius) {
    if (radius > 50000) {
      throw new IllegalArgumentException("The maximum allowed radius is 50,000 meters.");
    }
    return param("radius", String.valueOf(radius));
  }

  /**
   * minprice restricts to places that are at least this price level.
   */
  public TextSearchRequest minprice(int priceLevel) {
    if (priceLevel < 0 || priceLevel > 4) {
      throw new IllegalArgumentException("minprice must be between 0 and 4, inclusive.");
    }

    return param("minprice", String.valueOf(priceLevel));
  }

  /**
   * maxprice restricts to places that are at most this price level.
   */
  public TextSearchRequest maxprice(int priceLevel) {
    if (priceLevel < 0 || priceLevel > 4) {
      throw new IllegalArgumentException("maxprice must be between 0 and 4, inclusive.");
    }

    return param("maxprice", String.valueOf(priceLevel));
  }

  /**
   * opennow returns only those places that are open for business at the time the query is sent.
   */
  public TextSearchRequest opennow(boolean opennow) {
    return param("opennow", String.valueOf(opennow));
  }

  /* We are explicitly not implementing the following parameters:
   *  - rankby
   *  - name
   *  - types
   *  - zagatselected
   */

  @Override
  protected void validateRequest() {
    if (!params().containsKey("query")) {
      throw new IllegalArgumentException("Request must contain 'query'.");
    }

    if (params().containsKey("location") && !params().containsKey("radius")) {
      throw new IllegalArgumentException(
          "Request must contain 'radius' parameter when it contains a 'location' parameter.");
    }

  }

  public static class Response implements ApiResponse<PlacesSearchResponse> {

    public String status;
    public String htmlAttributions[];
    public PlacesSearchResult results[];
    public String errorMessage;

    @Override
    public boolean successful() {
      return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }

    @Override
    public PlacesSearchResponse getResult() {
      PlacesSearchResponse result = new PlacesSearchResponse();
      result.htmlAttributions = htmlAttributions;
      result.results = results;
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