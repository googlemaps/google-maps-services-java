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

/**
 * Describes a photo available with a Search Result.
 *
 * <p>Please see <a href="https://developers.google.com/places/web-service/photos">Place Photos</a>
 * for more details.
 */
public class Photo implements Serializable {

  private static final long serialVersionUID = 1L;
  /** Used to identify the photo when you perform a Photo request. */
  public String photoReference;

  /** The maximum height of the image. */
  public int height;

  /** The maximum width of the image. */
  public int width;

  /** Attributions about this listing which must be displayed to the user. */
  public String[] htmlAttributions;

  @Override
  public String toString() {
    String str = String.format("[Photo %s (%d x %d)", photoReference, width, height);
    if (htmlAttributions != null && htmlAttributions.length > 0) {
      str = str + " " + htmlAttributions.length + " attributions";
    }
    str = str + "]";
    return str;
  }
}
