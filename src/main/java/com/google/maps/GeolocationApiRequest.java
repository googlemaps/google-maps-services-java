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

import com.google.gson.Gson;
import com.google.maps.model.CellTower;
import com.google.maps.model.GeolocationPayload;
import com.google.maps.model.GeolocationPayload.GeolocationPayloadBuilder;
import com.google.maps.model.GeolocationResult;
import com.google.maps.model.WifiAccessPoint;

/** A request for the Geolocation API. */
public class GeolocationApiRequest
    extends PendingResultBase<GeolocationResult, GeolocationApiRequest, GeolocationApi.Response> {

  private GeolocationPayload payload = null;
  private GeolocationPayloadBuilder builder = null;

  GeolocationApiRequest(GeoApiContext context) {
    super(context, GeolocationApi.GEOLOCATION_API_CONFIG, GeolocationApi.Response.class);
    builder = new GeolocationPayload.GeolocationPayloadBuilder();
  }

  @Override
  protected void validateRequest() {
    if (this.payload.considerIp != null
        && !this.payload.considerIp
        && this.payload.wifiAccessPoints != null
        && this.payload.wifiAccessPoints.length < 2) {
      throw new IllegalArgumentException("Request must contain two or more 'Wifi Access Points'");
    }
  }

  public GeolocationApiRequest HomeMobileCountryCode(int newHomeMobileCountryCode) {
    this.builder.HomeMobileCountryCode(newHomeMobileCountryCode);
    return this;
  }

  public GeolocationApiRequest HomeMobileNetworkCode(int newHomeMobileNetworkCode) {
    this.builder.HomeMobileNetworkCode(newHomeMobileNetworkCode);
    return this;
  }

  public GeolocationApiRequest RadioType(String newRadioType) {
    this.builder.RadioType(newRadioType);
    return this;
  }

  public GeolocationApiRequest Carrier(String newCarrier) {
    this.builder.Carrier(newCarrier);
    return this;
  }

  public GeolocationApiRequest ConsiderIp(boolean newConsiderIp) {
    this.builder.ConsiderIp(newConsiderIp);
    return this;
  }

  public GeolocationApiRequest CellTowers(CellTower[] newCellTowers) {
    this.builder.CellTowers(newCellTowers);
    return this;
  }

  public GeolocationApiRequest AddCellTower(CellTower newCellTower) {
    this.builder.AddCellTower(newCellTower);
    return this;
  }

  public GeolocationApiRequest WifiAccessPoints(WifiAccessPoint[] newWifiAccessPoints) {
    this.builder.WifiAccessPoints(newWifiAccessPoints);
    return this;
  }

  public GeolocationApiRequest AddWifiAccessPoint(WifiAccessPoint newWifiAccessPoint) {
    this.builder.AddWifiAccessPoint(newWifiAccessPoint);
    return this;
  }

  public GeolocationApiRequest Payload(GeolocationPayload payload) {
    this.payload = payload;
    return this;
  }

  public GeolocationApiRequest CreatePayload() {
    if (this.payload == null) {
      // if the payload has not been set, create it
      this.payload = this.builder.createGeolocationPayload();
    } else {
      // use the payload that has been explicitly set by the Payload method above
    }
    Gson gson = new Gson();
    String jsonPayload = gson.toJson(this.payload);
    return param("_payload", jsonPayload);
  }
}
