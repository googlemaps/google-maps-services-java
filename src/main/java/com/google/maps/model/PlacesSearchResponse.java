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

/**
 * The response from a Places Search request.
 *
 * <p>Please see <a
 * href="https://developers.google.com/places/web-service/search#PlaceSearchResponses">Places Search
 * Response</a> for more detail.
 */
public class PlacesSearchResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The list of Search Results. */
  public PlacesSearchResult results[];

  /** Attributions about this listing which must be displayed to the user. */
  public String htmlAttributions[];

  /**
   * A token that can be used to request up to 20 additional results. This field will be null if
   * there are no further results. The maximum number of results that can be returned is 60.
   *
   * <p>Note: There is a short delay between when this response is issued, and when nextPageToken
   * will become valid to execute.
   */
  public String nextPageToken;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[PlacesSearchResponse: ");
    sb.append(results.length).append(" results");
    if (nextPageToken != null) {
      sb.append(", nextPageToken=").append(nextPageToken);
    }
    if (htmlAttributions != null && htmlAttributions.length > 0) {
      sb.append(", ").append(htmlAttributions.length).append(" htmlAttributions");
    }
    sb.append("]");
    return sb.toString();
  }
}
