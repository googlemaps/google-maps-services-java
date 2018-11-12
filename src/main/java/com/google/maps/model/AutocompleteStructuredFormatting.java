/*
 * Copyright 2017 Google Inc. All rights reserved.
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

/** The structured formatting info for a {@link com.google.maps.model.AutocompletePrediction}. */
public class AutocompleteStructuredFormatting implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The main text of a prediction, usually the name of the place. */
  public String mainText;

  /** Where the query matched the returned main text. */
  public AutocompletePrediction.MatchedSubstring mainTextMatchedSubstrings[];

  /** The secondary text of a prediction, usually the location of the place. */
  public String secondaryText;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("(");
    sb.append("\"").append(mainText).append("\"");
    sb.append(" at ").append(Arrays.toString(mainTextMatchedSubstrings));
    if (secondaryText != null) {
      sb.append(", secondaryText=\"").append(secondaryText).append("\"");
    }
    sb.append(")");
    return sb.toString();
  }
}
