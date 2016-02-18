/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.PriceLevel;
import com.google.maps.model.RankBy;

/**
 * A <a href="https://developers.google.com/places/web-service/search#TextSearchRequests">Text
 * Search</a> request.
 */
public class TextSearchRequest
    extends PendingResultBase<PlacesSearchResponse, TextSearchRequest, TextSearchRequest.Response> {

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
   * minPrice restricts to places that are at least this price level.
   */
  public TextSearchRequest minPrice(PriceLevel priceLevel) {
    return param("minprice", priceLevel);
  }

  /**
   * maxPrice restricts to places that are at most this price level.
   */
  public TextSearchRequest maxPrice(PriceLevel priceLevel) {
    return param("maxprice", priceLevel);
  }

  /**
   * name is one or more terms to be matched against the names of places, separated with a space
   * character.
   */
  public TextSearchRequest name(String name) {
    return param("name", name);
  }

  /**
   * openNow returns only those places that are open for business at the time the query is sent.
   */
  public TextSearchRequest openNow(boolean openNow) {
    return param("opennow", String.valueOf(openNow));
  }

  /**
   * pageToken returns the next 20 results from a previously run search. Setting a pageToken
   * parameter will execute a search with the same parameters used previously — all parameters other
   * than pageToken will be ignored.
   */
  public TextSearchRequest pageToken(String nextPageToken) {
    return param("pagetoken", nextPageToken);
  }

  /**
   * rankby specifies the order in which results are listed.
   */
  public TextSearchRequest rankby(RankBy ranking) {
    return param("rankby", ranking);
  }

  /**
   * type restricts the results to places matching the specified type.
   */
  public TextSearchRequest type(PlaceType type) {
    return param("type", type);
  }

  @Override
  protected void validateRequest() {

    // All other parameters are ignored if pagetoken is specified.
    if (params().containsKey("pagetoken")) {
      return;
    }

    if (!params().containsKey("query")) {
      throw new IllegalArgumentException("Request must contain 'query' or a 'pageToken'.");
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
    public String nextPageToken;
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
      result.nextPageToken = nextPageToken;
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
