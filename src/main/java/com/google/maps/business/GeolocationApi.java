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
package com.google.maps.business;

import com.google.maps.ApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.CellTower;
import com.google.maps.model.GeolocationResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.WiFiAccessPoint;

/**
 * @see https://developers.google.com/maps/documentation/business/geolocation/
 * @author Gavin.Lin
 */
public class GeolocationApi {

  private GeolocationApi() {

  }

  /**
   * create a new Geolocation API request.
   *
   * @param context
   * @return
   */
  public static GeolocationApiRequest newRequest(ApiContext context) {
    return new GeolocationApiRequest(context);
  }

  /**
   * Request the latitude and longitude of {@code CellTowers}.
   */
  public static GeolocationApiRequest cellTower(ApiContext context, CellTower... cellTower) {
    GeolocationApiRequest request = new GeolocationApiRequest(context);
    return request;
  }
  
  /**
   * Request the latitude and longitude of an {@code CellTower}.
   */
  public static GeolocationApiRequest cellTower(ApiContext context, int locationAreaCode, int mobileCountryCode, int mobileNetworkCode) {
    return cellTower(context, new CellTower(locationAreaCode, mobileCountryCode, mobileNetworkCode));
  }

  /**
   * Request the latitude and longitude of {@code WiFiAccessPoints}.
   */
  public static GeolocationApiRequest wiFiAccessPoint(ApiContext context, WiFiAccessPoint... wiFiAccessPoint) {
    GeolocationApiRequest request = new GeolocationApiRequest(context);
    return request;
  }

  /**
   * Request the latitude and longitude of an {@code WiFiAccessPoint}.
   */
  public static GeolocationApiRequest wiFiAccessPoint(ApiContext context, String macAddress) {
    return wiFiAccessPoint(context, new WiFiAccessPoint(macAddress));
  }

  static class Response implements ApiResponse<GeolocationResult> {

    public _Error error;
    public LatLng location;
    public double accuracy;

    @Override
    public boolean successful() {
      return error == null;
    }

    @Override
    public GeolocationResult getResult() {
      return new GeolocationResult(location, accuracy);
    }

    @Override
    public ApiException getError() {
      if (successful()) {
        return null;
      }
      return ApiException.from(String.valueOf(error.code), error.message);
    }
  }

  public class _Error {

    public int code;
    public String message;
    public _Errors[] errors;

    public class _Errors {

      public String domain;
      public String reason;
      public String message;
    }
  }

}
