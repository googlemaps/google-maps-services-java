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

import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixRow;

/**
 * The Google Distance Matrix API is a service that provides travel distance and time for a matrix
 * of origins and destinations. The information returned is based on the recommended route between
 * start and end points, as calculated by the Google Maps API, and consists of rows containing
 * duration and distance values for each pair.
 *
 * <p>This service does not return detailed route information. Route information can be obtained by
 * passing the desired single origin and destination to the Directions API, using {@link
 * com.google.maps.DirectionsApi}.
 *
 * <p><strong>Note:</strong> You can display Distance Matrix API results on a Google Map, or without
 * a map. If you want to display Distance Matrix API results on a map, then these results must be
 * displayed on a Google Map. It is prohibited to use Distance Matrix API data on a map that is not
 * a Google map.
 *
 * @see <a href="https://developers.google.com/maps/documentation/distancematrix/">Distance Matrix
 *     API Documentation</a>
 */
public class DistanceMatrixApi {
  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/distancematrix/json");

  private DistanceMatrixApi() {}

  public static DistanceMatrixApiRequest newRequest(GeoApiContext context) {
    return new DistanceMatrixApiRequest(context);
  }

  public static DistanceMatrixApiRequest getDistanceMatrix(
      GeoApiContext context, String[] origins, String[] destinations) {
    return newRequest(context).origins(origins).destinations(destinations);
  }

  public static class Response implements ApiResponse<DistanceMatrix> {
    public String status;
    public String errorMessage;
    public String[] originAddresses;
    public String[] destinationAddresses;
    public DistanceMatrixRow[] rows;

    @Override
    public boolean successful() {
      return "OK".equals(status);
    }

    @Override
    public ApiException getError() {
      if (successful()) {
        return null;
      }
      return ApiException.from(status, errorMessage);
    }

    @Override
    public DistanceMatrix getResult() {
      return new DistanceMatrix(originAddresses, destinationAddresses, rows);
    }
  }
}
