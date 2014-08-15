package com.google.maps.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.maps.model.AddressComponentType;

import java.io.IOException;

/**
 * This class handles conversion from JSON to {@link AddressComponentType}s.
 *
 * <p>Please see
 * <A href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/TypeAdapter.html">
 * here for more detail</a>.
 */
public class AddressComponentTypeAdapter extends TypeAdapter<AddressComponentType> {

  /**
   * Read a address component type from a Geocoding API result and convert it to a
   * {@link AddressComponentType}.
   */
  @Override
  public AddressComponentType read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    return AddressComponentType.lookup(reader.nextString());
  }

  /**
   * This method is not implemented.
   */
  @Override
  public void write(JsonWriter writer, AddressComponentType value) throws IOException {
    throw new RuntimeException("Unimplemented method");
  }

}

