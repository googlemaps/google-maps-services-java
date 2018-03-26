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
import java.awt.image.BufferedImage;

/**
 * <code>BufferedImageResponse</code> is a type system hack to enable API endpoints to return a
 * <code>BufferedImage</code>.
 */
public class BufferedImageResponse implements ApiResponse<BufferedImage> {

  public BufferedImageResponse(BufferedImage image) {}

  @Override
  public boolean successful() {
    return true;
  }

  @Override
  public ApiException getError() {
    return null;
  }

  @Override
  public BufferedImage getResult() {
    return null;
  }
}
