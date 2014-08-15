package com.google.maps.model;

/**
 * Represents a single row in a Distance Matrix API response. A row represents the results for a
 * single origin.
 */
public class DistanceMatrixRow {

  /**
   * {@code elements} contains the results for this row, or individual origin.
   */
  public DistanceMatrixElement[] elements;
}
