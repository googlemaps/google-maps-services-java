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

package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.LatLng;
import java.util.TimeZone;

/**
 * The Google Time Zone API provides a simple interface to request the time zone for a location on
 * the earth.
 *
 * <p>See the <a href="https://developers.google.com/maps/documentation/timezone/">Time Zone API
 * documentation</a>.
 */
public class TimeZoneApi {
  private static final ApiConfig API_CONFIG =
      new ApiConfig("/maps/api/timezone/json").fieldNamingPolicy(FieldNamingPolicy.IDENTITY);

  private TimeZoneApi() {}

  /**
   * Retrieves the {@link java.util.TimeZone} for the given location.
   *
   * @param context The {@link GeoApiContext} to make requests through.
   * @param location The location for which to retrieve a time zone.
   * @return Returns the time zone as a {@link PendingResult}.
   */
  public static PendingResult<TimeZone> getTimeZone(GeoApiContext context, LatLng location) {
    return context.get(
        API_CONFIG,
        Response.class,
        "location",
        location.toString(),
        // Java has its own lookup for time -> DST, so we really only need to fetch the TZ id.
        // "timestamp" is, in effect, ignored.
        "timestamp",
        "0");
  }

  private static class Response implements ApiResponse<TimeZone> {
    public String status;
    public String errorMessage;

    private String timeZoneId;

    @Override
    public boolean successful() {
      return "OK".equals(status);
    }

    @Override
    public TimeZone getResult() {
      if (timeZoneId == null) {
        return null;
      }
      return TimeZone.getTimeZone(timeZoneId);
    }

    @Override
    public ApiException getError() {
      if (successful()) {
        return null;
      }
      return ApiException.from(status, errorMessage);
    }
  }
}
