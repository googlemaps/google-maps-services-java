package com.google.maps.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.maps.model.TravelMode;

import java.io.IOException;

/**
 * This class handles conversion from JSON to {@link TravelMode}s.
 *
 * <p>Please see
 * {@url https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/TypeAdapter.html}
 * for more detail.
 */
public class TravelModeAdapter extends TypeAdapter<TravelMode> {

  /**
   * Read a travel mode from a Directions API result and convert it to a {@link TravelMode}.
   */
  @Override
  public TravelMode read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    return TravelMode.lookup(reader.nextString());
  }

  /**
   * This method is not implemented.
   */
  @Override
  public void write(JsonWriter writer, TravelMode value) throws IOException {
    throw new RuntimeException("Unimplemented method");
  }

}

