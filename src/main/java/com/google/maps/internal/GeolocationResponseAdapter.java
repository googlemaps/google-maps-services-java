/*
 * Copyright 2016 Google Inc. All rights reserved.
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
import com.google.maps.GeolocationApi;
import java.io.IOException;

public class GeolocationResponseAdapter extends TypeAdapter<GeolocationApi.Response> {
  /**
   * Reads in a JSON object to create a Geolocation Response. See:
   * https://developers.google.com/maps/documentation/geolocation/intro#responses
   *
   * <p>Success Case:
   *
   * <pre>
   *   {
   *     "location": {
   *       "lat": 51.0,
   *       "lng": -0.1
   *     },
   *     "accuracy": 1200.4
   *   }
   * </pre>
   *
   * Error Case: The response contains an object with a single error object with the following keys:
   *
   * <p>code: This is the same as the HTTP status of the response. {@code message}: A short
   * description of the error. {@code errors}: A list of errors which occurred. Each error contains
   * an identifier for the type of error (the reason) and a short description (the message). For
   * example, sending invalid JSON will return the following error:
   *
   * <pre>
   *   {
   *     "error": {
   *       "errors": [ {
   *           "domain": "geolocation",
   *           "reason": "notFound",
   *           "message": "Not Found",
   *           "debugInfo": "status: ZERO_RESULTS\ncom.google.api.server.core.Fault: Immu...
   *       }],
   *       "code": 404,
   *       "message": "Not Found"
   *     }
   *   }
   * </pre>
   */
  @Override
  public GeolocationApi.Response read(JsonReader reader) throws IOException {

    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    GeolocationApi.Response response = new GeolocationApi.Response();
    LatLngAdapter latLngAdapter = new LatLngAdapter();

    reader.beginObject(); // opening {
    while (reader.hasNext()) {
      String name = reader.nextName();
      // two different objects could be returned a success object containing "location" and
      // "accuracy" keys or an error object containing an "error" key
      if (name.equals("location")) {
        // we already have a parser for the LatLng object so lets use that
        response.location = latLngAdapter.read(reader);
      } else if (name.equals("accuracy")) {
        response.accuracy = reader.nextDouble();
      } else if (name.equals("error")) {
        reader.beginObject(); // the error key leads to another object...
        while (reader.hasNext()) {
          String errName = reader.nextName();
          // ...with keys "errors", "code" and "message"
          if (errName.equals("code")) {
            response.code = reader.nextInt();
          } else if (errName.equals("message")) {
            response.message = reader.nextString();
          } else if (errName.equals("errors")) {
            reader.beginArray(); // its plural because its an array of errors...
            while (reader.hasNext()) {
              reader.beginObject(); // ...and each error array element is an object...
              while (reader.hasNext()) {
                errName = reader.nextName();
                // ...with keys "reason", "domain", "debugInfo", "location", "locationType",  and
                // "message" (again)
                if (errName.equals("reason")) {
                  response.reason = reader.nextString();
                } else if (errName.equals("domain")) {
                  response.domain = reader.nextString();
                } else if (errName.equals("debugInfo")) {
                  response.debugInfo = reader.nextString();
                } else if (errName.equals("message")) {
                  // have this already
                  reader.nextString();
                } else if (errName.equals("location")) {
                  reader.nextString();
                } else if (errName.equals("locationType")) {
                  reader.nextString();
                }
              }
              reader.endObject();
            }
            reader.endArray();
          }
        }
        reader.endObject(); // closing }
      }
    }
    reader.endObject();
    return response;
  }

  /** Not supported. */
  @Override
  public void write(JsonWriter out, GeolocationApi.Response value) throws IOException {
    throw new UnsupportedOperationException("Unimplemented method.");
  }
}
