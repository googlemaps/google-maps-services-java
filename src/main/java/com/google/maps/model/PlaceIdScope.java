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

/** The scope of a Place ID returned from the Google Places API Web Service. */
@Deprecated
public enum PlaceIdScope {
  /**
   * Indicates the place ID is recognised by your application only. This is because your application
   * added the place, and the place has not yet passed the moderation process.
   */
  APP,
  /** Indicates the place ID is available to other applications and on Google Maps. */
  GOOGLE
}
