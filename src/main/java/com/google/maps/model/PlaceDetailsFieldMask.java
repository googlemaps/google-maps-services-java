package com.google.maps.model;

import com.google.maps.internal.StringJoin.UrlValue;

public enum PlaceDetailsFieldMask implements UrlValue {
  PLACE_ID("place_id"),
  NAME("name"),
  TYPE("type"),
  ADDRESS_COMPONENTS("address_components"),
  FORMATTED_ADDRESS("formatted_address"),
  URL("url"),
  UTC_OFFSET("utc_offset"),
  PERMANENTLY_CLOSED("permanently_closed"),
  GEOMETRY("geometry"),
  GEOMETRY_LOCATION("geometry.location"),
  GEOMETRY_VIEWPORT("geometry.viewport"),
  PHOTO("photo"),
  PHOTO_REFERENCE("photo.photo_reference"),
  PHOTOS("photos"),
  ICON("icon"),
  TYPES("types"),
  ADR_ADDRESS("adr_address"),
  SCOPE("scope"),
  VICINITY("vicinity"),
  OPENING_HOURS("opening_hours"),
  OPENING_HOURS_OPEN_NOW("opening_hours.open_now"),
  OPENING_HOURS_PERIOD("opening_hours.period"),
  WEBSITE("website"),
  FORMATTED_PHONE_NUMBER("formatted_phone_number"),
  INTERNATIONAL_PHONE_NUMBER("international_phone_number"),
  PRICE_LEVEL("price_level"),
  RATING("rating"),
  REVIEWS("reviews");

  private final String field;

  PlaceDetailsFieldMask(final String field) {
    this.field = field;
  }

  @Override
  public String toUrlValue() {
    return field;
  }
}
