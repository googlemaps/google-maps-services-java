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
import com.google.gson.Gson;
import com.google.maps.errors.ApiError;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.CellTower;
import com.google.maps.model.GeolocationPostPayload;
import com.google.maps.model.GeolocationResult;
import com.google.maps.model.WifiAccessPoint;

/*
 *  The Google Maps Geolocation API returns a location and accuracy radius based on information
 *  about cell towers and WiFi nodes that the mobile client can detect.
 *
 *  https://developers.google.com/maps/documentation/geolocation/intro#top_of_page
 */
public class GeolocationApi {
  static final String API_BASE_URL = "https://www.googleapis.com";

  static final ApiConfig GEOLOCATION_API_CONFIG = new ApiConfig("/geolocation/v1/geolocate")
      .hostName(API_BASE_URL)
      .supportsClientId(false)
      .fieldNamingPolicy(FieldNamingPolicy.IDENTITY);

  private GeolocationApi () {
  }

  public static PendingResult<GeolocationResult> geolocate(GeoApiContext context, GeolocationPostPayload payload) {

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
/*
TODO: need to write a post method in the style of the gets,
TODO: need to understand how to construct the post object
TODO: what does requestHandler do in GeoApiContext.java?
TODO: Object to Post body?

wait() actually makes the call
...PendingResult is passed a request and a client and a response class
the request determines get/post in OKHttpRequestHandler.java as well as headers and url
the responseClass is the Model class to unmarshal JSON body content in ...PendingResult
the client is just client = new OkHttpClient()

 RoadsApiIntegrationTest.java call snapToRoads from RoadsApi.java,
 RoadsApi.java calls context.get from GeoApiContext.java
 context.get is overloaded but basically takes a series of parameters and some config
     and defers to requestHander.handle, requestHandler is specific to GAE or no GAE.
 handle resides in OkHttpRequestHander.java and defines the post/get nature of the request then
     defeers to OKHttpPendingResult which handle ratelimiting and error retry, this file is where
     await is defined which actually executes the call.
*/