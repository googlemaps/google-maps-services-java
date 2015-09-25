package com.google.maps.model;

public class OpeningHours {
  /**
   * openNow is a boolean value indicating if the place is open at the current time.
   */
  public Boolean openNow;

  static public class Period {
    static public class OpenClose {
      public enum DayOfWeek {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY,

        /**
         * Indicates an unknown day of week type returned by the server. The Java Client for Google Maps
         * Services should be updated to support the new value.
         */
        UNKNOWN
      }

      /**
       * Day that this Open/Close pair is for.
       */
      public Period.OpenClose.DayOfWeek day;
      /**
       * Time that this Open or Close happens at.
       */
      public String time;
    }

    /**
     * When the Place opens.
     */
    public Period.OpenClose open;

    /**
     * When the Place closes.
     */
    public Period.OpenClose close;
  }

  /**
   * periods is an array of opening periods covering seven days, starting from Sunday, in
   * chronological order.
   */
  public Period[] periods;

  /**
   * weekdayText is an array of seven strings representing the formatted opening hours for each
   * day of the week.
   */
  public String[] weekdayText;

  /**
   * permanentlyClosed indicates whether the place has permanently shut down.
   */
  public Boolean permanentlyClosed;
}
