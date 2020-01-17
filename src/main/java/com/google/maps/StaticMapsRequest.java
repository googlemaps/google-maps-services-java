/*
 * Copyright 2018 Google Inc. All rights reserved.
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

import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.StringJoin;
import com.google.maps.internal.StringJoin.UrlValue;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.google.maps.model.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StaticMapsRequest
    extends PendingResultBase<ImageResult, StaticMapsRequest, ImageResult.Response> {

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/staticmap");

  public StaticMapsRequest(GeoApiContext context) {
    super(context, API_CONFIG, ImageResult.Response.class);
  }

  @Override
  protected void validateRequest() {
    if (!((params().containsKey("center") && params().containsKey("zoom"))
        || params().containsKey("markers")
        || params().containsKey("path"))) {
      throw new IllegalArgumentException(
          "Request must contain 'center' and 'zoom' if 'markers' or 'path' aren't present.");
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

  public enum ImageFormat implements UrlValue {
    png("png"),
    png8("png8"),
    png32("png32"),
    gif("gif"),
    jpg("jpg"),
    jpgBaseline("jpg-baseline");

    private final String format;

    ImageFormat(String format) {
      this.format = format;
    }

    @Override
    public String toUrlValue() {
      return format;
    }
  }

  /**
   * <code>format</code> defines the format of the resulting image. By default, the Google Static
   * Maps API creates PNG images. There are several possible formats including GIF, JPEG and PNG
   * types.
   *
   * @param format The format of the static map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest format(ImageFormat format) {
    return param("format", format);
  }

  public enum StaticMapType implements UrlValue {
    roadmap,
    satellite,
    terrain,
    hybrid;

    @Override
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

  public static class Markers implements UrlValue {

    public enum MarkersSize implements UrlValue {
      tiny,
      mid,
      small,
      normal;

      @Override
      public String toUrlValue() {
        return this.name();
      }
    }

    public enum CustomIconAnchor implements UrlValue {
      top,
      bottom,
      left,
      right,
      center,
      topleft,
      topright,
      bottomleft,
      bottomright;

      @Override
      public String toUrlValue() {
        return this.name();
      }
    }

    private MarkersSize size;
    private String color;
    private String label;
    private String customIconURL;
    private CustomIconAnchor anchorPoint;
    private Integer scale;
    private final List<String> locations = new ArrayList<>();

    /**
     * Specifies the size of marker. If no size parameter is set, the marker will appear in its
     * default (normal) size.
     *
     * @param size The size of the markers.
     */
    public void size(MarkersSize size) {
      this.size = size;
    }

    /**
     * Specifies a 24-bit color (example: color=0xFFFFCC) or a predefined color from the set {black,
     * brown, green, purple, yellow, blue, gray, orange, red, white}.
     *
     * @param color The color of the markers.
     */
    public void color(String color) {
      this.color = color;
    }

    private static final Pattern labelPattern = Pattern.compile("^[A-Z0-9]$");

    /**
     * Specifies a single uppercase alphanumeric character from the set {A-Z, 0-9}.
     *
     * @param label The label to add to markers.
     */
    public void label(String label) {
      if (!labelPattern.matcher(label).matches()) {
        throw new IllegalArgumentException(
            "Label '" + label + "' doesn't match acceptable label pattern.");
      }

      this.label = label;
    }

    /**
     * Set a custom icon for these markers.
     *
     * @param url URL for the custom icon.
     * @param anchorPoint The anchor point for this custom icon.
     */
    public void customIcon(String url, CustomIconAnchor anchorPoint) {
      this.customIconURL = url;
      this.anchorPoint = anchorPoint;
    }

    /**
     * Set a custom icon for these markers.
     *
     * @param url URL for the custom icon.
     * @param anchorPoint The anchor point for this custom icon.
     * @param scale Set the image density scale (1, 2, or 4) of the custom icon provided.
     */
    public void customIcon(String url, CustomIconAnchor anchorPoint, int scale) {
      this.customIconURL = url;
      this.anchorPoint = anchorPoint;
      this.scale = scale;
    }

    /**
     * Add the location of a marker. At least one is required.
     *
     * @param location The location of the added marker.
     */
    public void addLocation(String location) {
      locations.add(location);
    }

    /**
     * Add the location of a marker. At least one is required.
     *
     * @param location The location of the added marker.
     */
    public void addLocation(LatLng location) {
      locations.add(location.toUrlValue());
    }

    @Override
    public String toUrlValue() {
      List<String> urlParts = new ArrayList<>();

      if (customIconURL != null) {
        urlParts.add("icon:" + customIconURL);
      }

      if (anchorPoint != null) {
        urlParts.add("anchor:" + anchorPoint.toUrlValue());
      }

      if (scale != null) {
        urlParts.add("scale:" + scale);
      }

      if (size != null && size != MarkersSize.normal) {
        urlParts.add("size:" + size.toUrlValue());
      }

      if (color != null) {
        urlParts.add("color:" + color);
      }

      if (label != null) {
        urlParts.add("label:" + label);
      }

      urlParts.addAll(locations);

      return StringJoin.join('|', urlParts.toArray(new String[urlParts.size()]));
    }
  }

  /**
   * <code>markers</code> parameter defines a set of one or more markers (map pins) at a set of
   * locations. Each marker defined within a single markers declaration must exhibit the same visual
   * style; if you wish to display markers with different styles, you will need to supply multiple
   * markers parameters with separate style information.
   *
   * @param markers A group of markers with the same style.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest markers(Markers markers) {
    return paramAddToList("markers", markers);
  }

  public static class Path implements UrlValue {

    private int weight;
    private String color;
    private String fillcolor;
    private boolean geodesic;
    private final List<String> points = new ArrayList<>();

    /**
     * Specifies the thickness of the path in pixels. If no weight parameter is set, the path will
     * appear in its default thickness (5 pixels).
     *
     * @param weight The thickness of the path in pixels.
     */
    public void weight(int weight) {
      this.weight = weight;
    }

    /**
     * Specifies a 24-bit color (example: color=0xFFFFCC) or a predefined color from the set {black,
     * brown, green, purple, yellow, blue, gray, orange, red, white}.
     *
     * @param color The color of the path.
     */
    public void color(String color) {
      this.color = color;
    }

    /**
     * Specifies a 24-bit color (example: color=0xFFFFCC) or a predefined color from the set {black,
     * brown, green, purple, yellow, blue, gray, orange, red, white}.
     *
     * @param color The fill color.
     */
    public void fillcolor(String color) {
      this.fillcolor = color;
    }

    /**
     * Indicates that the requested path should be interpreted as a geodesic line that follows the
     * curvature of the earth.
     *
     * @param geodesic Whether the path is geodesic.
     */
    public void geodesic(boolean geodesic) {
      this.geodesic = geodesic;
    }

    /**
     * Add a point to the path. At least two are required.
     *
     * @param point The point to add.
     */
    public void addPoint(String point) {
      points.add(point);
    }

    /**
     * Add a point to the path. At least two are required.
     *
     * @param point The point to add.
     */
    public void addPoint(LatLng point) {
      points.add(point.toUrlValue());
    }

    @Override
    public String toUrlValue() {
      List<String> urlParts = new ArrayList<>();

      if (weight > 0) {
        urlParts.add("weight:" + weight);
      }

      if (color != null) {
        urlParts.add("color:" + color);
      }

      if (fillcolor != null) {
        urlParts.add("fillcolor:" + fillcolor);
      }

      if (geodesic) {
        urlParts.add("geodesic:" + geodesic);
      }

      urlParts.addAll(points);

      return StringJoin.join('|', urlParts.toArray(new String[urlParts.size()]));
    }
  }

  /**
   * The <code>path</code> parameter defines a set of one or more locations connected by a path to
   * overlay on the map image.
   *
   * @param path A path to render atop the map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest path(Path path) {
    return paramAddToList("path", path);
  }

  /**
   * The <code>path</code> parameter defines a set of one or more locations connected by a path to
   * overlay on the map image. This variant of the method accepts the path as an EncodedPolyline.
   *
   * @param path A path to render atop the map, as an EncodedPolyline.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest path(EncodedPolyline path) {
    return paramAddToList("path", "enc:" + path.getEncodedPath());
  }

  /**
   * <code>visible</code> instructs the Google Static Maps API service to construct a map such that
   * the existing locations remain visible.
   *
   * @param visibleLocation The location to be made visible in the requested Static Map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest visible(LatLng visibleLocation) {
    return param("visible", visibleLocation);
  }

  /**
   * <code>visible</code> instructs the Google Static Maps API service to construct a map such that
   * the existing locations remain visible.
   *
   * @param visibleLocation The location to be made visible in the requested Static Map.
   * @return Returns this {@code StaticMapsRequest} for call chaining.
   */
  public StaticMapsRequest visible(String visibleLocation) {
    return param("visible", visibleLocation);
  }
}
