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

package com.google.maps.errors;

/**
 * Indicates that the product of origins and destinations exceeds the per-query limit.
 *
 * @see <a href="https://developers.google.com/maps/documentation/distance-matrix/usage-limits">
 *     Limits</a>
 */
public class MaxElementsExceededException extends ApiException {

  private static final long serialVersionUID = 5926526363472768479L;

  public MaxElementsExceededException(String errorMessage) {
    super(errorMessage);
  }
}
