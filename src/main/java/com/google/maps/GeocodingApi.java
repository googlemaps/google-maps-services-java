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

import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

/**
 * Geocoding is the process of converting addresses (like "1600 Amphitheatre Parkway, Mountain View,
 * CA") into geographic coordinates (like latitude 37.423021 and longitude -122.083739), which you
 * can use to place markers or position the map. Reverse geocoding is the process of converting
 * geographic coordinates into a human-readable address.
 *
 * @see <a href="https://developers.google.com/maps/documentation/geocoding/">Geocoding
 *     documentation</a>
 */
public class GeocodingApi {
  private GeocodingApi() {}

  /**
   * Creates a new Geocoding API request.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @return Returns the request, ready to run.
   */
  public static GeocodingApiRequest newRequest(GeoApiContext context) {
    return new GeocodingApiRequest(context);
  }

  /**
   * Requests the latitude and longitude of an {@code address}.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param address The address to geocode.
   * @return Returns the request, ready to run.
   */
  public static GeocodingApiRequest geocode(GeoApiContext context, String address) {
    GeocodingApiRequest request = new GeocodingApiRequest(context);
    request.address(address);
    return request;
  }

  /**
   * Requests the street address of a {@code location}.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param location The location to reverse geocode.
   * @return Returns the request, ready to run.
   */
  public static GeocodingApiRequest reverseGeocode(GeoApiContext context, LatLng location) {
    GeocodingApiRequest request = new GeocodingApiRequest(context);
    request.latlng(location);
    return request;
  }

  public static class Response implements ApiResponse<GeocodingResult[]> {
    public String status;
    public String errorMessage;
    public GeocodingResult[] results;

    @Override
    public boolean successful() {
      return "OK".equals(status) || "ZERO_RESULTS".equals(status);
    }

    @Override
    public GeocodingResult[] getResult() {
      return results;
    }

    @Override
    public ApiException getError() {
      if (successful()) {
        return null;
      }
      return ApiException.from(status, errorMessage);
    }
  }
}
