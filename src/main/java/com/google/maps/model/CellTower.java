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
 * A cell tower object.
 *
 * <p>The Geolocation API request body's cellTowers array contains zero or more cell tower objects.
 *
 * <p>Please see <a
 * href="https://developers.google.com/maps/documentation/geolocation/intro#cell_tower_object">Cell
 * Tower Object</a> for more detail.
 */
public class CellTower implements Serializable {

  private static final long serialVersionUID = 1L;

  public CellTower() {}

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
   * Unique identifier of the cell (required). On GSM, this is the Cell ID (CID); CDMA networks use
   * the Base Station ID (BID). WCDMA networks use the UTRAN/GERAN Cell Identity (UC-Id), which is a
   * 32-bit value concatenating the Radio Network Controller (RNC) and Cell ID. Specifying only the
   * 16-bit Cell ID value in WCDMA networks may return inaccurate results.
   */
  public Integer cellId = null;
  /**
   * The Location Area Code (LAC) for GSM and WCDMAnetworks or The Network ID (NID) for CDMA
   * networks (required).
   */
  public Integer locationAreaCode = null;
  /** The cell tower's Mobile Country Code (MCC) (required). */
  public Integer mobileCountryCode = null;
  /**
   * The cell tower's Mobile Network Code (required). This is the MNC for GSM and WCDMA; CDMA uses
   * the System ID (SID).
   */
  public Integer mobileNetworkCode = null;
  /* The following optional fields are not currently used, but may be included if values are available. */
  /**
   * The number of milliseconds since this cell was primary. If age is 0, the cellId represents a
   * current measurement.
   */
  public Integer age = null;
  /** Radio signal strength measured in dBm. */
  public Integer signalStrength = null;
  /** The timing advance value. */
  public Integer timingAdvance = null;

  @Override
  public String toString() {
    return String.format(
        "[CellTower: cellId=%s, locationAreaCode=%s, mobileCountryCode=%s, "
            + "mobileNetworkCode=%s, age=%s, signalStrength=%s, timingAdvance=%s]",
        cellId,
        locationAreaCode,
        mobileCountryCode,
        mobileNetworkCode,
        age,
        signalStrength,
        timingAdvance);
  }

  public static class CellTowerBuilder {
    private Integer _cellId = null;
    private Integer _locationAreaCode = null;
    private Integer _mobileCountryCode = null;
    private Integer _mobileNetworkCode = null;
    private Integer _age = null;
    private Integer _signalStrength = null;
    private Integer _timingAdvance = null;

    // create the actual cell tower
    public CellTower createCellTower() {
      return new CellTower(
          _cellId,
          _locationAreaCode,
          _mobileCountryCode,
          _mobileNetworkCode,
          _age,
          _signalStrength,
          _timingAdvance);
    }

    public CellTowerBuilder CellId(int newCellId) {
      this._cellId = newCellId;
      return this;
    }

    public CellTowerBuilder LocationAreaCode(int newLocationAreaCode) {
      this._locationAreaCode = newLocationAreaCode;
      return this;
    }

    public CellTowerBuilder MobileCountryCode(int newMobileCountryCode) {
      this._mobileCountryCode = newMobileCountryCode;
      return this;
    }

    public CellTowerBuilder MobileNetworkCode(int newMobileNetworkCode) {
      this._mobileNetworkCode = newMobileNetworkCode;
      return this;
    }

    public CellTowerBuilder Age(int newAge) {
      this._age = newAge;
      return this;
    }

    public CellTowerBuilder SignalStrength(int newSignalStrength) {
      this._signalStrength = newSignalStrength;
      return this;
    }

    public CellTowerBuilder TimingAdvance(int newTimingAdvance) {
      this._timingAdvance = newTimingAdvance;
      return this;
    }
  }
}
