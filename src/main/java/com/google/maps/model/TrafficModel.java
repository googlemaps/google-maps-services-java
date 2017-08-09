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

import com.google.maps.internal.StringJoin.UrlValue;
import java.util.Locale;

/** Specifies traffic prediction model when requesting future directions. */
public enum TrafficModel implements UrlValue {
  BEST_GUESS,
  OPTIMISTIC,
  PESSIMISTIC;

  @Override
  public String toString() {
    return name().toLowerCase(Locale.ENGLISH);
  }

  @Override
  public String toUrlValue() {
    return toString();
  }
}
