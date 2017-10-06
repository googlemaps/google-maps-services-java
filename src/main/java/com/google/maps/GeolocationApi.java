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

package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.GeolocationPayload;
import com.google.maps.model.GeolocationResult;
import com.google.maps.model.LatLng;

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

  static final ApiConfig GEOLOCATION_API_CONFIG =
      new ApiConfig("/geolocation/v1/geolocate")
          .hostName(API_BASE_URL)
          .supportsClientId(false)
          .fieldNamingPolicy(FieldNamingPolicy.IDENTITY)
          .requestVerb("POST");

  private GeolocationApi() {}

  public static PendingResult<GeolocationResult> geolocate(
      GeoApiContext context, GeolocationPayload payload) {
    return new GeolocationApiRequest(context).Payload(payload).CreatePayload();
  }

  public static GeolocationApiRequest newRequest(GeoApiContext context) {
    return new GeolocationApiRequest(context);
  }

  public static class Response implements ApiResponse<GeolocationResult> {
    public int code = 200;
    public String message = "OK";
    public double accuracy = -1.0;
    public LatLng location = null;
    public String domain = null;
    public String reason = null;
    public String debugInfo = null;

    @Override
    public boolean successful() {
      return code == 200;
    }

    @Override
    public GeolocationResult getResult() {
      GeolocationResult result = new GeolocationResult();
      result.accuracy = accuracy;
      result.location = location;
      return result;
    }

    @Override
    public ApiException getError() {
      if (successful()) {
        return null;
      }
      return ApiException.from(reason, message);
    }
  }
}
