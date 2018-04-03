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

package com.google.maps;

import com.google.maps.internal.ApiConfig;

/**
 * A <a href="https://developers.google.com/places/web-service/photos#place_photo_requests">Place
 * Photo</a> request.
 */
public class PhotoRequest
    extends PendingResultBase<ImageResult, PhotoRequest, ImageResult.Response> {

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/place/photo");

  public PhotoRequest(GeoApiContext context) {
    super(context, API_CONFIG, ImageResult.Response.class);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("photoreference")) {
      throw new IllegalArgumentException("Request must contain 'photoReference'.");
    }
    if (!params().containsKey("maxheight") && !params().containsKey("maxwidth")) {
      throw new IllegalArgumentException("Request must contain 'maxHeight' or 'maxWidth'.");
    }
  }

  /**
   * Sets the photoReference for this request.
   *
   * @param photoReference A string identifier that uniquely identifies a photo. Photo references
   *     are returned from either a Place Search or Place Details request.
   * @return Returns the configured PhotoRequest.
   */
  public PhotoRequest photoReference(String photoReference) {
    return param("photoreference", photoReference);
  }

  /**
   * Sets the maxHeight for this request.
   *
   * @param maxHeight The maximum desired height, in pixels, of the image returned by the Place
   *     Photos service.
   * @return Returns the configured PhotoRequest.
   */
  public PhotoRequest maxHeight(int maxHeight) {
    return param("maxheight", String.valueOf(maxHeight));
  }

  /**
   * Sets the maxWidth for this request.
   *
   * @param maxWidth The maximum desired width, in pixels, of the image returned by the Place Photos
   *     service.
   * @return Returns the configured PhotoRequest.
   */
  public PhotoRequest maxWidth(int maxWidth) {
    return param("maxwidth", String.valueOf(maxWidth));
  }
}
