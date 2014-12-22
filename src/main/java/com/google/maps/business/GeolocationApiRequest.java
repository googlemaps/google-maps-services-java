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
import com.google.maps.PendingResultBase;
import com.google.maps.model.CellTower;
import com.google.maps.model.Geolocation;
import com.google.maps.model.GeolocationResult;
import com.google.maps.model.WiFiAccessPoint;
import com.squareup.okhttp.MediaType;

/**
 * Request for the Geolocation API.
 * @author Gavin.Lin
 */
public class GeolocationApiRequest 
  extends PendingResultBase<GeolocationResult, GeolocationApiRequest, GeolocationApi.Response> {
  
  private static final String BASE = "/geolocation/v1/geolocate";
  private static final MediaType contentType = MediaType.parse("application/json; charset=utf-8");
  private final Geolocation geolocation = new Geolocation();
  
  public GeolocationApiRequest(ApiContext context) {
    super(context, GeolocationApi.Response.class, BASE);
    this.setMethod(MethodType.POST, contentType);
  }

  /**
   * All fields are optional, but must contain at least one of homeMobileCountryCode, homeMobileNetworkCode, radioType
   * , carrier, cellTowers and wifiAccessPoints.
   */
  @Override
  protected void validateRequest() {
    if (geolocation.isNull()) {
      throw new IllegalArgumentException(
          "Request must contain at least one of homeMobileCountryCode, homeMobileNetworkCode, radioType, carrier,"
                  + " cellTowers and wifiAccessPoints");
    }
  }

  /**
   * Create a geolocation for {@code homeMobileCountryCode}.
   */
  public GeolocationApiRequest homeMobileCountryCode(int homeMobileCountryCode){
    return body(geolocation.setHomeMobileCountryCode(homeMobileCountryCode));
  }

  /**
   * Create a geolocation for {@code homeMobileNetworkCode}.
   */
  public GeolocationApiRequest homeMobileNetworkCode(int homeMobileNetworkCode){
    return body(geolocation.setHomeMobileNetworkCode(homeMobileNetworkCode));
  }

  /**
   * Create a geolocation for {@code radioType}.
   */
  public GeolocationApiRequest radioType(String radioType){
    return body(geolocation.setRadioType(radioType));
  }

  /**
   * Create a geolocation for {@code carrier}.
   */
  public GeolocationApiRequest carrier(String carrier){
    return body(geolocation.setCarrier(carrier));
  }
  
  /**
   * Create a geolocation for {@code cellTowers}.
   */
  public GeolocationApiRequest cellTowers(CellTower... cellTowers){
    return body(geolocation.setCellTowers(cellTowers));
  }
  
  /**
   * Create a geolocation for {@code wiFiAccessPoints}.
   */
  public GeolocationApiRequest wiFiAccessPoints(WiFiAccessPoint... wiFiAccessPoints){
    return body(geolocation.setWifiAccessPoints(wiFiAccessPoints));
  }
}
