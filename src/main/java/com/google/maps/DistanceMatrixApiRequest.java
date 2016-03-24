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

import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.DistanceMatrixApi.Response;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TransitRoutingPreference;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.google.maps.model.TrafficModel;

import org.joda.time.ReadableInstant;

/**
 * A request to the Distance Matrix API.
 */
public class DistanceMatrixApiRequest
    extends PendingResultBase<DistanceMatrix, DistanceMatrixApiRequest, Response> {

  public DistanceMatrixApiRequest(GeoApiContext context) {
    super(context, DistanceMatrixApi.API_CONFIG, Response.class);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("origins")) {
      throw new IllegalArgumentException("Request must contain 'origins'");
    }
    if (!params().containsKey("destinations")) {
      throw new IllegalArgumentException("Request must contain 'destinations'");
    }
    if (TravelMode.TRANSIT.toString().equals(params().get("mode"))
        && (params().containsKey("arrival_time") && params().containsKey("departure_time"))) {
      throw new IllegalArgumentException(
          "Transit request must not contain both a departureTime and an arrivalTime");
    }
  }

  /**
   * One or more addresses from which to calculate distance and time. The service will geocode the
   * string and convert it to a latitude/longitude coordinate to calculate directions.
   *
   * @param origins String to geocode and use as an origin point (e.g. "New York, NY")
   */
  public DistanceMatrixApiRequest origins(String... origins) {
    return param("origins", join('|', origins));
  }

  /**
   * One or more latitude/longitude values from which to calculate distance and time.
   *
   * @param points The origin points.
   */
  public DistanceMatrixApiRequest origins(LatLng... points) {
    return param("origins", join('|', points));
  }


  /**
   * One or more addresses to which to calculate distance and time. The service will geocode the
   * string and convert it to a latitude/longitude coordinate to calculate directions.
   *
   * @param destinations String to geocode and use as a destination point (e.g. "New Jersey, NY")
   */
  public DistanceMatrixApiRequest destinations(String... destinations) {
    return param("destinations", join('|', destinations));
  }

  /**
   * One or more latitude/longitude values to which to calculate distance and time.
   *
   * @param points The destination points.
   */
  public DistanceMatrixApiRequest destinations(LatLng... points) {
    return param("destinations", join('|', points));
  }

  /**
   * Specifies the mode of transport to use when calculating directions.
   *
   * <p>Note that Distance Matrix requests only support {@link TravelMode#DRIVING},
   * {@link TravelMode#WALKING} and {@link TravelMode#BICYCLING}.

   * @param mode  One of the travel modes supported by the Distance Matrix API.
   */
  public DistanceMatrixApiRequest mode(TravelMode mode) {
    if (TravelMode.DRIVING.equals(mode)
        || TravelMode.WALKING.equals(mode)
        || TravelMode.BICYCLING.equals(mode)
        || TravelMode.TRANSIT.equals(mode)) {
      return param("mode", mode);
    }
    throw new IllegalArgumentException("Distance Matrix API travel modes must be Driving, "
        + "Transit, Walking or Bicycling");
  }

  /**
   * Introduces restrictions to the route. Only one restriction can be specified.
   *
   * @param restriction One of {@link RouteRestriction#TOLLS}, {@link RouteRestriction#FERRIES} or
   *                    {@link RouteRestriction#HIGHWAYS}.
   */
  public DistanceMatrixApiRequest avoid(RouteRestriction restriction) {
    return param("avoid", restriction);
  }

  /**
   * Specifies the unit system to use when expressing distance as text. Distance Matrix results
   * contain text within distance fields to indicate the distance of the calculated route.
   *
   * @param unit One of {@link Unit#METRIC}, {@link Unit#IMPERIAL}.
   * @see <a href="https://developers.google.com/maps/documentation/distancematrix/#unit_systems">
   * Unit systems in the Distance Matrix API</a>
   */
  public DistanceMatrixApiRequest units(Unit unit) {
    return param("units", unit);
  }

  /**
   * Specifies the desired time of departure.
   *
   * <p>The departure time may be specified in two cases: <ul><li>For requests where the travel mode
   * is transit: You can optionally specify one of departure_time or arrival_time. If neither time
   * is specified, the departure_time defaults to now (that is, the departure time defaults to the
   * current time).</li> <li>For requests where the travel mode is driving: Google Maps API for Work
   * customers can specify the departure_time to receive trip duration considering current traffic
   * conditions. The departure_time must be set to within a few minutes of the current time.</li>
   * </ul>
   *
   * <p>Setting the parameter to null will remove it from the API request.
   *
   * @param departureTime The time of departure.
   */
  public DistanceMatrixApiRequest departureTime(ReadableInstant departureTime) {
    return param("departure_time", Long.toString(departureTime.getMillis() / 1000L));
  }

  /**
   * Specifies the assumptions to use when calculating time in traffic.  This
   * parameter may only be specified when the travel mode is driving and
   * the request includes a departure_time.
   */
  public DistanceMatrixApiRequest trafficModel(TrafficModel trafficModel) {
    return param("traffic_model", trafficModel);
  }

  /**
   * Specifies the desired time of arrival for transit requests. You can specify either
   * departure_time or arrival_time, but not both.
   */
  public DistanceMatrixApiRequest arrivalTime(ReadableInstant arrivalTime) {
    return param("arrival_time", Long.toString(arrivalTime.getMillis() / 1000L));
  }

  /**
   * Specifies one or more preferred modes of transit. This parameter may only be specified for
   * requests where the mode is transit.
   */
  public DistanceMatrixApiRequest transitModes(TransitMode... transitModes) {
    return param("transit_mode", join('|', transitModes));
  }

  /**
   * Specifies preferences for transit requests. Using this parameter, you can bias the options
   * returned, rather than accepting the default best route chosen by the API.
   */
  public DistanceMatrixApiRequest transitRoutingPreference(TransitRoutingPreference pref) {
    return param("transit_routing_preference", pref);
  }
}