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
 * PhotoResult contains the photo for a PhotoReference.
 *
 * <p>Please see <a href="https://developers.google.com/places/web-service/photos">Photos</a> for
 * more details.</p>
 */
public class PhotoResult {
  /**
   * imageData is the byte array of returned image data from the Photos API call.
   */
  public byte[] imageData;

  /**
   * contentType is the Content-Type header of the returned result.
   */
  public String contentType;
}
