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
 * The request body must be formatted as JSON. The following fields are supported, and all fields
 *     are optional:
 *
 * homeMobileCountryCode: The mobile country code (MCC) for the device's home network.
 * homeMobileNetworkCode: The mobile network code (MNC) for the device's home network.
 * radioType: The mobile radio type. Supported values are lte, gsm, cdma, and wcdma. While this
 *     field is optional, it should be included if a value is available, for more accurate results.
 * carrier: The carrier name.
 * considerIp: Specifies whether to fall back to IP geolocation if wifi and cell tower signals are
 *      not available. Note that the IP address in the request header may not be the IP of the device.
 *      Defaults to true. Set considerIp to false to disable fall back.
 * cellTowers: An array of cell tower objects. See the Cell Tower Objects section below.
 * wifiAccessPoints: An array of WiFi access point objects. See the WiFi Access Point Objects section
 *     below.
 *
 * https://developers.google.com/maps/documentation/geolocation/intro#body
 */
public class GeolocationPostPayload {
  public int homeMobileCountryCode;
  public int homeMobileNetworkCode;
  public String radioType;
  public String carrier;
  public boolean considerIp;
  public CellTower[] cellTowers;
  public WifiAccessPoint[] wifiAccessPoints;
}


