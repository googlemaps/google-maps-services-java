package com.google.maps.model;
/**
 * The distance component for Directions API results.
 */
public class Distance {

  /**
   * This is the numeric distance, always in meters. This is intended to be used only in
   * algorithmic situations, e.g. sorting results by some user specified metric.
   */
  public long inMeters;

  /**
   * This is the human friendly distance. This is rounded and in an appropriate unit for the
   * request. The units can be overriden with a request parameter.
   */
  public String humanReadable;

  @Override
  public String toString() {
    return humanReadable;
  }
}

