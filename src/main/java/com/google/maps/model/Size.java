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

package com.google.maps.model;

import com.google.maps.internal.StringJoin;
import java.io.Serializable;

public class Size implements StringJoin.UrlValue, Serializable {
  private static final long serialVersionUID = 1L;

  /** The width of this Size. */
  public int width;

  /** The height of this Size. */
  public int height;

  /**
   * Constructs a Size with a height/width pair.
   *
   * @param height The height of this Size.
   * @param width The width of this Size.
   */
  public Size(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /** Serialization constructor. */
  public Size() {}

  @Override
  public String toString() {
    return toUrlValue();
  }

  @Override
  public String toUrlValue() {
    return String.format("%dx%d", width, height);
  }
}
