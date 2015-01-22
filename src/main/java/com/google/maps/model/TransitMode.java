package com.google.maps.model;

import com.google.maps.internal.StringJoin.UrlValue;

import java.util.Locale;

/**
 * You may specify transit mode when requesting transit directions or distances.
 */
public enum TransitMode implements UrlValue {
  BUS, SUBWAY, TRAIN, TRAM,

  /**
   * Indicates preferred travel by train, tram, light rail and subway.
   */
  RAIL;

  @Override
  public String toString() {
    return name().toLowerCase(Locale.ENGLISH);
  }

  @Override
  public String toUrlValue() {
    return name().toLowerCase(Locale.ENGLISH);
  }
}
