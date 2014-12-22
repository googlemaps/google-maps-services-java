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
public class CellTower{

  /**
   * Unique identifier of the cell. On GSM, this is the Cell ID (CID); CDMA networks use the Base Station ID (BID).
   * WCDMA networks use the UTRAN/GERAN Cell Identity (UC-Id), which is a 32-bit value concatenating the Radio Network
   * Controller (RNC) and Cell ID. Specifying only the 16-bit Cell ID value in WCDMA networks may return inaccurate
   * results.
   */
  private int cellId;

  /**
   * (required): The Location Area Code (LAC) for GSM and WCDMAnetworks. The Network ID (NID) for CDMA networks.
   */
  int locationAreaCode;

  /**
   * (required): The cell tower's Mobile Country Code (MCC).
   */
  int mobileCountryCode;

  /**
   * (required): The cell tower's Mobile Network Code. This is the MNC for GSM and WCDMA; CDMA uses the System ID (SID).
   */
  int mobileNetworkCode;

  /**
   * The number of milliseconds since this cell was primary. If age is 0, the cellId represents a current measurement.
   */
  private int age;

  /**
   * Radio signal strength measured in dBm.
   */
  private int signalStrength;

  /**
   * The timing advance value.
   */
  private int timingAdvance;

  public CellTower(int locationAreaCode, int mobileCountryCode, int mobileNetworkCode) {
    super();
//    this.put("locationAreaCode", locationAreaCode);
//    this.put("mobileCountryCode", mobileCountryCode);
//    this.put("mobileNetworkCode", mobileNetworkCode);
    this.locationAreaCode = locationAreaCode;
    this.mobileCountryCode = mobileCountryCode;
    this.mobileNetworkCode = mobileNetworkCode;
  }

  /**
   * @param cellId the cellId to set
   * @return
   */
  public CellTower setCellId(int cellId) {
    this.cellId = cellId;
//    this.put("cellId", cellId);
    return this;
  }

  /**
   * @param age the age to set
   * @return
   */
  public CellTower setAge(int age) {
    this.age = age;
//    this.put("age", age);
    return this;
  }

  /**
   * @param signalStrength the signalStrength to set
   * @return
   */
  public CellTower setSignalStrength(int signalStrength) {
    this.signalStrength = signalStrength;
//    this.put("signalStrength", signalStrength);
    return this;
  }

  /**
   * @param timingAdvance the timingAdvance to set
   * @return
   */
  public CellTower setTimingAdvance(int timingAdvance) {
    this.timingAdvance = timingAdvance;
//    this.put("timingAdvance", timingAdvance);
    return this;
  }
  
  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
