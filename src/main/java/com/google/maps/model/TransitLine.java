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

import java.io.Serializable;

/**
 * The transit line used in a step.
 *
 * <p>See <a
 * href="https://developers.google.com/maps/documentation/directions/intro#TransitDetails">Transit
 * Details</a> for more detail.
 */
public class TransitLine implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The full name of this transit line. E.g. {@code "7 Avenue Express"}. */
  public String name;

  /**
   * The short name of this transit line. This will normally be a line number, such as {@code "M7"}
   * or {@code "355"}.
   */
  public String shortName;

  /**
   * The color commonly used in signage for this transit line. The color will be specified as a hex
   * string, such as {@code "#FF0033"}.
   */
  public String color;

  /** Information about the operator(s) of this transit line. */
  public TransitAgency[] agencies;

  /** The URL for this transit line as provided by the transit agency. */
  public String url;

  /** The URL for the icon associated with this transit line. */
  public String icon;

  /**
   * The color of text commonly used for signage of this transit line. The color will be specified
   * as a hex string, such as {@code "#FF0033"}.
   */
  public String textColor;

  /** The type of vehicle used on this transit line. */
  public Vehicle vehicle;

  @Override
  public String toString() {
    return String.format("%s \"%s\"", shortName, name);
  }
}
