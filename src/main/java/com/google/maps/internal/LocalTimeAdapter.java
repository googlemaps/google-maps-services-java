/*
 * Copyright 2015 Google Inc. All rights reserved.
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** This class handles conversion from JSON to {@link LocalTime}. */
public class LocalTimeAdapter extends TypeAdapter<LocalTime> {
  /** Read a time from the Places API and convert to a {@link LocalTime} */
  @Override
  public LocalTime read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    if (reader.peek() == JsonToken.STRING) {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmm");
      return LocalTime.parse(reader.nextString(), dtf);
    }

    throw new UnsupportedOperationException("Unsupported format");
  }

  /** This method is not implemented. */
  @Override
  public void write(JsonWriter out, LocalTime value) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method");
  }
}
