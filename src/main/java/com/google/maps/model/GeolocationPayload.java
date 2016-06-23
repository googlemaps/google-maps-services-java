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

package com.google.maps.model;

/**
 * Request body
 * The following fields are supported, and all fields are optional:
 *
 * <p>Please see <a href="https://developers.google.com/maps/documentation/geolocation/intro#requests">
 *Geolocation Requests</a> for more detail.
 *
 * https://developers.google.com/maps/documentation/geolocation/intro#body
 */
public class GeolocationPayload {
  /**
   * {@code homeMobileCountryCode}: The mobile country code (MCC) for the device's home network.
   */
  public int homeMobileCountryCode;
    /**
    * {@code homeMobileNetworkCode}: The mobile network code (MNC) for the device's home network.
    */
  public int homeMobileNetworkCode;
    /**
    * {@code radioType}: The mobile radio type. Supported values are lte, gsm, cdma, and wcdma. While
    * this field is optional, it should be included if a value is available, for more accurate results.
    */
  public String radioType;
    /**
    * {@code carrier}: The carrier name.
    */
  public String carrier;
    /**
    * considerIp: Specifies whether to fall back to IP geolocation if wifi and cell tower signals are
    * not available. Note that the IP address in the request header may not be the IP of the device.
    * Defaults to true. Set considerIp to false to disable fall back.
    */
  public boolean considerIp;
  /**
   * {@code cellTowers}: An array of cell tower objects. See the Cell Tower Objects.
   */

  public CellTower[] cellTowers;
  /**
   *  {@code wifiAccessPoints}: An array of WiFi access point objects. See the WiFi Access Point Objects.
   */
  public WifiAccessPoint[] wifiAccessPoints;
}


