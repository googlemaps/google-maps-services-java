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
import com.google.maps.model.PlaceDetails;

import java.util.List;

/**
 * The Google Places API enables you to get data from the same database used by Google Maps and
 * Google+ Local. Places features more than 100 million businesses and points of interest that are
 * updated frequently through owner-verified listings and user-moderated contributions.
 * <p/>
 * <p>See also: <a href="https://developers.google.com/places/web-service/">Places API Web Service
 * documentation</a>.
 */
public class PlacesApi {

  static final ApiConfig TEXT_SEARCH_API_CONFIG = new ApiConfig("/maps/api/place/textsearch/json")
      .fieldNamingPolicy(FieldNamingPolicy.IDENTITY);


  static final ApiConfig PLACE_PHOTOS_API_CONFIG = new ApiConfig("/maps/api/place/photo")
      .fieldNamingPolicy(FieldNamingPolicy.IDENTITY);

  static final ApiConfig QUERY_AUTOCOMPLETE_API_CONFIG = new ApiConfig("/maps/api/place/queryautocomplete/json")
      .fieldNamingPolicy(FieldNamingPolicy.IDENTITY);

  private PlacesApi() {
  }

  /**
   * Request the details of a Place.
   */
  public static PlaceDetailsRequest placeDetails(GeoApiContext context, String placeId) {
    PlaceDetailsRequest request = new PlaceDetailsRequest(context);
    request.placeId(placeId);
    return request;
  }

  static class PlaceDetailsResponse implements ApiResponse<PlaceDetails> {
    public String status;
    public PlaceDetails result;
    public List<String> htmlAttributions;
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

