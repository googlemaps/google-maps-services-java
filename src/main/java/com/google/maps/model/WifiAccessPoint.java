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

import java.io.Serializable;

/**
 * A WiFi access point.
 *
 * <p>The request body's {@code wifiAccessPoints} array must contain two or more WiFi access point
 * objects. {@code macAddress} is required; all other fields are optional.
 *
 * <p>Please see <a
 * href="https://developers.google.com/maps/documentation/geolocation/intro#wifi_access_point_object">
 * WiFi Access Point Objects</a> for more detail.
 */
public class WifiAccessPoint implements Serializable {

  private static final long serialVersionUID = 1L;

  public WifiAccessPoint() {}

  // constructor only used by the builder class below
  private WifiAccessPoint(
      String _macAddress,
      Integer _signalStrength,
      Integer _age,
      Integer _channel,
      Integer _signalToNoiseRatio) {
    macAddress = _macAddress;
    signalStrength = _signalStrength;
    age = _age;
    channel = _channel;
    signalToNoiseRatio = _signalToNoiseRatio;
  }
  /**
   * The MAC address of the WiFi node (required). Separators must be {@code :} (colon) and hex
   * digits must use uppercase.
   */
  public String macAddress;
  /** The current signal strength measured in dBm. */
  public Integer signalStrength = null;
  /** The number of milliseconds since this access point was detected. */
  public Integer age = null;
  /** The channel over which the client is communicating with the access point. */
  public Integer channel = null;
  /** The current signal to noise ratio measured in dB. */
  public Integer signalToNoiseRatio = null;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[WifiAccessPoint:");
    if (macAddress != null) {
      sb.append(" macAddress=").append(macAddress);
    }
    if (signalStrength != null) {
      sb.append(" signalStrength=").append(signalStrength);
    }
    if (age != null) {
      sb.append(" age=").append(age);
    }
    if (channel != null) {
      sb.append(" channel=").append(channel);
    }
    if (signalToNoiseRatio != null) {
      sb.append(" signalToNoiseRatio=").append(signalToNoiseRatio);
    }
    sb.append("]");
    return sb.toString();
  }

  public static class WifiAccessPointBuilder {
    private String _macAddress = null;
    private Integer _signalStrength = null;
    private Integer _age = null;
    private Integer _channel = null;
    private Integer _signalToNoiseRatio = null;

    // create the actual wifi access point
    public WifiAccessPoint createWifiAccessPoint() {
      return new WifiAccessPoint(_macAddress, _signalStrength, _age, _channel, _signalToNoiseRatio);
    }

    public WifiAccessPointBuilder MacAddress(String newMacAddress) {
      this._macAddress = newMacAddress;
      return this;
    }

    public WifiAccessPointBuilder SignalStrength(int newSignalStrength) {
      this._signalStrength = newSignalStrength;
      return this;
    }

    public WifiAccessPointBuilder Age(int newAge) {
      this._age = newAge;
      return this;
    }

    public WifiAccessPointBuilder Channel(int newChannel) {
      this._channel = newChannel;
      return this;
    }

    public WifiAccessPointBuilder SignalToNoiseRatio(int newSignalToNoiseRatio) {
      this._signalToNoiseRatio = newSignalToNoiseRatio;
      return this;
    }
  }
}
