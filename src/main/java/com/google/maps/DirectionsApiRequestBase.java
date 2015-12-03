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
package com.google.maps;

import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.LatLng;
import com.google.maps.model.TrafficModel;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TransitRoutingPreference;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import org.joda.time.ReadableInstant;

import static com.google.maps.internal.StringJoin.join;

/**
 * Common base class for DirectionsApiRequest and FullDirectionsApiRequest.
 */
abstract class DirectionsApiRequestBase<T, A extends DirectionsApiRequestBase<T, A, R>,
    R extends ApiResponse<T>>
    extends PendingResultBase<T,A,R> {
  protected boolean optimizeWaypoints;
  protected String[] waypoints;

  protected DirectionsApiRequestBase(GeoApiContext context, ApiConfig config, Class<? extends R> clazz) {
    super(context, config, clazz);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("origin")) {
      throw new IllegalArgumentException("Request must contain 'origin'");
    }
    if (!params().containsKey("destination")) {
      throw new IllegalArgumentException("Request must contain 'destination'");
    }
    if (TravelMode.TRANSIT.toString().equals(params().get("mode"))
        && (params().containsKey("arrival_time") && params().containsKey("departure_time"))) {
      throw new IllegalArgumentException(
          "Transit request must not contain both a departureTime and an arrivalTime");
    }
    if (params().containsKey("traffic_model") && !params().containsKey("departure_time")) {
      throw new IllegalArgumentException("Specifying a traffic model requires that departure time"
          + " be provided.");
    }
  }

  /**
   * The address or textual latitude/longitude value from which you wish to calculate directions.
   * If you pass an address as a string, the Directions service will geocode the string and convert
   * it to a latitude/longitude coordinate to calculate directions. If you pass coordinates, ensure
   * that no space exists between the latitude and longitude values.
   */
  public A origin(String origin) {
    return param("origin", origin);
  }

  /**
   * The address or textual latitude/longitude value from which you wish to calculate directions.
   * If you pass an address as a string, the Directions service will geocode the string and convert
   * it to a latitude/longitude coordinate to calculate directions. If you pass coordinates, ensure
   * that no space exists between the latitude and longitude values.
   */
  public A destination(String destination) {
    return param("destination", destination);
  }

  /**
   * The origin, as a latitude,longitude location.
   */
  public A origin(LatLng origin) {
    return origin(origin.toString());
  }

  /**
   * The destination, as a latitude,longitude location.
   */
  public A destination(LatLng destination) {
    return destination(destination.toString());
  }

  /**
   * Specifies the mode of transport to use when calculating directions. The mode defaults to
   * driving if left unspecified. If you set the mode to {@code TRANSIT} you must also specify
   * either a {@code departureTime} or an {@code arrivalTime}.
   *
   * @param mode The travel mode to request directions for.
   */
  public A mode(TravelMode mode) {
    return param("mode", mode);
  }

  /**
   * Indicates that the calculated route(s) should avoid the indicated features.
   *
   * @param restrictions one or more of {@link DirectionsApi.RouteRestriction#TOLLS},
   *     {@link DirectionsApi.RouteRestriction#HIGHWAYS}, {@link DirectionsApi.RouteRestriction#FERRIES}
   */
  public A avoid(DirectionsApi.RouteRestriction... restrictions) {
    return param("avoid", join('|', restrictions));
  }

  /**
   * Specifies the unit system to use when displaying results.
   */
  public A units(Unit units) {
    return param("units", units);
  }

  /**
   * @param region The region code, specified as a ccTLD ("top-level domain") two-character value.
   */
  public A region(String region) {
    return param("region", region);
  }

  /**
   * Set the arrival time for a Transit directions request.
   *
   * @param time The arrival time to calculate directions for.
   */
  public A arrivalTime(ReadableInstant time) {
    return param("arrival_time", Long.toString(time.getMillis() / 1000L));
  }

  /**
   * Set the departure time for a transit or driving directions request. If both departure time
   * and traffic model are not provided, then "now" is assumed. If traffic model is supplied,
   * then departure time must be specified.
   *
   * @param time The departure time to calculate directions for.
   */
  public A departureTime(ReadableInstant time) {
    return param("departure_time", Long.toString(time.getMillis() / 1000L));
  }

  /**
   * Specifies a list of waypoints. Waypoints alter a route by routing it through the specified
   * location(s). A waypoint is specified as either a latitude/longitude coordinate or as an
   * address which will be geocoded. Waypoints are only supported for driving, walking and
   * bicycling directions.
   *
   * <p>For more information on waypoints, see
   * <a href="https://developers.google.com/maps/documentation/directions/#Waypoints">
   * Using Waypoints in Routes</a>.
   */
  public A waypoints(String... waypoints) {
    if (waypoints == null || waypoints.length == 0) {
      return (A)this;
    } else if (waypoints.length == 1) {
      return param("waypoints", waypoints[0]);
    } else {
      return param("waypoints", (optimizeWaypoints ? "optimize:true|" : "") + join('|', waypoints));
    }
  }

  /**
   * Allow the Directions service to optimize the provided route by rearranging the waypoints in a
   * more efficient order.
   */
  public A optimizeWaypoints(boolean optimize) {
    optimizeWaypoints = optimize;
    if (waypoints != null) {
      return waypoints(waypoints);
    } else {
      return (A)this;
    }
  }

  /**
   * If set to true, specifies that the Directions service may provide more than one route
   * alternative in the response. Note that providing route alternatives may increase the response
   * time from the server.
   */
  public A alternatives(boolean alternateRoutes) {
    if (alternateRoutes) {
      return param("alternatives", "true");
    } else {
      return param("alternatives", "false");
    }
  }

  /**
   * Specifies one or more preferred modes of transit. This parameter may only be specified for
   * requests where the mode is transit.
   */
  public A transitMode(TransitMode... transitModes) {
    return param("transit_mode", join('|', transitModes));
  }

  /**
   * Specifies preferences for transit requests. Using this parameter,
   * you can bias the options returned, rather than accepting the default best route chosen by
   * the API.
   */
  public A transitRoutingPreference(TransitRoutingPreference pref) {
    return param("transit_routing_preference", pref);
  }

  /**
   * Specifies the traffic model to use when requesting future driving directions. Once set, you
   * must specify a departure time.
   */
  public A trafficModel(TrafficModel trafficModel) {
    return param("traffic_model", trafficModel);
  }
}
