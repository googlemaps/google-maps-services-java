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
import com.google.maps.model.AddressType;

import java.io.IOException;

/**
 * This class handles conversion from JSON to {@link AddressType}s.
 *
 * <p>Please see
 * <A href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/TypeAdapter.html">
 * here for more detail</a>.
 */
public class AddressTypeAdapter extends TypeAdapter<AddressType> {

  /**
   * Read a address component type from a Geocoding API result and convert it to a
   * {@link AddressComponentType}.
   */
  @Override
  public AddressType read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    return AddressType.lookup(reader.nextString());
  }

  /**
   * This method is not implemented.
   */
  @Override
  public void write(JsonWriter writer, AddressType value) throws IOException {
    throw new RuntimeException("Unimplemented method");
  }

}

