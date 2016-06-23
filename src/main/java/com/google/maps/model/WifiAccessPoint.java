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

package com.google.maps.model;

/**
 * WiFi access point objects
 *
 * The request body's wifiAccessPoints array must contain two or more WiFi access point objects.
 * {@code macAddress} is required; all other fields are optional.
 *
 * <p>Please see <a href="https://developers.google.com/maps/documentation/geolocation/intro#wifi_access_point_object">
 * WiFi Access Point Objects</a> for more detail.
 *
 */
public class WifiAccessPoint {
  /**
  * {@code macAddress}: (required) The MAC address of the WiFi node. Separators must be : (colon) and
  * hex digits must use uppercase.
  */
  public String macAddress;
  /**
  * {@code signalStrength}: The current signal strength measured in dBm.
  */
  public int signalStrength;
  /**
   * {@code age}: The number of milliseconds since this access point was detected.
   */
  public int age;
  /**
   * {@code channel}: The channel over which the client is communicating with the access point.
   */
  public int channel;
  /**
   * {@code signalToNoiseRatio}: The current signal to noise ratio measured in dB.
   */
  public int signalToNoiseRatio;
}
