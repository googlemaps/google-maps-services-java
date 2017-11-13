/*
 * Copyright 2014 Google Inc. All rights reserved.
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

/** The duration component for Directions API results. */
public class Duration implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * The numeric duration, in seconds. This is intended to be used only in algorithmic situations,
   * e.g. sorting results by some user specified metric.
   */
  public long inSeconds;

  /** The human-friendly duration. Use this for display purposes. */
  public String humanReadable;

  @Override
  public String toString() {
    return humanReadable;
  }
}
