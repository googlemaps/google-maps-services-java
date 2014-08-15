package com.google.maps.model;
/**
 * The duration component for Directions API results.
 */
public class Duration {

  /**
   * This is the numeric duration, in seconds. This is intended to be used only in
   * algorithmic situations, e.g. sorting results by some user specified metric.
   */
  public long inSeconds;

  /**
   * This is the human friendly duration. Use this for display purposes.
   */
  public String humanReadable;

  @Override
  public String toString() {
    return humanReadable;
  }
}

