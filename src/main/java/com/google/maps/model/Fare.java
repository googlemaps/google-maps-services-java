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
import java.math.BigDecimal;
import java.util.Currency;

/**
 * A representation of ticket cost for use on public transit.
 *
 * <p>See the <a href="https://developers.google.com/maps/documentation/directions/intro#Routes">
 * Routes Documentation</a> for more detail.
 */
public class Fare implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The currency that the amount is expressed in. */
  public Currency currency;

  /** The total fare amount, in the currency specified in {@link #currency}. */
  public BigDecimal value;

  @Override
  public String toString() {
    return String.format("%s %s", value, currency);
  }
}
