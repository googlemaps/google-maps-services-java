package com.google.maps.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.maps.model.PlaceDetails;

import java.io.IOException;

/**
 * This class handles conversion from JSON to {@link PlaceDetails.PriceLevel}.
 *
 * <p>Please see
 * <a href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/TypeAdapter.html">GSON Type
 * Adapter</a> for more detail.
 */
public class PriceLevelAdaptor extends TypeAdapter<PlaceDetails.PriceLevel> {

  @Override
  public PlaceDetails.PriceLevel read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    if (reader.peek() == JsonToken.NUMBER) {
      int day = reader.nextInt();

      switch (day) {
        case 0:
          return PlaceDetails.PriceLevel.FREE;
        case 1:
          return PlaceDetails.PriceLevel.INEXPENSIVE;
        case 2:
          return PlaceDetails.PriceLevel.MODERATE;
        case 3:
          return PlaceDetails.PriceLevel.EXPENSIVE;
        case 4:
          return PlaceDetails.PriceLevel.VERY_EXPENSIVE;
        default:
          return PlaceDetails.PriceLevel.UNKNOWN;
      }
    } else {
      return PlaceDetails.PriceLevel.UNKNOWN;
    }
  }

  /**
   * This method is not implemented.
   */
  @Override
  public void write(JsonWriter writer, PlaceDetails.PriceLevel value) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method");
  }

}
