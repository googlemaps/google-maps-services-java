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
   * {@code currency} contains the currency indicating the currency that the amount is expressed in.
   */
  public Currency currency;

  /**
   * {@code value} contains the total fare amount, in the currency specified in {@link #currency}.
   */
  public BigDecimal value;
}
