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

package com.google.maps.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a single Autocomplete result returned from the Google Places API Web Service.
 *
 * <p>Please see <a
 * href="https://developers.google.com/places/web-service/query#query_autocomplete_responses">Query
 * Autocomplete Responses</a> for more detail.
 */
public class AutocompletePrediction implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Description of the matched prediction. */
  public String description;

  /** The Place ID of the place. */
  public String placeId;

  /**
   * An array indicating the type of the address component.
   *
   * <p>Please see <a href="https://developers.google.com/places/supported_types">supported
   * types</a> for a list of types that can be returned.
   */
  public String types[];

  /**
   * An array of terms identifying each section of the returned description. (A section of the
   * description is generally terminated with a comma.) Each entry in the array has a value field,
   * containing the text of the term, and an offset field, defining the start position of this term
   * in the description, measured in Unicode characters.
   */
  public Term terms[];

  /**
   * The distance in meters of the place from the {@link
   * com.google.maps.PlaceAutocompleteRequest#origin(LatLng)}. Optional.
   */
  public Integer distanceMeters;

  /**
   * Describes the location of the entered term in the prediction result text, so that the term can
   * be highlighted if desired.
   */
  public static class MatchedSubstring implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The length of the matched substring, measured in Unicode characters. */
    public int length;

    /** The start position of the matched substring, measured in Unicode characters. */
    public int offset;

    @Override
    public String toString() {
      return String.format("(offset=%d, length=%d)", offset, length);
    }
  }

  /**
   * The locations of the entered term in the prediction result text, so that the term can be
   * highlighted if desired.
   */
  public MatchedSubstring matchedSubstrings[];

  /** A description of how the autocomplete query matched the returned result. */
  public AutocompleteStructuredFormatting structuredFormatting;

  /**
   * Identifies each section of the returned description. (A section of the description is generally
   * terminated with a comma.)
   */
  public static class Term implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The start position of this term in the description, measured in Unicode characters. */
    public int offset;

    /** The text of the matched term. */
    public String value;

    @Override
    public String toString() {
      return String.format("(offset=%d, value=%s)", offset, value);
    }
  }

  @Override
  public String toString() {
    return String.format(
        "[AutocompletePrediction: \"%s\", placeId=%s, types=%s, terms=%s, "
            + "matchedSubstrings=%s, structuredFormatting=%s]",
        description,
        placeId,
        Arrays.toString(types),
        Arrays.toString(terms),
        Arrays.toString(matchedSubstrings),
        Objects.toString(structuredFormatting));
  }
}
