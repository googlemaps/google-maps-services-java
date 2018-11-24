/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.google.maps.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * This class handles conversion from JSON to {@link ZonedDateTime}s.
 *
 * <p>Please see <a
 * href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/TypeAdapter.html">TypeAdapter</a>
 * for more detail.
 */
public class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {

  /**
   * Read a Time object from a Directions API result and convert it to a {@link ZonedDateTime}.
   *
   * <p>We are expecting to receive something akin to the following:
   *
   * <pre>
   * {
   *   "text" : "4:27pm",
   *   "time_zone" : "Australia/Sydney",
   *   "value" : 1406528829
   * }
   * </pre>
   */
  @Override
  public ZonedDateTime read(JsonReader reader) throws IOException {
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
        // Ignore the human-readable rendering.
        reader.nextString();
      } else if (name.equals("time_zone")) {
        timeZoneId = reader.nextString();
      } else if (name.equals("value")) {
        secondsSinceEpoch = reader.nextLong();
      }
    }
    reader.endObject();

    return ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(secondsSinceEpoch * 1000), ZoneId.of(timeZoneId));
  }

  /** This method is not implemented. */
  @Override
  public void write(JsonWriter writer, ZonedDateTime value) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method");
  }
}
