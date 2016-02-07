package com.google.maps;

import com.google.maps.internal.StringJoin;

import static com.google.maps.internal.StringJoin.join;

/**
 * This class represents a component filter for a geocode request. In a geocoding response, the
 * Google Geocoding API can return address results restricted to a specific area. The restriction
 * is specified using the components filter.
 *
 * <p>Please see
 * <a href="https://developers.google.com/maps/documentation/geocoding/#ComponentFiltering">
 * Component Filtering</a> for more detail.
 */
public class ComponentFilter implements StringJoin.UrlValue {
  private final String component;
  private final String value;

  ComponentFilter(String component, String value) {
    this.component = component;
    this.value = value;
  }

  @Override
  public String toString() {
    return toUrlValue();
  }

  @Override
  public String toUrlValue() {
    return join(':', component, value);
  }

  /**
   * {@code route} matches long or short name of a route.
   */
  public static ComponentFilter route(String route) {
    return new ComponentFilter("route", route);
  }

  /**
   * {@code locality} matches against both locality and sublocality types.
   */
  public static ComponentFilter locality(String locality) {
    return new ComponentFilter("locality", locality);
  }

  /**
   * {@code administrativeArea} matches all the administrative area levels.
   */
  public static ComponentFilter administrativeArea(String administrativeArea) {
    return new ComponentFilter("administrative_area", administrativeArea);
  }

  /**
   * {@code postalCode} matches postal code and postal code prefix.
   */
  public static ComponentFilter postalCode(String postalCode) {
    return new ComponentFilter("postal_code", postalCode);
  }

  /**
   * {@code country} matches a country name or a two letter ISO 3166-1 country code.
   */
  public static ComponentFilter country(String country) {
    return new ComponentFilter("country" , country);
  }
}
