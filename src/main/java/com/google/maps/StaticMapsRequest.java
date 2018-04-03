package com.google.maps;

import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.StringJoin.UrlValue;
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
    return param("zoom", zoom);
  }

  /**
   * <code>size</code> defines the rectangular dimensions of the map image.
   *
   * @param size The Size of the static map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest size(Size size) {
    return param("size", size);
  }

  /**
   * <code>scale</code> affects the number of pixels that are returned. Setting <code>scale</code>
   * to 2 returns twice as many pixels as <code>scale</code> set to 1 while retaining the same
   * coverage area and level of detail (i.e. the contents of the map doesn't change).
   *
   * @param scale The scale of the static map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest scale(int scale) {
    return param("scale", scale);
  }

  /**
   * <code>format</code> defines the format of the resulting image. By default, the Google Static
   * Maps API creates PNG images. There are several possible formats including GIF, JPEG and PNG
   * types.
   *
   * @param format The format of the static map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest format(String format) {
    return param("format", format);
  }

  public enum StaticMapType implements UrlValue {
    roadmap,
    satellite,
    terrain,
    hybrid;

    public String toUrlValue() {
      return this.name();
    }
  }

  /**
   * <code>maptype</code> defines the type of map to construct.
   *
   * @param maptype The map type of the static map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest maptype(StaticMapType maptype) {
    return param("maptype", maptype);
  }

  /**
   * <code>region</code> defines the appropriate borders to display, based on geo-political
   * sensitivities. Accepts a region code specified as a two-character ccTLD ('top-level domain')
   * value.
   *
   * @param region The region of the static map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest region(String region) {
    return param("region", region);
  }
}
