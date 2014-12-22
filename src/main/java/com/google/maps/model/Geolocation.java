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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Gavin.Lin
 */
public class Geolocation {

  int homeMobileCountryCode = -1;
  int homeMobileNetworkCode = -1;
  String radioType = null;
  String carrier = null;
  CellTower[] cellTowers = null;
  WiFiAccessPoint[] wifiAccessPoints = null;

  public Geolocation() {
    super();
  }

  /**
   * (Optional) The mobile country code (MCC) for the device's home network.
   *
   * @param homeMobileCountryCode the homeMobileCountryCode to set
   * @return
   */
  public Geolocation setHomeMobileCountryCode(int homeMobileCountryCode) {
    this.homeMobileCountryCode = homeMobileCountryCode;
//    this.put("homeMobileCountryCode", homeMobileCountryCode);
    return this;
  }

  /**
   * (Optional) The mobile network code (MNC) for the device's home network.
   *
   * @param homeMobileNetworkCode the homeMobileNetworkCode to set
   * @return
   */
  public Geolocation setHomeMobileNetworkCode(int homeMobileNetworkCode) {
    this.homeMobileNetworkCode = homeMobileNetworkCode;
//    this.put("homeMobileNetworkCode", homeMobileNetworkCode);
    return this;
  }

  /**
   * (Optional) The mobile radio type. Supported values are gsm, cdma, and wcdma. While this field is optional, it
   * should be included if a value is available, for more accurate results.
   *
   * @param radioType the radioType to set
   * @return
   */
  public Geolocation setRadioType(String radioType) {
    this.radioType = radioType;
//    this.put("radioType", radioType);
    return this;
  }

  /**
   * (Optional) The carrier name.
   *
   * @param carrier the carrier to set
   * @return
   */
  public Geolocation setCarrier(String carrier) {
    this.carrier = carrier;
//    this.put("carrier", carrier);
    return this;
  }

  /**
   * (Optional) An array of cell tower objects. See the Cell Tower Objects section below.
   *
   * @param cellTowers the cellTowers to set
   * @return
   */
  public Geolocation setCellTowers(CellTower... cellTowers) {
    this.cellTowers = ArrayMerge(this.cellTowers, cellTowers);
//    this.append("cellTowers", cellTowers);
    return this;
  }

  /**
   * (Optional) An array of WiFi access point objects. See the WiFi Access Point Objects section below.
   *
   * @param wifiAccessPoints the wifiAccessPoints to set
   * @return
   */
  public Geolocation setWifiAccessPoints(WiFiAccessPoint... wifiAccessPoints) {
    this.wifiAccessPoints = ArrayMerge(this.wifiAccessPoints, wifiAccessPoints);
//    this.append("wifiAccessPoints", wifiAccessPoints);
    return this;
  }

  private <T> T[] ArrayMerge(T[] a1, T[] a2) {
    if (a1 == null && a2 == null) {
      return (T[]) new Object[]{};
    } else if (a1 == null) {
      return a2;
    } else if (a2 == null) {
      return a1;
    } else {
      Collection<T> collection = new ArrayList<>();
      collection.addAll(Arrays.asList(a1));
      collection.addAll(Arrays.asList(a2));
      return collection.toArray((T[]) new Object[]{});
    }
  }

  public boolean isNull() {
    return homeMobileCountryCode == -1
            && homeMobileNetworkCode == -1
            && radioType == null
            && carrier == null
            && cellTowers == null
            && wifiAccessPoints == null;

  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
