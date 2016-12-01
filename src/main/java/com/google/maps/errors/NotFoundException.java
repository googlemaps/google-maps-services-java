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

package com.google.maps.errors;

/**
 * Indicates at least one of the locations specified in the request's origin, destination, or
 * waypoints could not be geocoded.
 */
public class NotFoundException extends ApiException {

  private static final long serialVersionUID = -5447625132975504651L;

  public NotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
