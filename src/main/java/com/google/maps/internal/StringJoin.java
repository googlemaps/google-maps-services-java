package com.google.maps.internal;

/**
 * Utility class to join strings.
 */
public class StringJoin {

  /**
   * Marker Interface to enable the URL Value enums in {@link com.google.maps.DirectionsApi} to
   * be string joinable.
   */
  public interface UrlValue {
  }

  private StringJoin() {
  }

  public static String join(char delim, String... parts) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < parts.length; i++) {
      if (i != 0) {
        result.append(delim);
      }
      result.append(parts[i]);
    }
    return result.toString();
  }

  public static String join(char delim, UrlValue... parts) {
    String[] strings = new String[parts.length];
    int i = 0;
    for (UrlValue part : parts) {
      strings[i++] = part.toString();
    }

    return join(delim, strings);
  }
}
