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

import java.math.BigDecimal;
import java.util.Currency;

/**
 * A representation of ticket cost for use on public transit.
 *
 * See <a href="https://developers.google.com/maps/documentation/directions/#Routes">the Routes
 * Documentation</a> for more detail.
 */
public class Fare {

  /**
   * {@code currency} contains the currency indicating the currency that the amount is expressed
   * in.
   */
  public Currency currency;

  /**
   * {@code value} contains the total fare amount, in the currency specified in {@link #currency}.
   */
  public BigDecimal value;
}
