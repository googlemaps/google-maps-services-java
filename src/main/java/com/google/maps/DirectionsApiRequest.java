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

import static com.google.maps.internal.StringJoin.join;
import static java.util.Objects.requireNonNull;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TrafficModel;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TransitRoutingPreference;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import java.time.Instant;

/** Request for the Directions API. */
public class DirectionsApiRequest
    extends PendingResultBase<DirectionsResult, DirectionsApiRequest, DirectionsApi.Response> {

  public DirectionsApiRequest(GeoApiContext context) {
    super(context, DirectionsApi.API_CONFIG, DirectionsApi.Response.class);
  }

  protected boolean optimizeWaypoints;
  protected Waypoint[] waypoints;

  @Override
  protected void validateRequest() {
    if (!params().containsKey("origin")) {
      throw new IllegalArgumentException("Request must contain 'origin'");
    }
    if (!params().containsKey("destination")) {
      throw new IllegalArgumentException("Request must contain 'destination'");
    }
    if (params().containsKey("arrival_time") && params().containsKey("departure_time")) {
      throw new IllegalArgumentException(
          "Transit request must not contain both a departureTime and an arrivalTime");
    }
    if (params().containsKey("traffic_model") && !params().containsKey("departure_time")) {
      throw new IllegalArgumentException(
          "Specifying a traffic model requires that departure time be provided.");
    }
  }

  /**
   * The address or textual latitude/longitude value from which you wish to calculate directions. If
   * you pass an address as a location, the Directions service will geocode the location and convert
   * it to a latitude/longitude coordinate to calculate directions. If you pass coordinates, ensure
   * that no space exists between the latitude and longitude values.
   *
   * @param origin The starting location for the Directions request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest origin(String origin) {
    return param("origin", origin);
  }

  /**
   * The address or textual latitude/longitude value from which you wish to calculate directions. If
   * you pass an address as a location, the Directions service will geocode the location and convert
   * it to a latitude/longitude coordinate to calculate directions. If you pass coordinates, ensure
   * that no space exists between the latitude and longitude values.
   *
   * @param destination The ending location for the Directions request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest destination(String destination) {
    return param("destination", destination);
  }

  /**
   * The Place ID value from which you wish to calculate directions.
   *
   * @param originPlaceId The starting location Place ID for the Directions request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest originPlaceId(String originPlaceId) {
    return param("origin", prefixPlaceId(originPlaceId));
  }

  /**
   * The Place ID value from which you wish to calculate directions.
   *
   * @param destinationPlaceId The ending location Place ID for the Directions request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest destinationPlaceId(String destinationPlaceId) {
    return param("destination", prefixPlaceId(destinationPlaceId));
  }

  /**
   * The origin, as a latitude/longitude location.
   *
   * @param origin The starting location for the Directions request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest origin(LatLng origin) {
    return origin(origin.toString());
  }

  /**
   * The destination, as a latitude/longitude location.
   *
   * @param destination The ending location for the Directions request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest destination(LatLng destination) {
    return destination(destination.toString());
  }

  /**
   * Specifies the mode of transport to use when calculating directions. The mode defaults to
   * driving if left unspecified. If you set the mode to {@code TRANSIT} you must also specify
   * either a {@code departureTime} or an {@code arrivalTime}.
   *
   * @param mode The travel mode to request directions for.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest mode(TravelMode mode) {
    return param("mode", mode);
  }

  /**
   * Indicates that the calculated route(s) should avoid the indicated features.
   *
   * @param restrictions one or more of {@link DirectionsApi.RouteRestriction} objects.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest avoid(DirectionsApi.RouteRestriction... restrictions) {
    return param("avoid", join('|', restrictions));
  }

  /**
   * Specifies the unit system to use when displaying results.
   *
   * @param units The preferred units for displaying distances.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest units(Unit units) {
    return param("units", units);
  }

  /**
   * @param region The region code, specified as a ccTLD ("top-level domain") two-character value.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest region(String region) {
    return param("region", region);
  }

  /**
   * Set the arrival time for a Transit directions request.
   *
   * @param time The arrival time to calculate directions for.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest arrivalTime(Instant time) {
    return param("arrival_time", Long.toString(time.toEpochMilli() / 1000L));
  }

  /**
   * Set the departure time for a transit or driving directions request. If both departure time and
   * traffic model are not provided, then "now" is assumed. If traffic model is supplied, then
   * departure time must be specified. Duration in traffic will only be returned if the departure
   * time is specified.
   *
   * @param time The departure time to calculate directions for.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest departureTime(Instant time) {
    return param("departure_time", Long.toString(time.toEpochMilli() / 1000L));
  }

  /**
   * Set the departure time for a transit or driving directions request as the current time. If
   * traffic model is supplied, then departure time must be specified. Duration in traffic will only
   * be returned if the departure time is specified.
   *
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest departureTimeNow() {
    return param("departure_time", "now");
  }

  /**
   * Specifies a list of waypoints. Waypoints alter a route by routing it through the specified
   * location(s). A waypoint is specified as either a latitude/longitude coordinate or as an address
   * which will be geocoded. Waypoints are only supported for driving, walking and bicycling
   * directions.
   *
   * <p>For more information on waypoints, see <a
   * href="https://developers.google.com/maps/documentation/directions/intro#Waypoints">Using
   * Waypoints in Routes</a>.
   *
   * @param waypoints The waypoints to add to this directions request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest waypoints(Waypoint... waypoints) {
    if (waypoints == null || waypoints.length == 0) {
      this.waypoints = new Waypoint[0];
      param("waypoints", "");
      return this;
    } else {
      this.waypoints = waypoints;
      String[] waypointStrs = new String[waypoints.length];
      for (int i = 0; i < waypoints.length; i++) {
        waypointStrs[i] = waypoints[i].toString();
      }
      param("waypoints", (optimizeWaypoints ? "optimize:true|" : "") + join('|', waypointStrs));
      return this;
    }
  }

  /**
   * Specifies the list of waypoints as String addresses. If any of the Strings are Place IDs, you
   * must prefix them with {@code place_id:}.
   *
   * <p>See {@link #prefixPlaceId(String)}.
   *
   * <p>See {@link #waypoints(Waypoint...)}.
   *
   * @param waypoints The waypoints to add to this directions request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest waypoints(String... waypoints) {
    Waypoint[] objWaypoints = new Waypoint[waypoints.length];
    for (int i = 0; i < waypoints.length; i++) {
      objWaypoints[i] = new Waypoint(waypoints[i]);
    }
    return waypoints(objWaypoints);
  }

  /**
   * Specifies the list of waypoints as Plade ID Strings, prefixing them as required by the API.
   *
   * <p>See {@link #prefixPlaceId(String)}.
   *
   * <p>See {@link #waypoints(Waypoint...)}.
   *
   * @param waypoints The waypoints to add to this directions request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest waypointsFromPlaceIds(String... waypoints) {
    Waypoint[] objWaypoints = new Waypoint[waypoints.length];
    for (int i = 0; i < waypoints.length; i++) {
      objWaypoints[i] = new Waypoint(prefixPlaceId(waypoints[i]));
    }
    return waypoints(objWaypoints);
  }

  /**
   * The list of waypoints as latitude/longitude locations.
   *
   * <p>See {@link #waypoints(Waypoint...)}.
   *
   * @param waypoints The waypoints to add to this directions request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest waypoints(LatLng... waypoints) {
    Waypoint[] objWaypoints = new Waypoint[waypoints.length];
    for (int i = 0; i < waypoints.length; i++) {
      objWaypoints[i] = new Waypoint(waypoints[i]);
    }
    return waypoints(objWaypoints);
  }

  /**
   * Allow the Directions service to optimize the provided route by rearranging the waypoints in a
   * more efficient order.
   *
   * @param optimize Whether to optimize waypoints.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest optimizeWaypoints(boolean optimize) {
    optimizeWaypoints = optimize;
    if (waypoints != null) {
      return waypoints(waypoints);
    } else {
      return this;
    }
  }

  /**
   * If set to true, specifies that the Directions service may provide more than one route
   * alternative in the response. Note that providing route alternatives may increase the response
   * time from the server.
   *
   * @param alternateRoutes whether to return alternate routes.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest alternatives(boolean alternateRoutes) {
    if (alternateRoutes) {
      return param("alternatives", "true");
    } else {
      return param("alternatives", "false");
    }
  }

  /**
   * Specifies one or more preferred modes of transit. This parameter may only be specified for
   * requests where the mode is transit.
   *
   * @param transitModes The preferred transit modes.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest transitMode(TransitMode... transitModes) {
    return param("transit_mode", join('|', transitModes));
  }

  /**
   * Specifies preferences for transit requests. Using this parameter, you can bias the options
   * returned, rather than accepting the default best route chosen by the API.
   *
   * @param pref The transit routing preferences for this request.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest transitRoutingPreference(TransitRoutingPreference pref) {
    return param("transit_routing_preference", pref);
  }

  /**
   * Specifies the traffic model to use when requesting future driving directions. Once set, you
   * must specify a departure time.
   *
   * @param trafficModel The traffic model for estimating driving time.
   * @return Returns this {@code DirectionsApiRequest} for call chaining.
   */
  public DirectionsApiRequest trafficModel(TrafficModel trafficModel) {
    return param("traffic_model", trafficModel);
  }

  /**
   * Helper method for prefixing a Place ID, as specified by the API.
   *
   * @param placeId The Place ID to be prefixed.
   * @return Returns the Place ID prefixed with {@code place_id:}.
   */
  public String prefixPlaceId(String placeId) {
    return "place_id:" + placeId;
  }

  public static class Waypoint {
    /** The location of this waypoint, expressed as an API-recognized location. */
    private String location;
    /** Whether this waypoint is a stopover waypoint. */
    private boolean isStopover;

    /**
     * Constructs a stopover Waypoint using a String address.
     *
     * @param location Any address or location recognized by the Google Maps API.
     */
    public Waypoint(String location) {
      this(location, true);
    }

    /**
     * Constructs a Waypoint using a String address.
     *
     * @param location Any address or location recognized by the Google Maps API.
     * @param isStopover Whether this waypoint is a stopover waypoint.
     */
    public Waypoint(String location, boolean isStopover) {
      requireNonNull(location, "address may not be null");
      this.location = location;
      this.isStopover = isStopover;
    }

    /**
     * Constructs a stopover Waypoint using a Latlng location.
     *
     * @param location The LatLng coordinates of this waypoint.
     */
    public Waypoint(LatLng location) {
      this(location, true);
    }

    /**
     * Constructs a Waypoint using a LatLng location.
     *
     * @param location The LatLng coordinates of this waypoint.
     * @param isStopover Whether this waypoint is a stopover waypoint.
     */
    public Waypoint(LatLng location, boolean isStopover) {
      requireNonNull(location, "location may not be null");
      this.location = location.toString();
      this.isStopover = isStopover;
    }

    /**
     * Gets the String representation of this Waypoint, as an API request parameter fragment.
     *
     * @return The HTTP parameter fragment representing this waypoint.
     */
    @Override
    public String toString() {
      if (isStopover) {
        return location;
      } else {
        return "via:" + location;
      }
    }
  }
}
