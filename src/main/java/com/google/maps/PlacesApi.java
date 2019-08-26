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

import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;

/**
 * Performs a text search for places. The Google Places API enables you to get data from the same
 * database used by Google Maps and Google+ Local. Places features more than 100 million businesses
 * and points of interest that are updated frequently through owner-verified listings and
 * user-moderated contributions.
 *
 * <p>See also: <a href="https://developers.google.com/places/web-service/">Places API Web Service
 * documentation</a>.
 */
public class PlacesApi {

  private PlacesApi() {}

  /**
   * Performs a search for nearby Places.
   *
   * @param context The context on which to make Geo API requests.
   * @param location The latitude/longitude around which to retrieve place information.
   * @return Returns a NearbySearchRequest that can be configured and executed.
   */
  public static NearbySearchRequest nearbySearchQuery(GeoApiContext context, LatLng location) {
    NearbySearchRequest request = new NearbySearchRequest(context);
    request.location(location);
    return request;
  }

  /**
   * Retrieves the next page of Nearby Search results. The nextPageToken, returned in a
   * PlacesSearchResponse when there are more pages of results, encodes all of the original Nearby
   * Search Request parameters, which are thus not required on this call.
   *
   * @param context The context on which to make Geo API requests.
   * @param nextPageToken The nextPageToken returned as part of a PlacesSearchResponse.
   * @return Returns a NearbySearchRequest that can be executed.
   */
  public static NearbySearchRequest nearbySearchNextPage(
      GeoApiContext context, String nextPageToken) {
    NearbySearchRequest request = new NearbySearchRequest(context);
    request.pageToken(nextPageToken);
    return request;
  }

  /**
   * Performs a search for Places using a text query; for example, "pizza in New York" or "shoe
   * stores near Ottawa".
   *
   * @param context The context on which to make Geo API requests.
   * @param query The text string on which to search, for example: "restaurant".
   * @return Returns a TextSearchRequest that can be configured and executed.
   */
  public static TextSearchRequest textSearchQuery(GeoApiContext context, String query) {
    TextSearchRequest request = new TextSearchRequest(context);
    request.query(query);
    return request;
  }

  /**
   * Performs a search for Places using a text query; for example, "pizza in New York" or "shoe
   * stores near Ottawa".
   *
   * @param context The context on which to make Geo API requests.
   * @param query The text string on which to search, for example: "restaurant".
   * @param location The latitude/longitude around which to retrieve place information.
   * @return Returns a TextSearchRequest that can be configured and executed.
   */
  public static TextSearchRequest textSearchQuery(
      GeoApiContext context, String query, LatLng location) {
    TextSearchRequest request = new TextSearchRequest(context);
    request.query(query);
    request.location(location);
    return request;
  }

  /**
   * Performs a search for Places using a PlaceType parameter.
   *
   * @param context The context on which to make Geo API requests.
   * @param type Restricts the results to places matching the specified PlaceType.
   * @return Returns a TextSearchRequest that can be configured and executed.
   */
  public static TextSearchRequest textSearchQuery(GeoApiContext context, PlaceType type) {
    TextSearchRequest request = new TextSearchRequest(context);
    request.type(type);
    return request;
  }

  /**
   * Retrieves the next page of Text Search results. The nextPageToken, returned in a
   * PlacesSearchResponse when there are more pages of results, encodes all of the original Text
   * Search Request parameters, which are thus not required on this call.
   *
   * @param context The context on which to make Geo API requests.
   * @param nextPageToken The nextPageToken returned as part of a PlacesSearchResponse.
   * @return Returns a TextSearchRequest that can be executed.
   */
  public static TextSearchRequest textSearchNextPage(GeoApiContext context, String nextPageToken) {
    TextSearchRequest request = new TextSearchRequest(context);
    request.pageToken(nextPageToken);
    return request;
  }

