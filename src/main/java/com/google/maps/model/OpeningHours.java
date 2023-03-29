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

package com.google.maps.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * Opening hours for a Place Details result. Please see <a href=
 * "https://developers.google.com/places/web-service/details#PlaceDetailsResults">Place Details
 * Results</a> for more details.
 */
public class OpeningHours implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * Whether the place is open at the current time.
   *
   * <p>Note: this field will be null if it isn't present in the response.
   */
  public Boolean openNow;

  /** The opening hours for a Place for a single day. */
  public static class Period implements Serializable {

    private static final long serialVersionUID = 1L;

    public static class OpenClose implements Serializable {

      private static final long serialVersionUID = 1L;

      public enum DayOfWeek {
        SUNDAY("Sunday"),
        MONDAY("Monday"),
        TUESDAY("Tuesday"),
        WEDNESDAY("Wednesday"),
        THURSDAY("Thursday"),
        FRIDAY("Friday"),
        SATURDAY("Saturday"),

        /**
         * Indicates an unknown day of week type returned by the server. The Java Client for Google
         * Maps Services should be updated to support the new value.
         */
        UNKNOWN("Unknown");

        private DayOfWeek(String name) {
          this.name = name;
        }

        private final String name;

        public String getName() {
          return name;
        }
      }

      /** Day that this Open/Close pair is for. */
      public Period.OpenClose.DayOfWeek day;

      /** Time that this Open or Close happens at. */
      public LocalTime time;

      @Override
      public String toString() {
        return String.format("%s %s", day, time);
      }
    }

    /** When the Place opens. */
    public Period.OpenClose open;

    /** When the Place closes. */
    public Period.OpenClose close;

    @Override
    public String toString() {
      return String.format("%s - %s", open, close);
    }
  }

  /** Opening periods covering seven days, starting from Sunday, in chronological order. */
  public Period[] periods;

  /** An indicator of special hours for a Place for a single day. */
  public static class SpecialDay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * A date expressed in RFC3339 format in the local timezone for the place, for example
     * 2010-12-31.
     */
    public String date;

    /**
     * True if there are exceptional hours for this day. If true, this means that there is at least
     * one exception for this day. Exceptions cause different values to occur in the subfields of
     * currentOpeningHours and secondaryOpeningHours such as periods, DayOfWeek, openNow. The
     * exceptions apply to the hours, and the hours are used to generate the other fields.
     */
    public Boolean exceptionalHours;

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("[Special Day: ");
      if (date != null) {
        sb.append(" (\"").append(date).append("\")");
      }
      if (exceptionalHours != null) {
        sb.append(" (\"").append(exceptionalHours).append("\")");
      }
      sb.append("]");
      return sb.toString();
    }
  }

  /** An array of up to seven entries corresponding to the next seven days. */
  public SpecialDay[] specialDays;

  /**
   * A type string used to identify the type of secondary hours (for example, DRIVE_THROUGH,
   * HAPPY_HOUR, DELIVERY, TAKEOUT, KITCHEN, BREAKFAST, LUNCH, DINNER, BRUNCH, PICKUP,
   * SENIOR_HOURS). Set for secondary_opening_hours only.
   */
  public String type;

  /**
   * The formatted opening hours for each day of the week, as an array of seven strings; for
   * example, {@code "Monday: 8:30 am – 5:30 pm"}.
   */
  public String[] weekdayText;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[OpeningHours:");
    if (openNow != null && openNow) {
      sb.append(" openNow");
    }
    sb.append(" ").append(Arrays.toString(periods));
    if (specialDays != null) {
      sb.append(" (\"").append(specialDays).append("\")");
    }
    if (type != null) {
      sb.append(" (\"").append(type).append("\")");
    }
    return sb.toString();
  }
}
