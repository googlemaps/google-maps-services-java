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
import com.google.maps.internal.StringJoin.UrlValue;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceAutocompleteType;
import java.io.Serializable;
import java.util.UUID;

/**
 * A <a
 * href="https://developers.google.com/places/web-service/autocomplete#place_autocomplete_requests">Place
 * Autocomplete</a> request.
 */
public class PlaceAutocompleteRequest
    extends PendingResultBase<
        AutocompletePrediction[], PlaceAutocompleteRequest, PlaceAutocompleteRequest.Response> {

  static final ApiConfig API_CONFIG =
      new ApiConfig("/maps/api/place/autocomplete/json")
          .fieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  protected PlaceAutocompleteRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  /** SessionToken represents an Autocomplete session. */
  public static final class SessionToken implements UrlValue, Serializable {

    private static final long serialVersionUID = 1L;

    private UUID uuid;

    /** This constructor creates a new session. */
    public SessionToken() {
      uuid = UUID.randomUUID();
    }

    /**
     * Construct a session that is a continuation of a previous session.
     *
     * @param uuid The universally unique identifier for this session.
     */
    public SessionToken(UUID uuid) {
      this.uuid = uuid;
    }

    /**
     * Construct a session that is a continuation of a previous session.
     *
     * @param token The unique String to be used as the seed for the session identifier.
     */
    public SessionToken(String token) {
      this.uuid = UUID.nameUUIDFromBytes(token.getBytes());
    }

    /**
     * Retrieve the universally unique identifier for this session. This enables you to recreate the
     * session token in a later context.
     *
     * @return Returns the universally unique identifier for this session.
     */
    public UUID getUUID() {
      return uuid;
    }

    @Override
    public String toUrlValue() {
      return uuid.toString();
    }
  }

  /**
   * Sets the SessionToken for this request. Using session token makes sure the autocomplete is
   * priced per session, instead of per keystroke.
   *
   * @param sessionToken Session Token is the session identifier.
   * @return Returns this {@code PlaceAutocompleteRequest} for call chaining.
   */
  public PlaceAutocompleteRequest sessionToken(SessionToken sessionToken) {
    return param("sessiontoken", sessionToken);
  }

  /**
   * Sets the text string on which to search. The Places service will return candidate matches based
   * on this string and order results based on their perceived relevance.
   *
   * @param input The input text to autocomplete.
   * @return Returns this {@code PlaceAutocompleteRequest} for call chaining.
   */
  public PlaceAutocompleteRequest input(String input) {
    return param("input", input);
  }

  /**
   * The character position in the input term at which the service uses text for predictions. For
   * example, if the input is 'Googl' and the completion point is 3, the service will match on
   * 'Goo'. The offset should generally be set to the position of the text caret. If no offset is
   * supplied, the service will use the entire term.
   *
   * @param offset The character offset position of the user's cursor.
   * @return Returns this {@code PlaceAutocompleteRequest} for call chaining.
   */
  public PlaceAutocompleteRequest offset(int offset) {
    return param("offset", String.valueOf(offset));
  }

  /**
   * The origin point from which to calculate straight-line distance to the destination (returned as
   * {@link AutocompletePrediction#distanceMeters}). If this value is omitted, straight-line
   * distance will not be returned.
   *
   * @param origin The {@link LatLng} origin point from which to calculate distance.
   * @return Returns this {@code PlaceAutocompleteRequest} for call chaining.
   */
  public PlaceAutocompleteRequest origin(LatLng origin) {
    return param("origin", origin);
  }

  /**
   * The point around which you wish to retrieve place information.
   *
   * @param location The {@link LatLng} location to center this autocomplete search.
   * @return Returns this {@code PlaceAutocompleteRequest} for call chaining.
   */
  public PlaceAutocompleteRequest location(LatLng location) {
    return param("location", location);
  }

  /**
   * The distance (in meters) within which to return place results. Note that setting a radius
   * biases results to the indicated area, but may not fully restrict results to the specified area.
   *
   * @param radius The radius over which to bias results.
   * @return Returns this {@code PlaceAutocompleteRequest} for call chaining.
   */
  public PlaceAutocompleteRequest radius(int radius) {
    return param("radius", String.valueOf(radius));
  }

  /**
   * Restricts the results to places matching the specified type.
   *
   * @param type The type to restrict results to.
   * @return Returns this {@code PlaceAutocompleteRequest} for call chaining.
   * @deprecated Please use {@code types} instead.
   */
  public PlaceAutocompleteRequest type(PlaceAutocompleteType type) {
    return this.types(type);
  }

  /**
   * Restricts the results to places matching the specified type.
   *
   * @param types The type to restrict results to.
   * @return Returns this {@code PlaceAutocompleteRequest} for call chaining.
   */
  public PlaceAutocompleteRequest types(PlaceAutocompleteType types) {
    return param("types", types);
  }

  /**
   * A grouping of places to which you would like to restrict your results. Currently, you can use
   * components to filter by country.
   *
   * @param filters The component filter to restrict results with.
   * @return Returns this {@code PlaceAutocompleteRequest} for call chaining.
   */
  public PlaceAutocompleteRequest components(ComponentFilter... filters) {
    return param("components", join('|', filters));
  }

  /**
   * StrictBounds returns only those places that are strictly within the region defined by location
   * and radius. This is a restriction, rather than a bias, meaning that results outside this region
   * will not be returned even if they match the user input.
   *
   * @param strictBounds Whether to strictly bound results.
   * @return Returns this {@code PlaceAutocompleteRequest} for call chaining.
   */
  public PlaceAutocompleteRequest strictBounds(boolean strictBounds) {
    return param("strictbounds", Boolean.toString(strictBounds));
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
