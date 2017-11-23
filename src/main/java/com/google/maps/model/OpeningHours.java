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
import org.joda.time.LocalTime;

/**
 * Opening hours for a Place Details result. Please see <a
 * href="https://developers.google.com/places/web-service/details#PlaceDetailsResults">Place Details
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
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,

        /**
         * Indicates an unknown day of week type returned by the server. The Java Client for Google
         * Maps Services should be updated to support the new value.
         */
        UNKNOWN
      }

      /** Day that this Open/Close pair is for. */
      public Period.OpenClose.DayOfWeek day;

      /** Time that this Open or Close happens at. */
      public LocalTime time;
    }

    /** When the Place opens. */
    public Period.OpenClose open;

    /** When the Place closes. */
    public Period.OpenClose close;
  }

  /** Opening periods covering seven days, starting from Sunday, in chronological order. */
  public Period[] periods;

  /**
   * The formatted opening hours for each day of the week, as an array of seven strings; for
   * example, {@code "Monday: 8:30 am – 5:30 pm"}.
   */
  public String[] weekdayText;

  /**
   * Indicates that the place has permanently shut down.
   *
   * <p>Note: this field will be null if it isn't present in the response.
   */
  public Boolean permanentlyClosed;
}
