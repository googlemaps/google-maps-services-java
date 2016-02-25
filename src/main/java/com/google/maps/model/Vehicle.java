/*
 * Copyright 2015 Google Inc. All rights reserved.
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
 * The vehicle used on a line.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/directions/#TransitDetails">
 * Transit details</a> for more detail.
 */
public class Vehicle {

  /**
   * {@code name} contains the name of the vehicle on this line. eg. "Subway."
   */
  public String name;

  /**
   * {@code type} contains the type of vehicle that runs on this line. See the {@link
   * com.google.maps.model.VehicleType VehicleType} documentation for a complete list of supported
   * values.
   */
  public VehicleType type;

  /**
   * {@code icon} contains the URL for an icon associated with this vehicle type.
   */
  public String icon;

  /**
   * {@code localIcon} contains the URL for an icon based on the local transport signage.
   */
  public String localIcon;
}