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

import com.google.maps.model.GeolocationPayload;
import com.google.maps.model.GeolocationResult;

/**
 * Request for the Geolocation API.
 */
public class GeolocationApiRequest
    extends PendingResultBase<GeolocationResult, GeolocationApiRequest, GeolocationApi.Response>{

  private GeolocationPayload payload;

  GeolocationApiRequest(GeoApiContext context) {
    super(context, GeolocationApi.GEOLOCATION_API_CONFIG, GeolocationApi.Response.class);
  }

  @Override
  protected void validateRequest() {
    // TODO: see DirectionsApiRequest for an example on how to validate
  }
}
