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

import com.google.gson.Gson;

/**
 *
 * @author Gavin.Lin
 */
public class WiFiAccessPoint {

  /**
   * (required) The MAC address of the WiFi node. Separators must be : (colon) and hex digits must use uppercase.
   */
  String macAddress;
  /**
   * The current signal strength measured in dBm.
   */
  private int signalStrength;
  /**
   * The number of milliseconds since this access point was detected.
   */
  private int age;
  /**
   * The channel over which the client is communicating with the access point.
   */
  private int channel;
  /**
   * The current signal to noise ratio measured in dB.
   */
  private int signalToNoiseRatio;

  public WiFiAccessPoint(String macAddress) {
//    super();
//    this.put("macAddress", macAddress);
    this.macAddress = macAddress;
  }

  /**
   * @param signalStrength the signalStrength to set
   * @return
   */
  public WiFiAccessPoint setSignalStrength(int signalStrength) {
    this.signalStrength = signalStrength;
//    this.put("signalStrength", signalStrength);
    return this;
  }

  /**
   * @param age the age to set
   * @return
   */
  public WiFiAccessPoint setAge(int age) {
    this.age = age;
//    this.put("age", age);
    return this;
  }

  /**
   * @param channel the channel to set
   * @return
   */
  public WiFiAccessPoint setChannel(int channel) {
    this.channel = channel;
//    this.put("channel", channel);
    return this;
  }

  /**
   * @param signalToNoiseRatio the signalToNoiseRatio to set
   * @return
   */
  public WiFiAccessPoint setSignalToNoiseRatio(int signalToNoiseRatio) {
    this.signalToNoiseRatio = signalToNoiseRatio;
//    this.put("signalToNoiseRatio", signalToNoiseRatio);
    return this;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
