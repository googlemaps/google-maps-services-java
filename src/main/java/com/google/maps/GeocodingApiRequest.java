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

import com.google.maps.internal.ApiConfig;
import com.google.maps.model.AddressType;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.LocationType;

/**
 * Request for the Geocoding API.
 */
public class GeocodingApiRequest
    extends PendingResultBase<GeocodingResult[], GeocodingApiRequest, GeocodingApi.Response> {

  private static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/geocode/json");

  public GeocodingApiRequest(GeoApiContext context) {
    super(context, API_CONFIG, GeocodingApi.Response.class);
  }

  @Override
  protected void validateRequest() {
    // Must not have both address and latlng.
    if (params().containsKey("latlng") && params().containsKey("address")
        && params().containsKey("place_id")) {
      throw new IllegalArgumentException("Request must contain only one of 'address', 'latlng' "
          + "or 'place_id'.");
    }

    // Must contain at least one of place_id, address, latlng, and components;
    if (!params().containsKey("latlng") && !params().containsKey("address")
        && !params().containsKey("components") && !params().containsKey("place_id")) {
      throw new IllegalArgumentException(
          "Request must contain at least one of 'address', 'latlng', 'place_id' and 'components'.");
    }
  }

  /**
   * Create a forward geocode for {@code address}.
   */
  public GeocodingApiRequest address(String address) {
    return param("address", address);
  }

  /**
   * Create a forward geocode for {@code placeId}.
   */
  public GeocodingApiRequest place(String placeId) {
    return param("place_id", placeId);
  }

  /**
   * Create a reverse geocode for {@code latlng}.
   */
  public GeocodingApiRequest latlng(LatLng latlng) {
    return param("latlng", latlng);
  }

  /**
   * Set the bounding box of the viewport within which to bias geocode results more prominently.
   * This parameter will only influence, not fully restrict, results from the geocoder. (
   *
   * <p>For more information see <a href="https://developers.google.com/maps/documentation/geocoding/?hl=pl#Viewports">Viewports
   * documentation</a>.
   */
  public GeocodingApiRequest bounds(LatLng southWestBound, LatLng northEastBound) {
    return param("bounds", join('|', southWestBound, northEastBound));
  }

  /**
   * Set the region code, specified as a ccTLD ("top-level domain") two-character value. This
   * parameter will only influence, not fully restrict, results from the geocoder.
   *
   * <p>For more information see <a href="https://developers.google.com/maps/documentation/geocoding/?hl=pl#RegionCodes">
   * Region Codes</a>.
   */
  public GeocodingApiRequest region(String region) {
    return param("region", region);
  }

  /**
   * Set the component filters. Each component filter consists of a component:value pair and will
   * fully restrict the results from the geocoder.
   *
   * <p>For more information see <a href="https://developers.google.com/maps/documentation/geocoding/?hl=pl#ComponentFiltering">
   * Component Filtering</a>.
   */
  public GeocodingApiRequest components(ComponentFilter... filters) {
    return param("components", join('|', filters));
  }

  /**
   * Set the result type. Specifying a type will restrict the results to this type. If multiple
   * types are specified, the API will return all addresses that match any of the types.
   */
  public GeocodingApiRequest resultType(AddressType... resultTypes) {
    return param("result_type", join('|', resultTypes));
  }

  /**
   * Set the location type. Specifying a type will restrict the results to this type. If multiple
   * types are specified, the API will return all addresses that match any of the types.
   */
  public GeocodingApiRequest locationType(LocationType... locationTypes) {
    return param("location_type", join('|', locationTypes));
  }

}

