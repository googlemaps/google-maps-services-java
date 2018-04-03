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

package com.google.maps;

import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiResponse;
import java.io.Serializable;

/**
 * <code>ImageResponse</code> is a type system hack to enable API endpoints to return a <code>
 * ImageResponse.Result</code>.
 */
public class ImageResponse implements ApiResponse<ImageResponse.Result> {

  public ImageResponse() {}

  @Override
  public boolean successful() {
    return true;
  }

  @Override
  public ApiException getError() {
    return null;
  }

  @Override
  public ImageResponse.Result getResult() {
    return null;
  }

  /** This is the object returned from API endpoints that return an Image. */
  public static class Result implements Serializable {

    public Result(String contentType, byte[] imageData) {
      this.imageData = imageData;
      this.contentType = contentType;
    }

    private static final long serialVersionUID = 1L;

    /** The image data from the Photos API call. */
    public final byte[] imageData;

    /** The Content-Type header of the returned result. */
    public final String contentType;
  }
}
