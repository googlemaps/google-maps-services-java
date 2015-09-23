package com.google.maps.model;

public class QueryAutocompletePrediction {

  /**
   * Description of the matched prediction.
   */
  public String description;

  /**
   * Deprecated field. Do not use.
   *
   * @deprecated This field is deprecated. Please see
   * <a href="https://developers.google.com/places/web-service/query#deprecation">the deprecation notice</a>.
   */
  public String id;

  /**
   * Deprecated field. Do not use.
   *
   * @deprecated This field is deprecated. Please see
   * <a href="https://developers.google.com/places/web-service/query#deprecation">the deprecation notice</a>.
   */
  public String reference;

  /**
   * The Place ID of the place.
   */
  public String placeId;

  public String types[];

  // TODO(brettmorgan): Document this on https://developers.google.com/places/web-service/query
  public static class MatchedSubstring {
    public int length;
    public int offset;
  }

  public MatchedSubstring matchedSubstrings[];

  // TODO(brettmorgan): Document this on https://developers.google.com/places/web-service/query
  public static class Term {
    public int offset;
    public String value;
  }

  public Term terms[];
}
