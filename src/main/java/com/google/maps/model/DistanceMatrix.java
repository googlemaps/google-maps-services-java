package com.google.maps.model;

/**
 * A complete result from a Distance Matrix API call.
 *
 * @see <a href="https://developers.google
 * .com/maps/documentation/distancematrix/#DistanceMatrixResponses">Distance Matrix Results</a>
 */
public class DistanceMatrix {

  /**
   * {@code originAddresses} contains an array of addresses as returned by the API from your
   * original request. These are formatted by the geocoder and localized according to the
   * language parameter passed with the request.
   */
  public final String[] originAddresses;

  /**
   * {@code destinationAddresses} contains an array of addresses as returned by the API from your
   * original request. As with {@link #originAddresses}, these are localized if appropriate.
   */
  public final String[] destinationAddresses;

  /**
   * {@code rows} contains an array of elements, which in turn each contain a status, duration,
   * and distance element.
   */
  public final DistanceMatrixRow[] rows;

  public DistanceMatrix(String[] originAddresses, String[] destinationAddresses,
      DistanceMatrixRow[] rows) {
    this.originAddresses = originAddresses;
    this.destinationAddresses = destinationAddresses;
    this.rows = rows;
  }
}
