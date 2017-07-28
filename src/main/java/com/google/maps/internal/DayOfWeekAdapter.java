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
import com.google.maps.model.OpeningHours.Period.OpenClose.DayOfWeek;
import java.io.IOException;

/**
 * This class handles conversion from JSON to {@link DayOfWeek}.
 *
 * <p>Please see <a
 * href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/TypeAdapter.html">GSON
 * Type Adapter</a> for more detail.
 */
public class DayOfWeekAdapter extends TypeAdapter<DayOfWeek> {

  @Override
  public DayOfWeek read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    if (reader.peek() == JsonToken.NUMBER) {
      int day = reader.nextInt();

      switch (day) {
        case 0:
          return DayOfWeek.SUNDAY;
        case 1:
          return DayOfWeek.MONDAY;
        case 2:
          return DayOfWeek.TUESDAY;
        case 3:
          return DayOfWeek.WEDNESDAY;
        case 4:
          return DayOfWeek.THURSDAY;
        case 5:
          return DayOfWeek.FRIDAY;
        case 6:
          return DayOfWeek.SATURDAY;
      }
    }

    return DayOfWeek.UNKNOWN;
  }

  /** This method is not implemented. */
  @Override
  public void write(JsonWriter writer, DayOfWeek value) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method");
  }
}
