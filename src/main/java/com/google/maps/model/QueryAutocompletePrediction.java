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

public class QueryAutocompletePrediction {

  /**
   * Description of the matched prediction.
   */
  public String description;

  /**
   * Deprecated field. Do not use.
   *
   * @deprecated This field is deprecated. Please see
   * <a href="https://developers.google.com/places/web-service/query#deprecation">the deprecation notice</a>.
   */
  public String id;

  /**
   * Deprecated field. Do not use.
   *
   * @deprecated This field is deprecated. Please see
   * <a href="https://developers.google.com/places/web-service/query#deprecation">the deprecation notice</a>.
   */
  public String reference;

  /**
   * The Place ID of the place.
   */
  public String placeId;

  public String types[];

  // TODO(brettmorgan): Document this on https://developers.google.com/places/web-service/query
  public static class MatchedSubstring {
    public int length;
    public int offset;
  }

  public MatchedSubstring matchedSubstrings[];

  // TODO(brettmorgan): Document this on https://developers.google.com/places/web-service/query
  public static class Term {
    public int offset;
    public String value;
  }

  public Term terms[];
}
