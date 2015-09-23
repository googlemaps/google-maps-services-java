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

import java.net.URL;

/**
 * PlaceDetails is the result of a Place Details request. A Place Details request returns more
 * comprehensive information about the indicated place such as its complete address, phone number,
 * user rating and reviews.
 *
 * See <a href="https://developers.google.com/places/web-service/details#PlaceDetailsResults">
 *   Place Details Results</a> for more detail.
 */
public class PlaceDetails {

  /** addressComponents is a list of separate address components used to compose a given address. */
  public AddressComponent[] addressComponents;

  /** formattedAddress is a string containing the human-readable address of this place. */
  public String formattedAddress;

  /** formattedPhoneNumber contains the place's phone number in its local format. */
  public String formattedPhoneNumber;

  /** adrAddress is an undocumented address field.
   * @deprecated Undocumented field. See b/24313079
   */
  @Deprecated
  public String adrAddress;

  public class Geometry {
    /** The location of a Place, as a latitude, longitude pair. */
    public LatLng location;
  }

  /** geometry is the location of the Place. */
  public Geometry geometry;

  /** icon contains the URL of a suggested icon which may be displayed to the user when indicating
   * this result on a map. */
  public URL icon;

  /** internationalPhoneNumber contains the place's phone number in international format.
   * International format includes the country code, and is prefixed with the plus (+) sign. */
  public String internationalPhoneNumber;

  /** name contains the human-readable name for the returned result. */
  public String name;

  static public class OpeningHours {
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
        public DayOfWeek day;
        /**
         * Time that this Open or Close happens at.
         */
        public String time;
      }

      /**
       * When the Place opens.
       */
      public OpenClose open;

      /**
       * When the Place closes.
       */
      public OpenClose close;
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

  /** The opening hours for the place. */
  public OpeningHours openingHours;

  static public class Photo {
    /** photoReference is used to identify the photo when you perform a Photo request. */
    public String photoReference;

    /** height is the maximum height of the image. */
    public int height;

    /** width is the maximum width of the image. */
    public int width;

    /** htmlAttributions contains any required attributions. */
    public String[] htmlAttributions;
  }

  /** photos is a list of photo objects, each containing a reference to an image. */
  public Photo[] photos;

  /** placeId is a textual identifier that uniquely identifies a place. */
  public String placeId;

  public enum PlaceIdScope {
    /** APP indicates the place ID is recognised by your application only. */
    APP,
    /** GOOGLE indicates the place ID is available to other applications and on Google Maps. */
    GOOGLE
  }

  /** scope: Indicates the scope of the placeId. */
  public PlaceIdScope scope;

  static public class AlternatePlaceIds {
    /** placeId — The most likely reason for a place to have an alternative place ID is if your
     * application adds a place and receives an application-scoped place ID, then later receives a
     * Google-scoped place ID after passing the moderation process. */
    public String placeId;

    /** scope — The scope of an alternative place ID will always be APP, indicating that the
     * alternative place ID is recognised by your application only. */
    public PlaceIdScope scope;
  }

  /** altIds is an optional array of alternative place IDs for the place, with a scope related to
   * each alternative ID. */
  public AlternatePlaceIds[] altIds;

  public enum PriceLevel {
    FREE, INEXPENSIVE, MODERATE, EXPENSIVE, VERY_EXPENSIVE,

    /**
     * Indicates an unknown price level type returned by the server. The Java Client for Google Maps
     * Services should be updated to support the new value.
     */
    UNKNOWN
  }

  /** priceLevel is the price level of the place. The exact amount indicated by a specific value
   * will vary from region to region. */
  public PriceLevel priceLevel;

  /** rating contains the place's rating, from 1.0 to 5.0, based on aggregated user reviews. */
  public float rating;

  /** userRatingsTotal is an undocumented field.
   *
   * @deprecated this field is not documented.
   */
  public int userRatingsTotal;


  static public class Review {
    static public class AspectRating {
      public enum RatingType {
        APPEAL, ATMOSPHERE, DECOR, FACILITIES, FOOD, OVERALL, QUALITY, SERVICE,

        /**
         * Indicates an unknown rating type returned by the server. The Java Client for Google Maps
         * Services should be updated to support the new value.
         */
        UNKNOWN
      }

      /** type is the name of the aspect that is being rated. */
      public RatingType type;

      /** rating is the user's rating for this particular aspect, from 0 to 3. */
      public int rating;
    }

    /** aspects contains a collection of AspectRating objects, each of which provides a rating of
     * a single attribute of the establishment. */
    public AspectRating[] aspects;

    /** authorName the name of the user who submitted the review. Anonymous reviews are attributed
     * to "A Google user". */
    public String authorName;

    /** authorUrl the URL to the users Google+ profile, if available. */
    public URL authorUrl;

    /** language an IETF language code indicating the language used in the user's review. */
    public String language;

    /** rating the user's overall rating for this place. This is a whole number, ranging from 1 to
     * 5. */
    public int rating;

    /** text is the user's review. When reviewing a location with Google Places, text reviews are
     * considered optional. */
    public String text;

    /** time is the time that the review was submitted, as seconds since epoch.*/
    public long time; // TODO(brettmorgan): figure out how to convert this to a DateTime.
  }

  /** reviews is an array of up to five reviews. */
  public Review[] reviews;

  /** types contains an array of feature types describing the given result. */
  public String[] types;

  /** url contains the URL of the official Google page for this place. This will be the
   * establishment's Google+ page if the Google+ page exists, otherwise it will be the
   * Google-owned page that contains the best available information about the place. Applications
   * must link to or embed this page on any screen that shows detailed results about the place to
   * the user. */
  public URL url;

  /** utcOffset contains the number of minutes this place’s current timezone is offset from UTC. */
  public int utcOffset;

  /** vicinity lists a simplified address for the place, including the street name, street number,
   * and locality, but not the province/state, postal code, or country. */
  public String vicinity;

  /** website lists the authoritative website for this place, such as a business' homepage. */
  public URL website;

  /** htmlAttributions contains an array of attributions about this listing which must be displayed
   * to the user. */
  public String[] htmlAttributions;

  /** id is deprecated. Please do not use. This field may stop being returned at any time.
   *
   * @deprecated This field is deprecated. See the deprecation warning at the top of
   * <a href="https://developers.google.com/places/web-service/details">this page</a>
   */
  public String id;

  /** reference is deprecated. Please do not use. This field may stop being returned at any time.
   *
   * @deprecated This field is deprecated. See the deprecation warning at the top of
   * <a href="https://developers.google.com/places/web-service/details">this page</a>
   */
  public String reference;
}

