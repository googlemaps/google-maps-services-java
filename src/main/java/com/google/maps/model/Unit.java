package com.google.maps.model;

import com.google.maps.internal.StringJoin.UrlValue;

/**
 * Units of measurement.
 */
public enum Unit implements UrlValue {
  METRIC("metric"), IMPERIAL("imperial");

  private final String type;

  Unit(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return type;
  }
}
