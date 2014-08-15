package com.google.maps.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.maps.model.Distance;

import java.io.IOException;

/**
 * This class handles conversion from JSON to {@link Distance}.
 *
 * <p>Please see
 * {@url https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/TypeAdapter.html}
 * for more detail.
 */
public class DistanceAdapter extends TypeAdapter<Distance> {

  /**
   * Read a distance object from a Directions API result and convert it to a {@link Distance}.
   *
   * <p>We are expecting to receive something akin to the following:
   * <pre>
   * {
   *   "value": 207,
       "text": "0.1 mi"
   * }
   * </pre>
   */
  @Override
  public Distance read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    Distance distance = new Distance();

    reader.beginObject();
    while (reader.hasNext()) {
      String name = reader.nextName();
      if (name.equals("text")) {
        distance.humanReadable = reader.nextString();
      } else if (name.equals("value")) {
        distance.inMeters = reader.nextLong();
      }

    }
    reader.endObject();

    return distance;
  }

  /**
   * This method is not implemented.
   */
  @Override
  public void write(JsonWriter writer, Distance value) throws IOException {
    throw new RuntimeException("Unimplemented method");
  }

}

