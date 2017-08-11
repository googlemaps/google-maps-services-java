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

package com.google.maps.model;

import com.google.maps.internal.StringJoin;

/** Used by the Places API to specify the order in which results are listed. */
public enum RankBy implements StringJoin.UrlValue {
  PROMINENCE("prominence"),
  DISTANCE("distance");

  private final String ranking;

  RankBy(String ranking) {
    this.ranking = ranking;
  }

  @Override
  public String toString() {
    return ranking;
  }

  @Override
  public String toUrlValue() {
    return ranking;
  }
}
