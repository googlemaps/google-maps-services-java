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

import static com.google.maps.internal.StringJoin.join;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceAutocompleteType;

/**
 * A <a href="https://developers.google.com/places/web-service/autocomplete#place_autocomplete_requests">Place
 * Autocomplete</a> request.
 */
public class PlaceAutocompleteRequest
    extends PendingResultBase<AutocompletePrediction[], PlaceAutocompleteRequest, PlaceAutocompleteRequest.Response> {

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/place/autocomplete/json")
      .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  protected PlaceAutocompleteRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  /**
   * input is the text string on which to search. The Places service will return candidate matches
   * based on this string and order results based on their perceived relevance.
   */
  public PlaceAutocompleteRequest input(String input) {
    return param("input", input);
  }

  /**
   * offset is the character position in the input term at which the service uses text for
   * predictions. For example, if the input is 'Googl' and the completion point is 3, the service
   * will match on 'Goo'. The offset should generally be set to the position of the text caret. If
   * no offset is supplied, the service will use the entire term.
   */
  public PlaceAutocompleteRequest offset(int offset) {
    return param("offset", String.valueOf(offset));
  }

  /**
   * location is the point around which you wish to retrieve place information.
   */
  public PlaceAutocompleteRequest location(LatLng location) {
    return param("location", location);
  }

  /**
   * radius is the distance (in meters) within which to return place results. Note that setting a
   * radius biases results to the indicated area, but may not fully restrict results to the
   * specified area.
   */
  public PlaceAutocompleteRequest radius(int radius) {
    return param("radius", String.valueOf(radius));
  }

  /**
   * type restricts the results to places matching the specified type.
   */
  public PlaceAutocompleteRequest type(PlaceAutocompleteType type) {
    return param("types", type);
  }

  /**
   * Components is a grouping of places to which you would like to restrict your results. Currently,
   * you can use components to filter by country.
   */
  public PlaceAutocompleteRequest components(ComponentFilter... filters) {
    return param("components", join('|', filters));
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("input")) {
      throw new IllegalArgumentException("Request must contain 'input'.");
    }
  }

  public static class Response implements ApiResponse<AutocompletePrediction[]> {
    public String status;
    public AutocompletePrediction predictions[];
    public String errorMessage;

    @Override
    public boolean successful() {
      return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }

    @Override
    public AutocompletePrediction[] getResult() {
      return predictions;
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
