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

import java.util.ArrayList;
import java.util.List;

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
  public GeolocationPayload(){

  }
  // constructor only used by the builder class below
  private GeolocationPayload(
      Integer _homeMobileCountryCode,
      Integer _homeMobileNetworkCode,
      String _radioType,
      String _carrier,
      Boolean _considerIp,
      CellTower[] _cellTowers,
      WifiAccessPoint[] _wifiAccessPoints) {
    homeMobileCountryCode = _homeMobileCountryCode;
    homeMobileNetworkCode = _homeMobileNetworkCode;
    radioType = _radioType;
    carrier = _carrier;
    considerIp = _considerIp;
    cellTowers = _cellTowers;
    wifiAccessPoints = _wifiAccessPoints;
  }
  /**
   * {@code homeMobileCountryCode}: The mobile country code (MCC) for the device's home network.
   */
  public Integer homeMobileCountryCode = null;
    /**
    * {@code homeMobileNetworkCode}: The mobile network code (MNC) for the device's home network.
    */
  public Integer homeMobileNetworkCode = null;
    /**
    * {@code radioType}: The mobile radio type. Supported values are lte, gsm, cdma, and wcdma. While
    * this field is optional, it should be included if a value is available, for more accurate results.
    */
  public String radioType = null;
    /**
    * {@code carrier}: The carrier name.
    */
  public String carrier = null;
    /**
    * considerIp: Specifies whether to fall back to IP geolocation if wifi and cell tower signals are
    * not available. Note that the IP address in the request header may not be the IP of the device.
    * Defaults to true. Set considerIp to false to disable fall back.
    */
  public Boolean considerIp = null;
  /**
   * {@code cellTowers}: An array of cell tower objects. See the Cell Tower Objects.
   */

  public CellTower[] cellTowers;
  /**
   *  {@code wifiAccessPoints}: An array of WiFi access point objects. See the WiFi Access Point Objects.
   */
  public WifiAccessPoint[] wifiAccessPoints;

  public static class GeolocationPayloadBuilder {
    private Integer _homeMobileCountryCode = null;
    private Integer _homeMobileNetworkCode = null;
    private String _radioType = null;
    private String _carrier = null;
    private Boolean _considerIp = null;
    private CellTower[] _cellTowers = null;
    private List<CellTower> _addedCellTowers = new ArrayList<CellTower>();
    private WifiAccessPoint[] _wifiAccessPoints = null;
    private List<WifiAccessPoint> _addedWifiAccessPoints = new ArrayList<WifiAccessPoint>();

    public GeolocationPayload createGeolocationPayload()
    {
      // if wifi access points have been added individually...
      if(!_addedWifiAccessPoints.isEmpty()) {
        // ...use them as our list of access points by converting the list to an array
        _wifiAccessPoints = _addedWifiAccessPoints.toArray(new WifiAccessPoint[0]);
      } // otherwise we will simply use the array set outright

      // same logic as above for cell towers
      if(!_addedCellTowers.isEmpty()) {
        _cellTowers = _addedCellTowers.toArray(new CellTower[0]);
      }

      return new GeolocationPayload(
          _homeMobileCountryCode,
          _homeMobileNetworkCode,
          _radioType,
          _carrier,
          _considerIp,
          _cellTowers,
          _wifiAccessPoints);
    }
    public GeolocationPayloadBuilder HomeMobileCountryCode(int newHomeMobileCountryCode)
    {
      this._homeMobileCountryCode = new Integer(newHomeMobileCountryCode);
      return this;
    }
    public GeolocationPayloadBuilder HomeMobileNetworkCode(int newHomeMobileNetworkCode)
    {
      this._homeMobileNetworkCode = new Integer(newHomeMobileNetworkCode);
      return this;
    }
    public GeolocationPayloadBuilder RadioType(String newRadioType)
    {
      this._radioType = newRadioType;
      return this;
    }
    public GeolocationPayloadBuilder Carrier(String newCarrier)
    {
      this._carrier = newCarrier;
      return this;
    }
    public GeolocationPayloadBuilder ConsiderIp(boolean newConsiderIp)
    {
      this._considerIp = new Boolean(newConsiderIp);
      return this;
    }
    public GeolocationPayloadBuilder CellTowers(CellTower[] newCellTowers)
    {
      this._cellTowers = newCellTowers;
      return this;
    }
    public GeolocationPayloadBuilder AddCellTower(CellTower newCellTower)
    {
      this._addedCellTowers.add(newCellTower);
      return this;
    }
    public GeolocationPayloadBuilder WifiAccessPoints(WifiAccessPoint[] newWifiAccessPoints)
    {
      this._wifiAccessPoints = newWifiAccessPoints;
      return this;
    }
    public GeolocationPayloadBuilder AddWifiAccessPoint(WifiAccessPoint newWifiAccessPoint)
    {
      this._addedWifiAccessPoints.add(newWifiAccessPoint);
      return this;
    }
  }
}


