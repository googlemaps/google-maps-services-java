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
 * This describes a photo available with a Search Result.
 *
 * <p>Please see <a href="https://developers.google.com/places/web-service/photos">Photos</a> for
 * more details.</p>
 */
public class Photo {
  /**
   * photoReference is used to identify the photo when you perform a Photo request.
   */
  public String photoReference;

  /**
   * height is the maximum height of the image.
   */
  public int height;

  /**
   * width is the maximum width of the image.
   */
  public int width;

  /**
   * htmlAttributions contains any required attributions.
   */
  public String[] htmlAttributions;
}
