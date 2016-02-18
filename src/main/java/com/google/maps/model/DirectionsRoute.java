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

package com.google.maps.model;

/**
 * A Directions API result. When the Directions API returns results, it places them within a routes
 * array. Even if the service returns no results (such as if the origin and/or destination doesn't
 * exist) it still returns an empty routes array.
 *
 * <p>Please see <a href="https://developers.google.com/maps/documentation/directions/#Routes">
 * Routes</a> for more detail.
 */
public class DirectionsRoute {
  /**
   * {@code summary} contains a short textual description for the route, suitable for naming and
   * disambiguating the route from alternatives.
   */
  public String summary;

  /**
   * {@code legs} contains information about a leg of the route, between two locations within the
   * given route. A separate leg will be present for each waypoint or destination specified. (A
   * route with no waypoints will contain exactly one leg within the legs array.)
   */
  public DirectionsLeg[] legs;

  /**
   * {@code waypointOrder} contains an array indicating the order of any waypoints in the calculated
   * route. This waypoints may be reordered if the request was passed {@code optimize:true} within
   * its {@code waypoints} parameter.
   */
  public int[] waypointOrder;

  /**
   * {@code overviewPolyline} contains an object holding an array of encoded points that represent
   * an approximate (smoothed) path of the resulting directions.
   */
  public EncodedPolyline overviewPolyline;

  /**
   * {@code bounds} contains the viewport bounding box of the overview_polyline.
   */
  public Bounds bounds;

  /**
   * {@code copyrights} contains the copyrights text to be displayed for this route. You must handle
   * and display this information yourself.
   */
  public String copyrights;

  /**
   * {@code fare} contains information about the fare (that is, the ticket costs) on this route.
   * This property is only returned for transit directions, and only for routes where fare
   * information is available for all transit legs.
   */
  public Fare fare;

  /**
   * {@code warnings} contains an array of warnings to be displayed when showing these directions.
   * You must handle and display these warnings yourself.
   */
  public String[] warnings;
}
