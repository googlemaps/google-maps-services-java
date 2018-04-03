package com.google.maps;

import com.google.maps.model.Size;

public class StaticMapsApi {

  private StaticMapsApi() {}

  /**
   * Create a new {@code StaticMapRequest}.
   *
   * @param context The {@code GeoApiContext} to make this request through.
   * @param size The size of the static map.
   */
  public static StaticMapsRequest newRequest(GeoApiContext context, Size size) {
    return new StaticMapsRequest(context).size(size);
  }
}
