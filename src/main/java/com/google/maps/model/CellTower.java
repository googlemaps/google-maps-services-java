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
 * Cell tower objects
 *
 * The Geolocation API request body's cellTowers array contains zero or more cell tower objects.
 *
 * <p>Please see <a href="https://developers.google.com/maps/documentation/geolocation/intro#cell_tower_object">
 * Cell Tower Object</a> for more detail.
 */
public class CellTower {
  public CellTower() {

  }
  // constructor only used by the builder class below
  private CellTower(
      Integer _cellId,
      Integer _locationAreaCode,
      Integer _mobileCountryCode,
      Integer _mobileNetworkCode,
      Integer _age,
      Integer _signalStrength,
      Integer _timingAdvance) {
    this.cellId = _cellId;
    this.locationAreaCode = _locationAreaCode;
    this.mobileCountryCode = _mobileCountryCode;
    this.mobileNetworkCode = _mobileNetworkCode;
    this.age = _age;
    this.signalStrength = _signalStrength;
    this.timingAdvance = _timingAdvance;
  }
  /**
   * {@code cellId}  (required): Unique identifier of the cell. On GSM, this is the Cell ID (CID);
   * CDMA networks use the Base Station ID (BID). WCDMA networks use the UTRAN/GERAN Cell Identity
   * (UC-Id), which is a 32-bit value concatenating the Radio Network Controller (RNC) and Cell ID.
   * Specifying only the 16-bit Cell ID value in WCDMA networks may return inaccurate results.
   */
  public Integer cellId = null;
  /**
   * {@code locationAreaCode} (required): The Location Area Code (LAC) for GSM and WCDMAnetworks.
   * The Network ID (NID) for CDMA networks.
   */
  public Integer locationAreaCode = null;
   /**
    * {@code mobileCountryCode} (required): The cell tower's Mobile Country Code (MCC).
    */
  public Integer mobileCountryCode = null;
  /**
   * {@code mobileNetworkCode} (required): The cell tower's Mobile Network Code. This is the MNC for
   * GSM and WCDMA; CDMA uses the System ID (SID).
   */
  public Integer mobileNetworkCode = null;
  /* The following optional fields are not currently used, but may be included if values are available. */
  /**
   * {@code age}: The number of milliseconds since this cell was primary. If age is 0, the cellId represents
   * a current measurement.
   */
  public Integer age = null;
  /**
   * {@code signalStrength}: Radio signal strength measured in dBm.
   */
  public Integer signalStrength = null;
  /**
   *{@code timingAdvance}: The timing advance value.
   */
  public Integer timingAdvance = null;

  public static class CellTowerBuilder {
    private Integer _cellId = null;
    private Integer _locationAreaCode = null;
    private Integer _mobileCountryCode = null;
    private Integer _mobileNetworkCode = null;
    private Integer _age = null;
    private Integer _signalStrength = null;
    private Integer _timingAdvance = null;

    // create the actual cell tower
    public CellTower createCellTower()
    {
      return new CellTower(
          _cellId,
          _locationAreaCode,
          _mobileCountryCode,
          _mobileNetworkCode,
          _age,
          _signalStrength,
          _timingAdvance);
    }
    public CellTowerBuilder CellId(int newCellId)
    {
      this._cellId = new Integer(newCellId);
      return this;
    }
    public CellTowerBuilder LocationAreaCode(int newLocationAreaCode)
    {
      this._locationAreaCode = new Integer(newLocationAreaCode);
      return this;
    }
    public CellTowerBuilder MobileCountryCode(int newMobileCountryCode)
    {
      this._mobileCountryCode = new Integer(newMobileCountryCode);
      return this;
    }
    public CellTowerBuilder MobileNetworkCode(int newMobileNetworkCode)
    {
      this._mobileNetworkCode = new Integer(newMobileNetworkCode);
      return this;
    }
    public CellTowerBuilder Age(int newAge)
    {
      this._age = new Integer(newAge);
      return this;
    }
    public CellTowerBuilder SignalStrength(int newSignalStrength)
    {
      this._signalStrength = new Integer(newSignalStrength);
      return this;
    }
    public CellTowerBuilder TimingAdvance(int newTimingAdvance)
    {
      this._timingAdvance = new Integer(newTimingAdvance);
      return this;
    }
  }
}
