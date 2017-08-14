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

/** Represents the Structured Formatting field of an {@see AutocompletePrediction}. */
public class AutcompleteStructuredFormatting {

  /** Contains the main text of a prediction, usually the name of the place. */
  public String mainText;

  /** Contains an array of where the query matched the returned main text. */
  public AutocompletePrediction.MatchedSubstring mainTextMatchedSubstrings[];

  /** Contains the secondary text of a prediction, usually the location of the place. */
  public String secondaryText;
}
