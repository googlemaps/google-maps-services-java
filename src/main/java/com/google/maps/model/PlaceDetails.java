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
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;

/**
 * The result of a Place Details request. A Place Details request returns more comprehensive
 * information about the indicated place such as its complete address, phone number, user rating,
 * and reviews.
 *
 * <p>See <a href= "https://developers.google.com/places/web-service/details#PlaceDetailsResults">
 * Place Details Results</a> for more detail.
 */
public class PlaceDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  /** A list of separate address components that comprise the address of this place. */
  public AddressComponent[] addressComponents;

  /** A representation of the place's address in the adr microformat. */
  public String adrAddress;

  /** The status of the business (i.e. operational, temporarily closed, etc.). */
  public String businessStatus;

  /** Specifies if the business supports curbside pickup. */
  public Boolean curbsidePickup;

  /**
   * The hours of operation for the next seven days (including today). The time period starts at
   * midnight on the date of the request and ends at 11:59 pm six days later. This field includes
   * the special_days subfield of all hours, set for dates that have exceptional hours.
   */
  public OpeningHours currentOpeningHours;

  /** Specifies if the business supports delivery. */
  public Boolean delivery;

  /** Specifies if the business supports indoor or outdoor seating options. */
  public Boolean dineIn;

  /**
   * Contains a summary of the place. A summary is comprised of a textual overview, and also
   * includes the language code for these if applicable. Summary text must be presented as-is and
   * can not be modified or altered.
   */
  public PlaceEditorialSummary editorialSummary;

  /** The human-readable address of this place. */
  public String formattedAddress;

  /** The place's phone number in its local format. */
  public String formattedPhoneNumber;

  /** The location of the Place. */
  public Geometry geometry;

  /**
   * The URL of a suggested icon which may be displayed to the user when indicating this result on a
   * map.
   */
  public URL icon;

  /**
   * The place's phone number in international format. International format includes the country
   * code, and is prefixed with the plus (+) sign.
   */
  public String internationalPhoneNumber;

  /** The human-readable name for the returned result. */
  public String name;

  /** The regular hours of operation. */
  public OpeningHours openingHours;

  /** Whether the place has permanently closed. */
  @Deprecated public boolean permanentlyClosed;

  /** A list of photos associated with this place, each containing a reference to an image. */
  public Photo[] photos;

  /** A textual identifier that uniquely identifies this place. */
  public String placeId;

  /** The scope of the placeId. */
  @Deprecated public PlaceIdScope scope;

  /** The Plus Code location identifier for this place. */
  public PlusCode plusCode;

  /**
   * The price level of the place. The exact amount indicated by a specific value will vary from
   * region to region.
   */
  public PriceLevel priceLevel;

  @Deprecated
  public static class AlternatePlaceIds implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The alternative placeId. The most likely reason for a place to have an alternative place ID
     * is if your application adds a place and receives an application-scoped place ID, then later
     * receives a Google-scoped place ID after passing the moderation process.
     */
    public String placeId;

    /**
     * The scope of an alternative place ID will always be APP, indicating that the alternative
     * place ID is recognised by your application only.
     */
    @Deprecated public PlaceIdScope scope;

    @Override
    public String toString() {
      return String.format("%s (%s)", placeId, scope);
    }
  }

  /**
   * An optional array of alternative place IDs for the place, with a scope related to each
   * alternative ID.
   */
  @Deprecated public AlternatePlaceIds[] altIds;

  /** The place's rating, from 1.0 to 5.0, based on aggregated user reviews. */
  public float rating;

  public static class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    public static class AspectRating implements Serializable {

      private static final long serialVersionUID = 1L;

      public enum RatingType {
        APPEAL,
        ATMOSPHERE,
        DECOR,
        FACILITIES,
        FOOD,
        OVERALL,
        QUALITY,
        SERVICE,

        /**
         * Indicates an unknown rating type returned by the server. The Java Client for Google Maps
         * Services should be updated to support the new value.
         */
        UNKNOWN
      }

      /** The name of the aspect that is being rated. */
      public RatingType type;

      /** The user's rating for this particular aspect, from 0 to 3. */
      public int rating;
    }

    /**
     * A list of AspectRating objects, each of which provides a rating of a single attribute of the
     * establishment.
     *
     * <p>Note: this is a <a href=
     * "https://developers.google.com/places/web-service/details#PremiumData">Premium Data</a> field
     * available to the Google Places API for Work customers.
     */
    public AspectRating[] aspects;

    /**
     * The name of the user who submitted the review. Anonymous reviews are attributed to "A Google
     * user".
     */
    public String authorName;

    /** The URL of the user's Google+ profile, if available. */
    public URL authorUrl;

    /** An IETF language code indicating the language used in the user's review. */
    public String language;

    /** The URL of the user's Google+ profile photo, if available. */
    public String profilePhotoUrl;

    /** The user's overall rating for this place. This is a whole number, ranging from 1 to 5. */
    public int rating;

    /** The relative time that the review was submitted. */
    public String relativeTimeDescription;

    /**
     * The user's review. When reviewing a location with Google Places, text reviews are considered
     * optional.
     */
    public String text;

    /** The time that the review was submitted. */
    public Instant time;
  }

  /** Specifies if the place supports reservations. */
  public Boolean reservable;

  /**
   * An array of up to five reviews. If a language parameter was specified in the Place Details
   * request, the Places Service will bias the results to prefer reviews written in that language.
   */
  public Review[] reviews;

  /**
   * Contains an array of entries for the next seven days including information about secondary
   * hours of a business. Secondary hours are different from a business's main hours. For example, a
   * restaurant can specify drive through hours or delivery hours as its secondary hours. This field
   * populates the type subfield, which draws from a predefined list of opening hours types (such as
   * DRIVE_THROUGH, PICKUP, or TAKEOUT) based on the types of the place. This field includes the
   * special_days subfield of all hours, set for dates that have exceptional hours.
   */
  public OpeningHours secondaryOpeningHours;

  /** Specifies if the place serves beer. */
  public Boolean servesBeer;

  /** Specifies if the place serves breakfast. */
  public Boolean servesBreakfast;

  /** Specifies if the place serves brunch. */
  public Boolean servesBrunch;

  /** Specifies if the place serves dinner. */
  public Boolean servesDinner;

  /** Specifies if the place serves lunch. */
  public Boolean servesLunch;

  /** Specifies if the place serves vegetarian food. */
  public Boolean servesVegetarianFood;

  /** Specifies if the place serves wine. */
  public Boolean servesWine;

  /** Specifies if the business supports takeout. */
  public Boolean takeout;

  /** Feature types describing the given result. */
  public AddressType[] types;

  /**
   * The URL of the official Google page for this place. This will be the establishment's Google+
   * page if the Google+ page exists, otherwise it will be the Google-owned page that contains the
   * best available information about the place. Applications must link to or embed this page on any
   * screen that shows detailed results about the place to the user.
   */
  public URL url;

  /** The number of user reviews for this place */
  public int userRatingsTotal;

  /** The number of minutes this place’s current timezone is offset from UTC. */
  public int utcOffset;

  /**
   * A simplified address for the place, including the street name, street number, and locality, but
   * not the province/state, postal code, or country.
   */
  public String vicinity;

  /** The authoritative website for this place, such as a business's homepage. */
  public URL website;

  /** Specifies if the place has an entrance that is wheelchair-accessible. */
  public Boolean wheelchairAccessibleEntrance;

  /** Attributions about this listing which must be displayed to the user. */
  public String[] htmlAttributions;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[PlaceDetails: ");
    sb.append("\"").append(name).append("\"");
    sb.append(" ").append(placeId).append(" (").append(scope).append(")");
    sb.append(" address=\"").append(formattedAddress).append("\"");
    if (businessStatus != null) {
      sb.append("businessStatus=").append(businessStatus);
    }
    if (curbsidePickup != null) {
      sb.append(", curbsidePickup=").append(curbsidePickup);
    }
    if (currentOpeningHours != null) {
      sb.append(", currentOpeningHours=").append(currentOpeningHours);
    }
    if (delivery != null) {
      sb.append(", delivery=").append(delivery);
    }
    if (dineIn != null) {
      sb.append(", dineIn=").append(dineIn);
    }
    if (editorialSummary != null) {
      sb.append(", editorialSummary=").append(editorialSummary);
    }
    sb.append(", geometry=").append(geometry);
    if (vicinity != null) {
      sb.append(", vicinity=").append(vicinity);
    }
    if (types != null && types.length > 0) {
      sb.append(", types=").append(Arrays.toString(types));
    }
    if (altIds != null && altIds.length > 0) {
      sb.append(", altIds=").append(Arrays.toString(altIds));
    }
    if (formattedPhoneNumber != null) {
      sb.append(", phone=").append(formattedPhoneNumber);
    }
    if (internationalPhoneNumber != null) {
      sb.append(", internationalPhoneNumber=").append(internationalPhoneNumber);
    }
    if (url != null) {
      sb.append(", url=").append(url);
    }
    if (website != null) {
      sb.append(", website=").append(website);
    }
    if (icon != null) {
      sb.append(", icon");
    }
    if (openingHours != null) {
      sb.append(", openingHours");
      sb.append(", utcOffset=").append(utcOffset);
    }
    if (priceLevel != null) {
      sb.append(", priceLevel=").append(priceLevel);
    }
    sb.append(", rating=").append(rating);
    if (permanentlyClosed) {
      sb.append(", permanentlyClosed");
    }
    if (userRatingsTotal > 0) {
      sb.append(", userRatingsTotal=").append(userRatingsTotal);
    }
    if (photos != null && photos.length > 0) {
      sb.append(", ").append(photos.length).append(" photos");
    }
    if (reservable != null) {
      sb.append(", reservable=").append(reservable);
    }
    if (reviews != null && reviews.length > 0) {
      sb.append(", ").append(reviews.length).append(" reviews");
    }
    if (secondaryOpeningHours != null) {
      sb.append(", secondaryOpeningHours=").append(secondaryOpeningHours);
    }
    if (servesBeer != null) {
      sb.append(", servesBeer=").append(servesBeer);
    }
    if (servesBreakfast != null) {
      sb.append(", servesBreakfast=").append(servesBreakfast);
    }
    if (servesBrunch != null) {
      sb.append(", servesBrunch=").append(servesBrunch);
    }
    if (servesDinner != null) {
      sb.append(", servesDinner=").append(servesDinner);
    }
    if (servesLunch != null) {
      sb.append(", servesLunch=").append(servesLunch);
    }
    if (servesVegetarianFood != null) {
      sb.append(", servesVegetarianFood=").append(servesVegetarianFood);
    }
    if (servesWine != null) {
      sb.append(", servesWine=").append(servesWine);
    }
    if (takeout != null) {
      sb.append(", takeout=").append(takeout);
    }
    if (wheelchairAccessibleEntrance != null) {
      sb.append(", wheelchairAccessibleEntrance=").append(wheelchairAccessibleEntrance);
    }
    if (htmlAttributions != null && htmlAttributions.length > 0) {
      sb.append(", ").append(htmlAttributions.length).append(" htmlAttributions");
    }
    sb.append("]");
    return sb.toString();
  }
}
