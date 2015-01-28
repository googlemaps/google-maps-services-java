package com.google.maps.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.maps.model.Fare;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * This class handles conversion from JSON to {@link com.google.maps.model.Fare}.
 */
public class FareAdapter extends TypeAdapter<Fare> {

  /**
   * Read a Fare object from the Directions API and convert to a {@link com.google.maps.model.Fare}
   *
   * <pre>{
   *   "currency": "USD",
   *   "value": 6
   * }</pre>
   */
  @Override
  public Fare read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    Fare fare = new Fare();
    reader.beginObject();
    while (reader.hasNext()) {
      String key = reader.nextName();
      if ("currency".equals(key)) {
        fare.currency = Currency.getInstance(reader.nextString());
      } else if ("value".equals(key)) {
        // this relies on nextString() being able to coerce raw numbers to strings
        fare.value = new BigDecimal(reader.nextString());
      }
    }
    reader.endObject();

    return fare;
  }

  /**
   * This method is not implemented.
   */
  @Override
  public void write(JsonWriter out, Fare value) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method");
  }
}
