/*
 * Copyright 2024 Google Inc. All rights reserved.
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

/**
 * The response from a Geocoding request.
 *
 * <p>Please see <a
 * href="https://developers.google.com/maps/documentation/geocoding/requests-geocoding#GeocodingResponses">Geocoding
 * Responses</a> for more detail.
 */
public class GeocodingResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The list of Geocoding Results. */
  public GeocodingResult results[];

  /** The Address Descriptor for the target. */
  public AddressDescriptor addressDescriptor;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[GeocodingResponse: ");
    sb.append(results.length).append(" results");
    if (addressDescriptor != null) {
      sb.append(", addressDescriptor=").append(addressDescriptor);
    }
    sb.append("]");
    return sb.toString();
  }
}
