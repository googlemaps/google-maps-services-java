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

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiError;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.GeolocationPayload;
import com.google.maps.model.GeolocationResult;

/*
 *  The Google Maps Geolocation API returns a location and accuracy radius based on information
 *  about cell towers and WiFi nodes that the mobile client can detect.
 *
 * <p>Please see the<a href="https://developers.google.com/maps/documentation/geolocation/intro#top_of_page">
 *   Geolocation API</a> for more detail.
 *
 *
 */
public class GeolocationApi {
  private static final String API_BASE_URL = "https://www.googleapis.com";

  private static final ApiConfig GEOLOCATION_API_CONFIG = new ApiConfig("/geolocation/v1/geolocate")
      .hostName(API_BASE_URL)
      .supportsClientId(false)
      .fieldNamingPolicy(FieldNamingPolicy.IDENTITY);

  private GeolocationApi () {
  }

  public static PendingResult<GeolocationResult> geolocate(GeoApiContext context, GeolocationPayload payload) {

    return context.post(GEOLOCATION_API_CONFIG, GeolocationResponse.class, payload);
  }

  private static class GeolocationResponse implements ApiResponse<GeolocationResult> {
    private GeolocationResult result;
    private ApiError error;

    @Override
    public boolean successful() {
      return error == null;
    }

    @Override
    public GeolocationResult getResult() {
      return result;
    }

    @Override
    public ApiException getError() {
      return ApiException.from(error.status, error.message);
    }
  }
}