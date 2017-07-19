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
import com.google.maps.model.Fare;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

/** This class handles conversion from JSON to {@link com.google.maps.model.Fare}. */
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
      } else {
        // Be forgiving of unexpected values
        reader.skipValue();
      }
    }
    reader.endObject();

    return fare;
  }

  /** This method is not implemented. */
  @Override
  public void write(JsonWriter out, Fare value) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method");
  }
}
