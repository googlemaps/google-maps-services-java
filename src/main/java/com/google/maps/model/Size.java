package com.google.maps.model;

import com.google.maps.internal.StringJoin;
import java.io.Serializable;

public class Size implements StringJoin.UrlValue, Serializable {
  private static final long serialVersionUID = 1L;

  /** The width of this Size. */
  public int width;

  /** The height of this Size. */
  public int height;

  /**
   * Constructs a Size with a height/width pair.
   *
   * @param height The height of this Size.
   * @param width The width of this Size.
   */
  public Size(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /** Serialisation constructor. */
  public Size() {}

  @Override
  public String toString() {
    return toUrlValue();
  }

  @Override
  public String toUrlValue() {
    return String.format("%dx%d", width, height);
  }
}
