package com.google.maps;

public class StaticMapsApi {

  private StaticMapsApi() {}

  public static StaticMapsRequest newRequest(GeoApiContext context) {
    return new StaticMapsRequest(context);
  }
}
