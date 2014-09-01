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
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import org.joda.time.ReadableInstant;

/**
 * A request to the Distance Matrix API.
 */
public class DistanceMatrixApiRequest
    extends PendingResultBase<DistanceMatrix, DistanceMatrixApiRequest, Response> {

  public DistanceMatrixApiRequest(GeoApiContext context) {
    super(context, Response.class, DistanceMatrixApi.BASE);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("origins")) {
      throw new IllegalArgumentException("Request must contain 'origins'");
    }
    if (!params().containsKey("destinations")) {
      throw new IllegalArgumentException("Request must contain 'destinations'");
    }
  }

  /**
   * One or more addresses from which to calculate distance and time. The service will geocode
   * the string and convert it to a latitude/longitude coordinate to calculate directions.
   *
   * @param origins String to geocode and use as an origin point (e.g. "New York, NY")
   */
  public DistanceMatrixApiRequest origins(String... origins) {
    return param("origins", join('|', origins));
  }

  /**
   * One or more latitude/longitude values from which to calculate distance and time.
   *
   * @param points  The origin points.
   */
  public DistanceMatrixApiRequest origins(LatLng... points) {
    return param("origins", join('|', points));
  }


  /**
   * One or more addresses to which to calculate distance and time. The service will geocode the
   * string and convert it to a latitude/longitude coordinate to calculate directions.
   *
   * @param destinations  String to geocode and use as a destination point (e.g. "New Jersey, NY")
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
        || TravelMode.BICYCLING.equals(mode)) {
      return param("mode", mode);
    }
    throw new IllegalArgumentException("Distance Matrix API travel modes must be Driving, "
        + "Walking or Bicycling");
  }

  /**
   * Introduces restrictions to the route. Only one restriction can be specified.
   *
   * @param restriction  One of {@link RouteRestriction#TOLLS}, {@link RouteRestriction#FERRIES} or
   * {@link RouteRestriction#HIGHWAYS}.
   */
  public DistanceMatrixApiRequest avoid(RouteRestriction restriction) {
    return param("avoid", restriction);
  }

  /**
   * Specifies the unit system to use when expressing distance as text. Distance Matrix results
   * contain text within distance fields to indicate the distance of the calculated route.
   *
   * @see <a href="https://developers.google
   * .com/maps/documentation/distancematrix/#unit_systems">Unit systems in the Distance Matrix
   * API</a>
   *
   * @param unit One of {@link Unit#METRIC}, {@link Unit#IMPERIAL}.
   */
  public DistanceMatrixApiRequest units(Unit unit) {
    return param("units", unit);
  }

  /**
   * The departure time may be specified by Maps for Business customers for to specify the
   * departure time to receive trip duration considering current traffic conditions. The
   * departure time must be set to within a few minutes of the current time.
   *
   * @param departureTime  The time of departure.
   */
  public DistanceMatrixApiRequest departureTime(ReadableInstant departureTime) {
    return param("departure_time", Long.toString(departureTime.getMillis() / 1000L));
  }
}
