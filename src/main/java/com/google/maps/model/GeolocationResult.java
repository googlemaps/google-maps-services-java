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
 * Result from a Geolocation API call.
 * @author Gavin.Lin
 */
public class GeolocationResult {
  /**
   * {@code location} contains the geocoded {@code latitude,longitude} value. For normal address
   * lookups, this field is typically the most important.
   */
  public LatLng location;
  
  /**
   * {@code accuracy} The accuracy of the estimated location, in meters. 
   * This represents the radius of a circle around the given location.
   */
  public double accuracy;
  
  public GeolocationResult(LatLng location, double accuracy){
    this.location = location;
    this.accuracy = accuracy;
  }
}
