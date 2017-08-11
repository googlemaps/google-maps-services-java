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
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.LatLng;

/**
 * A <a
 * href="https://developers.google.com/places/web-service/query#query_autocomplete_requests">Query
 * Autocomplete</a> request.
 */
public class QueryAutocompleteRequest
    extends PendingResultBase<
        AutocompletePrediction[], QueryAutocompleteRequest, QueryAutocompleteRequest.Response> {

  static final ApiConfig API_CONFIG =
      new ApiConfig("/maps/api/place/queryautocomplete/json")
          .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  protected QueryAutocompleteRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("input")) {
      throw new IllegalArgumentException("Request must contain 'input'.");
    }
  }

  /**
   * The text string on which to search. The Places service will return candidate matches based on
   * this string and order results based on their perceived relevance.
   *
   * @param input The input text to autocomplete.
   * @return Returns this {@code QueryAutocompleteRequest} for call chaining.
   */
  public QueryAutocompleteRequest input(String input) {
    return param("input", input);
  }

  /**
   * The character position in the input term at which the service uses text for predictions. For
   * example, if the input is 'Googl' and the completion point is 3, the service will match on
   * 'Goo'. The offset should generally be set to the position of the text caret. If no offset is
   * supplied, the service will use the entire term.
   *
   * @param offset The character offset to search from.
   * @return Returns this {@code QueryAutocompleteRequest} for call chaining.
   */
  public QueryAutocompleteRequest offset(int offset) {
    return param("offset", String.valueOf(offset));
  }

  /**
   * The point around which you wish to retrieve place information.
   *
   * @param location The location point around which to search.
   * @return Returns this {@code QueryAutocompleteRequest} for call chaining.
   */
  public QueryAutocompleteRequest location(LatLng location) {
    return param("location", location);
  }

  /**
   * The distance (in meters) within which to return place results. Note that setting a radius
   * biases results to the indicated area, but may not fully restrict results to the specified area.
   *
   * @param radius The radius around which to bias results.
   * @return Returns this {@code QueryAutocompleteRequest} for call chaining.
   */
  public QueryAutocompleteRequest radius(int radius) {
    return param("radius", String.valueOf(radius));
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
