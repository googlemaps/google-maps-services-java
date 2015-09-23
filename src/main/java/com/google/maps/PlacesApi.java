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
      .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  private PlacesApi() {
  }

  /*
   * Please note: We are specifically not implementing:
   *  - Nearby Search
   *  - Radar Search
   *  - Place Add
   *  - Place Autocomplete
   */

  /**
   * Request the details of a Place.
   *
   * We are only enabling looking up Places by placeId as the older Place identifier - reference - is deprecated.
   * Please see the <a href="https://developers.google.com/places/web-service/details#deprecation">deprecation warning</a>.
   *
   * @param context The context on which to make Geo API requests.
   * @param placeId The PlaceID to request details on.
   * @return Returns a PlaceDetailsRequest that you can configure and execute.
   */

  public static PlaceDetailsRequest placeDetails(GeoApiContext context, String placeId) {
    PlaceDetailsRequest request = new PlaceDetailsRequest(context);
    request.placeId(placeId);
    return request;
  }

  public static QueryAutocompleteRequest queryAutocomplete(GeoApiContext context, String input) {
    QueryAutocompleteRequest request = new QueryAutocompleteRequest(context);
    request.input(input);
    return request;
  }

}

