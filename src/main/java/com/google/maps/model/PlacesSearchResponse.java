package com.google.maps.model;

import com.google.maps.TextSearchRequest;

/**
 * The response from a Places Search request.
 *
 * <p>Please see <a href="https://developers.google.com/places/web-service/search#PlaceSearchResponses">Places
 * Search Response</a> for more detail.</p>
 */
public class PlacesSearchResponse {

  /**
   * The list of Search Results.
   */
  public PlacesSearchResult results[];

  /**
   * htmlAttributions contain a set of attributions about this listing which must be displayed to the user.
   */
  public String htmlAttributions[];

  /**
   * nextPageRequest is a request that can be used to request up to 20 additional results. This field will be null
   * if there are no further results. The maximum number of results that can be returned is 60. There is a short delay
   * between when this response is issued, and when nextPageRequest will become valid to execute.
   */
  public TextSearchRequest nextPageRequest;

}
