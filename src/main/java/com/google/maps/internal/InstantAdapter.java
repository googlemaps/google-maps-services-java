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
import java.time.Instant;

/** This class handles conversion from JSON to {@link Instant}. */
public class InstantAdapter extends TypeAdapter<Instant> {

  /** Read a time from the Places API and convert to a {@link Instant} */
  @Override
  public Instant read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    if (reader.peek() == JsonToken.NUMBER) {
      // Number is the number of seconds since Epoch.
      return Instant.ofEpochMilli(reader.nextLong() * 1000L);
    }

    throw new UnsupportedOperationException("Unsupported format");
  }

  /** This method is not implemented. */
  @Override
  public void write(JsonWriter out, Instant value) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method");
  }
}
