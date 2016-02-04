/*
 * Copyright 2016 Google Inc. All rights reserved.
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
import com.google.maps.model.*;

public class NearbySearchRequest extends PendingResultBase<PlacesSearchResponse, NearbySearchRequest, NearbySearchRequest.Response> {

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/place/nearbysearch/json")
      .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  public NearbySearchRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  /**
   * location is the latitude/longitude around which to retrieve place information.
   */
  public NearbySearchRequest location(LatLng location) {
    return param("location", location);
  }

  /**
   * radius defines the DISTANCE (in meters) within which to return place results. The maximum allowed radius is
   * 50,000 meters. Note that radius must not be included if rankby=DISTANCE is specified.
   */
  public NearbySearchRequest radius(int distance) {
    return param("radius", Integer.toString(distance));
  }

  /**
   * rankby specifies the order in which results are listed.
   */
  public NearbySearchRequest rankby(Rankby ranking) {
    return param("rankby", ranking);
  }

  /**
   * keyword is a term to be matched against all content that Google has indexed for this place, including but not
   * limited to name, type, and address, as well as customer reviews and other third-party content.
   */
  public NearbySearchRequest keyword(String keyword) {
    return param("keyword", keyword);
  }

  /**
   * minPrice restricts to places that are at least this price level.
   */
  public NearbySearchRequest minPrice(PriceLevel priceLevel) {
    return param("minprice", priceLevel);
  }

  /**
   * maxPrice restricts to places that are at most this price level.
   */
  public NearbySearchRequest maxPrice(PriceLevel priceLevel) {
    return param("maxprice", priceLevel);
  }

  /**
   * name is one or more terms to be matched against the names of places, separated with a space character.
   */
  public NearbySearchRequest name(String name) {
    return param("name", name);
  }

  /**
   * openNow returns only those places that are open for business at the time the query is sent.
   */
  public NearbySearchRequest openNow(boolean openNow) {
    return param("opennow", String.valueOf(openNow));
  }

  @Override
  protected void validateRequest() {

    // radius must not be included if rankby=distance
    if (params().containsKey("rankby") &&
        params().get("rankby").equals(Rankby.DISTANCE.toString()) &&
        params().containsKey("radius")) {
      throw new IllegalArgumentException("Request must not contain radius with rankby=distance");
    }

    // If rankby=DISTANCE is specified, then one or more of keyword, name, or types is required.
    if (params().containsKey("rankby") &&
        params().get("rankby").equals(Rankby.DISTANCE.toString()) &&
        !params().containsKey("keyword") &&
        !params().containsKey("name") &&
        !params().containsKey("types")) {
      throw new IllegalArgumentException("With rankby=distance is specified, then one or more of keyword, name, or types is required");
    }

  }


  public class Response implements ApiResponse<PlacesSearchResponse> {

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
