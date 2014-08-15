package com.google.maps.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;

/**
 * This class handles conversion from JSON to {@link DateTime}s.
 *
 * <p>Please see
 * {@url https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/TypeAdapter.html}
 * for more detail.
 */
public class DateTimeAdapter extends TypeAdapter<DateTime> {

  /**
   * Read a Time object from a Directions API result and convert it to a {@link DateTime}.
   *
   * <p>We are expecting to receive something akin to the following:
   * <pre>
   * {
   *   "text" : "4:27pm",
   *   "time_zone" : "Australia/Sydney",
   *   "value" : 1406528829
   * }
   * </pre>
   */
  @Override
  public DateTime read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    String timeZoneId = "";
    long secondsSinceEpoch = 0L;

    reader.beginObject();
    while (reader.hasNext()) {
      String name = reader.nextName();
      if (name.equals("text")) {
        // Ignore the human readable rendering.
        reader.nextString();
      } else if (name.equals("time_zone")) {
        timeZoneId = reader.nextString();
      } else if (name.equals("value")) {
        secondsSinceEpoch = reader.nextLong();
      }

    }
    reader.endObject();

    return new DateTime(secondsSinceEpoch * 1000, DateTimeZone.forID(timeZoneId));
  }

  /**
   * This method is not implemented.
   */
  @Override
  public void write(JsonWriter writer, DateTime value) throws IOException {
    throw new RuntimeException("Unimplemented method");
  }

}

