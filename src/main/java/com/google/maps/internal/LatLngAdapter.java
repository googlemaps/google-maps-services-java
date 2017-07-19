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
import com.google.maps.model.LatLng;
import java.io.IOException;

/** Handle conversion from varying types of latitude and longitude representations. */
public class LatLngAdapter extends TypeAdapter<LatLng> {
  /**
   * Reads in a JSON object and try to create a LatLng in one of the following formats.
   *
   * <pre>{
   *   "lat" : -33.8353684,
   *   "lng" : 140.8527069
   * }
   *
   * {
   *   "latitude": -33.865257570508334,
   *   "longitude": 151.19287000481452
   * }</pre>
   */
  @Override
  public LatLng read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    double lat = 0;
    double lng = 0;
    boolean hasLat = false;
    boolean hasLng = false;

    reader.beginObject();
    while (reader.hasNext()) {
      String name = reader.nextName();
      if ("lat".equals(name) || "latitude".equals(name)) {
        lat = reader.nextDouble();
        hasLat = true;
      } else if ("lng".equals(name) || "longitude".equals(name)) {
        lng = reader.nextDouble();
        hasLng = true;
      }
    }
    reader.endObject();

    if (hasLat && hasLng) {
      return new LatLng(lat, lng);
    } else {
      return null;
    }
  }

  /** Not supported. */
  @Override
  public void write(JsonWriter out, LatLng value) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method.");
  }
}
