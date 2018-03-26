package com.google.maps;

import com.google.maps.internal.ApiConfig;
import com.google.maps.model.LatLng;
import com.google.maps.model.Size;
import java.awt.image.BufferedImage;

public class StaticMapsRequest
    extends PendingResultBase<BufferedImage, StaticMapsRequest, BufferedImageResponse> {

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/staticmap");

  public StaticMapsRequest(GeoApiContext context) {
    super(context, API_CONFIG, BufferedImageResponse.class);
  }

  @Override
  protected void validateRequest() {
    // TODO: center and zoom required if markers aren't present.
    if (!params().containsKey("center") || !params().containsKey("zoom")) {
      throw new IllegalArgumentException("Request must contain 'center' and 'zoom'.");
    }
    if (!params().containsKey("size")) {
      throw new IllegalArgumentException("Request must contain 'size'.");
    }
  }

  /**
   * <code>center</code> (required if markers not present) defines the center of the map,
   * equidistant from all edges of the map.
   *
   * @param location The location of the center of the map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest center(LatLng location) {
    return param("center", location);
  }

  /**
   * <code>center</code> (required if markers not present) defines the center of the map,
   * equidistant from all edges of the map.
   *
   * @param location The location of the center of the map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest center(String location) {
    return param("center", location);
  }

  /**
   * <code>zoom</code> (required if markers not present) defines the zoom level of the map, which
   * determines the magnification level of the map.
   *
   * @param zoom The zoom level of the region.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest zoom(int zoom) {
    return param("zoom", Integer.toString(zoom));
  }

  public StaticMapsRequest size(Size size) {
    return param("size", size);
  }
}
