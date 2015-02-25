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
 * The transit line used in a step.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/directions/#TransitDetails">
 * Transit details</a> for more detail.
 */
public class TransitLine {

  /**
   * {@code name} contains the full name of this transit line. eg. "7 Avenue Express".
   */
  public String name;

  /**
   * {@code shortName} contains the short name of this transit line. This will normally be a line
   * number, such as "M7" or "355".
   */
  public String shortName;

  /**
   * {@code color} contains the color commonly used in signage for this transit line. The color will
   * be specified as a hex string such as: #FF0033.
   */
  public String color;

  /**
   * {@code agencies} contains an array of TransitAgency objects that each provide information about
   * the operator of the line.
   */
  public TransitAgency[] agencies;

  /**
   * {@code url} contains the URL for this transit line as provided by the transit agency.
   */
  public String url;

  /**
   * {@code icon} contains the URL for the icon associated with this line.
   */
  public String icon;

  /**
   * {@code textColor} contains the color of text commonly used for signage of this line. The color
   * will be specified as a hex string.
   */
  public String textColor;

  /**
   * {@code vehicle} contains the type of vehicle used on this line.
   */
  public Vehicle vehicle;
}
