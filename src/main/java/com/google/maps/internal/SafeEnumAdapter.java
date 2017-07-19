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
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link com.google.gson.TypeAdapter} that maps case-insensitive values to an enum type. If the
 * value is not found, an UNKNOWN value is returned, and logged. This allows the server to return
 * values this client doesn't yet know about.
 *
 * @param <E> the enum type to map values to.
 */
public class SafeEnumAdapter<E extends Enum<E>> extends TypeAdapter<E> {

  private static final Logger LOG = LoggerFactory.getLogger(SafeEnumAdapter.class.getName());

  private final Class<E> clazz;
  private final E unknownValue;

  /** @param unknownValue the value to return if the value cannot be found. */
  public SafeEnumAdapter(E unknownValue) {
    if (unknownValue == null) throw new IllegalArgumentException();

    this.unknownValue = unknownValue;
    this.clazz = unknownValue.getDeclaringClass();
  }

  @Override
  public void write(JsonWriter out, E value) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method");
  }

  @Override
  public E read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    String value = reader.nextString();
    try {
      return Enum.valueOf(clazz, value.toUpperCase(Locale.ENGLISH));
    } catch (IllegalArgumentException iae) {
      LOG.warn("Unknown type for enum {}: '{}'", clazz.getName(), value);
      return unknownValue;
    }
  }
}
