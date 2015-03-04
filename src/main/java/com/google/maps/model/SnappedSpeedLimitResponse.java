package com.google.maps.model;

/**
 * A combined snap-to-roads and speed limit response.
 */
public class SnappedSpeedLimitResponse {

  /** Speed limit results. */
  public SpeedLimit[] speedLimits;

  /** Snap-to-road results. */
  public SnappedPoint[] snappedPoints;
}
