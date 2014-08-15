package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.LatLng;

import java.util.TimeZone;

/**
 * <p>The Google Time Zone API provides a simple interface to request the time zone
 * for a location on the earth.
 * <p>See <a href="https://developers.google.com/maps/documentation/timezone/">documentation</a>.
 */
public class TimeZoneApi {
  private static final String BASE = "/maps/api/timezone/json";

  private TimeZoneApi() {
  }

  /**
   * Retrieve the {@link java.util.TimeZone} for the given location.
   */
  public static PendingResult<TimeZone> getTimeZone(GeoApiContext context, LatLng location) {
    return context.get(Response.class, FieldNamingPolicy.IDENTITY,
        BASE,
        "location", location.toString(),
        // Java has its own lookup for time -> DST, so we really only need to fetch the TZ id.
        // "timestamp" is, in effect, ignored.
        "timestamp", "0");
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