  /**
   * Requests the details of a Place.
   *
   * <p>We are only enabling looking up Places by placeId as the older Place identifier, reference,
   * is deprecated. Please see the <a
   * href="https://web.archive.org/web/20170521070241/https://developers.google.com/places/web-service/details#deprecation">
   * deprecation warning</a>.
   *
   * @param context The context on which to make Geo API requests.
   * @param placeId The PlaceID to request details on.
   * @param sessionToken The Session Token for this request.
   * @return Returns a PlaceDetailsRequest that you can configure and execute.
   */
  public static PlaceDetailsRequest placeDetails(
      GeoApiContext context, String placeId, PlaceAutocompleteRequest.SessionToken sessionToken) {
    PlaceDetailsRequest request = new PlaceDetailsRequest(context);
    request.placeId(placeId);
    request.sessionToken(sessionToken);
    return request;
  }

  /**
   * Requests the details of a Place.
   *
   * <p>We are only enabling looking up Places by placeId as the older Place identifier, reference,
   * is deprecated. Please see the <a
   * href="https://web.archive.org/web/20170521070241/https://developers.google.com/places/web-service/details#deprecation">
   * deprecation warning</a>.
   *
   * @param context The context on which to make Geo API requests.
   * @param placeId The PlaceID to request details on.
   * @return Returns a PlaceDetailsRequest that you can configure and execute.
   */
  public static PlaceDetailsRequest placeDetails(GeoApiContext context, String placeId) {
    PlaceDetailsRequest request = new PlaceDetailsRequest(context);
    request.placeId(placeId);
    return request;
  }

  /**
   * Requests a Photo from a PhotoReference.
   *
   * <p>Note: If you want to use a Photo in a web browser, please retrieve the photos for a place
   * via our <a
   * href="https://developers.google.com/maps/documentation/javascript/places#places_photos">
   * JavaScript Places Library</a>. Likewise, on Android, Places Photos can be retrieved using the
   * <a href="https://developers.google.com/places/android-api/photos">Google Places API for
   * Android</a>.
   *
   * @param context The context on which to make Geo API requests.
   * @param photoReference The reference to the photo to retrieve.
   * @return Returns a PhotoRequest that you can execute.
   */
  public static PhotoRequest photo(GeoApiContext context, String photoReference) {
    PhotoRequest request = new PhotoRequest(context);
    request.photoReference(photoReference);
    return request;
  }

  /**
   * Creates a new Places Autocomplete request for a given input. The Place Autocomplete service can
   * match on full words as well as substrings. Applications can therefore send queries as the user
   * types, to provide on-the-fly place predictions.
   *
   * @param context The context on which to make Geo API requests.
   * @param input input is the text string on which to search.
   * @param sessionToken Session token, to make sure requests are billed per session, instead of per
   *     character.
   * @return Returns a PlaceAutocompleteRequest that you can configure and execute.
   */
  public static PlaceAutocompleteRequest placeAutocomplete(
      GeoApiContext context, String input, PlaceAutocompleteRequest.SessionToken sessionToken) {
    PlaceAutocompleteRequest request = new PlaceAutocompleteRequest(context);
    request.input(input);
    request.sessionToken(sessionToken);
    return request;
  }

  /**
   * Allows you to add on-the-fly geographic query predictions to your application.
   *
   * @param context The context on which to make Geo API requests.
   * @param input input is the text string on which to search.
   * @return Returns a QueryAutocompleteRequest that you can configure and execute.
   */
  public static QueryAutocompleteRequest queryAutocomplete(GeoApiContext context, String input) {
    QueryAutocompleteRequest request = new QueryAutocompleteRequest(context);
    request.input(input);
    return request;
  }

  /**
   * Find places using either search text, or a phone number.
   *
   * @param context The context on which to make Geo API requests.
   * @param input The input to search on.
   * @param inputType Whether the input is search text, or a phone number.
   * @return Returns a FindPlaceFromTextRequest that you can configure and execute.
   */
  public static FindPlaceFromTextRequest findPlaceFromText(
      GeoApiContext context, String input, FindPlaceFromTextRequest.InputType inputType) {
    FindPlaceFromTextRequest request = new FindPlaceFromTextRequest(context);
    request.input(input).inputType(inputType);
    return request;
  }
}
