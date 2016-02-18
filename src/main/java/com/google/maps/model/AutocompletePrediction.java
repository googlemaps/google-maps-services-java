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

/**
 * AutocompletePrediction represents a single Autocomplete result returned from the Google Places
 * API Web Service.
 *
 * <p>Please see <a href="https://developers.google.com/places/web-service/query#query_autocomplete_responses">
 * Query Autocomplete Responses</a> for more detail.</p>
 */
public class AutocompletePrediction {

  /**
   * Description of the matched prediction.
   */
  public String description;

  /**
   * The Place ID of the place.
   */
  public String placeId;

  /**
   * types is an array indicating the type of the address component.
   *
   * <p>Please see <a href="https://developers.google.com/places/supported_types">supported
   * types</a> for a list of types that can be returned.</p>
   */
  public String types[];

  /**
   * terms contains an array of terms identifying each section of the returned description (a
   * section of the description is generally terminated with a comma). Each entry in the array has a
   * value field, containing the text of the term, and an offset field, defining the start position
   * of this term in the description, measured in Unicode characters.
   */
  public Term terms[];

  /**
   * MatchedSubstring describes the location of the entered term in the prediction result text, so
   * that the term can be highlighted if desired.
   */
  public static class MatchedSubstring {

    /**
     * length describes the length of the matched substring.
     */
    public int length;

    /**
     * offset defines the start position of the matched substring.
     */
    public int offset;
  }

  public MatchedSubstring matchedSubstrings[];

  /**
   * Term identifies each section of the returned description (a section of the description is
   * generally terminated with a comma).
   */
  public static class Term {

    /**
     * offset defines the start position of this term in the description, measured in Unicode
     * characters.
     */
    public int offset;

    /**
     * The text of the matched term.
     */
    public String value;
  }

}
