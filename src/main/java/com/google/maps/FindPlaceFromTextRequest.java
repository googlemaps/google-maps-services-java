/*
 * Copyright 2018 Google Inc. All rights reserved.
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
import com.google.maps.internal.StringJoin;
import com.google.maps.internal.StringJoin.UrlValue;
import com.google.maps.model.FindPlaceFromText;
import com.google.maps.model.PlacesSearchResult;

public class FindPlaceFromTextRequest
    extends PendingResultBase<
        FindPlaceFromText, FindPlaceFromTextRequest, FindPlaceFromTextRequest.Response> {

  static final ApiConfig API_CONFIG =
      new ApiConfig("/maps/api/place/findplacefromtext/json")
          .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .supportsClientId(false);

  public FindPlaceFromTextRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  public enum InputType implements UrlValue {
    TEXT_QUERY("textquery"),
    PHONE_NUMBER("phonenumber");

    private final String inputType;

    InputType(final String inputType) {
      this.inputType = inputType;
    }

    @Override
    public String toUrlValue() {
      return this.inputType;
    }
  }

  public FindPlaceFromTextRequest input(String input) {
    return param("input", input);
  }

  public FindPlaceFromTextRequest inputType(InputType inputType) {
    return param("inputtype", inputType);
  }

  public FindPlaceFromTextRequest fields(FieldMask... fields) {
    return param("fields", StringJoin.join(',', fields));
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("input")) {
      throw new IllegalArgumentException("Request must contain 'input'.");
    }
    if (!params().containsKey("inputtype")) {
      throw new IllegalArgumentException("Request must contain 'inputType'.");
    }
  }

  public static class Response implements ApiResponse<FindPlaceFromText> {

    public String status;
    public PlacesSearchResult candidates[];
    public String errorMessage;

    @Override
    public boolean successful() {
      return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }

    @Override
    public FindPlaceFromText getResult() {
      FindPlaceFromText result = new FindPlaceFromText();
      result.candidates = candidates;
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

  public enum FieldMask implements UrlValue {
    FORMATTED_ADDRESS("formatted_address"),
    GEOMETRY("geometry"),
    ICON("icon"),
    ID("id"),
    NAME("name"),
    OPENING_HOURS("opening_hours"),
    PERMANENTLY_CLOSED("permanently_closed"),
    PHOTOS("photos"),
    PLACE_ID("place_id"),
    PRICE_LEVEL("price_level"),
    RATING("rating"),
    TYPES("types");

    private final String field;

    FieldMask(final String field) {
      this.field = field;
    }

    @Override
    public String toUrlValue() {
      return field;
    }
  }
}
