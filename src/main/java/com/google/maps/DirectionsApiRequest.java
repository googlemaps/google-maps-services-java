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

package com.google.maps;

import com.google.maps.model.DirectionsRoute;

/**
 * Request for the Directions API.
 *
 * <p>This request generates an elided result with just the routes.</p>
 */
public class DirectionsApiRequest
    extends DirectionsApiRequestBase<DirectionsRoute[], DirectionsApiRequest, DirectionsApi.Response> {

  DirectionsApiRequest(GeoApiContext context) {
    super(context, DirectionsApi.API_CONFIG, DirectionsApi.Response.class);
  }

}
