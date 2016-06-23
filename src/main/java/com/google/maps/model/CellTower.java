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
 * Cell tower objects
 *
 * The Geolocation API request body's cellTowers array contains zero or more cell tower objects.
 *
 * <p>Please see <a href="https://developers.google.com/maps/documentation/geolocation/intro#cell_tower_object">
 * Cell Tower Object</a> for more detail.
 */
public class CellTower {
  /**
   * {@code cellId}  (required): Unique identifier of the cell. On GSM, this is the Cell ID (CID);
   * CDMA networks use the Base Station ID (BID). WCDMA networks use the UTRAN/GERAN Cell Identity
   * (UC-Id), which is a 32-bit value concatenating the Radio Network Controller (RNC) and Cell ID.
   * Specifying only the 16-bit Cell ID value in WCDMA networks may return inaccurate results.
   */
  public int cellId;
  /**
   * {@code locationAreaCode} (required): The Location Area Code (LAC) for GSM and WCDMAnetworks.
   * The Network ID (NID) for CDMA networks.
   */
  public int locationAreaCode;
   /**
    * {@code mobileCountryCode} (required): The cell tower's Mobile Country Code (MCC).
    */
  public int mobileCountryCode;
  /**
   * {@code mobileNetworkCode} (required): The cell tower's Mobile Network Code. This is the MNC for
   * GSM and WCDMA; CDMA uses the System ID (SID).
   */
  public int mobileNetworkCode;
  /* The following optional fields are not currently used, but may be included if values are available. */
  /**
   * {@code age}: The number of milliseconds since this cell was primary. If age is 0, the cellId represents
   * a current measurement.
   */
  public int age;
  /**
   * {@code signalStrength}: Radio signal strength measured in dBm.
   */
  public int signalStrength;
  /**
   *{@code timingAdvance}: The timing advance value.
   */
  public int timingAdvance;

}
