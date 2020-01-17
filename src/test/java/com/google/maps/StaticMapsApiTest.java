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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.maps.StaticMapsRequest.ImageFormat;
import com.google.maps.StaticMapsRequest.Markers;
import com.google.maps.StaticMapsRequest.Markers.CustomIconAnchor;
import com.google.maps.StaticMapsRequest.Markers.MarkersSize;
import com.google.maps.StaticMapsRequest.Path;
import com.google.maps.StaticMapsRequest.StaticMapType;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.google.maps.model.Size;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.Test;

public class StaticMapsApiTest {

  private final int WIDTH = 640;
  private final int HEIGHT = 480;
  private final LatLng MELBOURNE = new LatLng(-37.8136, 144.9630);
  private final LatLng SYDNEY = new LatLng(-33.8688, 151.2093);
  /** This encoded path matches the exact [MELBOURNE, SYDNEY] points. */
  private final String MELBOURNE_TO_SYDNEY_ENCODED_POLYLINE = "~mxeFwaxsZ_naWk~be@";

  private final BufferedImage IMAGE = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

  @Test
  public void testGetSydneyStaticMap() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {

      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));
      req.center("Google Sydney");
      req.zoom(16);
      ByteArrayInputStream bais = new ByteArrayInputStream(req.await().imageData);
      BufferedImage img = ImageIO.read(bais);

      sc.assertParamValue("640x480", "size");
      sc.assertParamValue("Google Sydney", "center");
      sc.assertParamValue("16", "zoom");

      assertNotNull(img);
      assertEquals(WIDTH, img.getWidth());
      assertEquals(HEIGHT, img.getHeight());
    }
  }

  @Test
  public void testGetSydneyLatLngStaticMap() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {

      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));
      req.center(SYDNEY);
      req.zoom(16);
      req.await();

      sc.assertParamValue("640x480", "size");
      sc.assertParamValue("-33.86880000,151.20930000", "center");
      sc.assertParamValue("16", "zoom");
    }
  }

  @Test
  public void testRequest() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {

      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));
      req.center("Sydney");
      req.zoom(16);
      req.scale(2);
      req.format(ImageFormat.png32);
      req.maptype(StaticMapType.hybrid);
      req.region("AU");
      req.visible("Melbourne");
      req.await();

      sc.assertParamValue("640x480", "size");
      sc.assertParamValue("Sydney", "center");
      sc.assertParamValue("16", "zoom");
      sc.assertParamValue("2", "scale");
      sc.assertParamValue("png32", "format");
      sc.assertParamValue("hybrid", "maptype");
      sc.assertParamValue("AU", "region");
      sc.assertParamValue("Melbourne", "visible");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateRequest_noCenter() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {
      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));
      req.zoom(16);
      req.await();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateRequest_noZoom() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {
      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));
      req.center("Google Sydney");
      req.await();
    }
  }

  @Test
  public void testValidateRequest_noCenterAndNoZoomWithMarkers() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {
      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));

      Markers markers = new Markers();
      markers.size(MarkersSize.small);
      markers.customIcon("http://not.a/real/url", CustomIconAnchor.bottomleft, 2);
      markers.color("blue");
      markers.label("A");
      markers.addLocation("Melbourne");
      markers.addLocation(SYDNEY);
      req.markers(markers);
      req.await();
    }
  }

  @Test
  public void testValidateRequest_noCenterAndNoZoomWithPath() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {
      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));
      Path path = new Path();
      path.color("green");
      path.fillcolor("0xAACCEE");
      path.weight(3);
      path.geodesic(true);
      path.addPoint("Melbourne");
      path.addPoint(SYDNEY);
      req.path(path);
      req.await();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateRequest_noSize() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {
      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, null);
      req.center("Google Sydney");
      req.zoom(16);
      req.await();
    }
  }

  @Test
  public void testMarkerAndPath() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {
      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));
      Markers markers = new Markers();
      markers.size(MarkersSize.small);
      markers.customIcon("http://not.a/real/url", CustomIconAnchor.bottomleft, 2);
      markers.color("blue");
      markers.label("A");
      markers.addLocation("Melbourne");
      markers.addLocation(SYDNEY);
      req.markers(markers);

      Path path = new Path();
      path.color("green");
      path.fillcolor("0xAACCEE");
      path.weight(3);
      path.geodesic(true);
      path.addPoint("Melbourne");
      path.addPoint(SYDNEY);
      req.path(path);

      req.await();

      sc.assertParamValue(
          "icon:http://not.a/real/url|anchor:bottomleft|scale:2|size:small|color:blue|label:A|Melbourne|-33.86880000,151.20930000",
          "markers");
      sc.assertParamValue(
          "weight:3|color:green|fillcolor:0xAACCEE|geodesic:true|Melbourne|-33.86880000,151.20930000",
          "path");
    }
  }

  @Test
  public void testMarkerAndPathAsEncodedPolyline() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {
      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));
      Markers markers = new Markers();
      markers.size(MarkersSize.small);
      markers.customIcon("http://not.a/real/url", CustomIconAnchor.bottomleft, 2);
      markers.color("blue");
      markers.label("A");
      markers.addLocation("Melbourne");
      markers.addLocation(SYDNEY);
      req.markers(markers);

      List<LatLng> points = new ArrayList<>();
      points.add(MELBOURNE);
      points.add(SYDNEY);
      EncodedPolyline path = new EncodedPolyline(points);
      req.path(path);

      req.await();

      sc.assertParamValue(
          "icon:http://not.a/real/url|anchor:bottomleft|scale:2|size:small|color:blue|label:A|Melbourne|-33.86880000,151.20930000",
          "markers");
      sc.assertParamValue("enc:" + MELBOURNE_TO_SYDNEY_ENCODED_POLYLINE, "path");
    }
  }

  @Test
  public void testBrooklynBridgeNYMarkers() throws Exception {
    try (LocalTestServerContext sc = new LocalTestServerContext(IMAGE)) {
      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));
      req.center("Brooklyn Bridge, New York, NY");
      req.zoom(13);
      req.maptype(StaticMapType.roadmap);
      {
        Markers markers = new Markers();
        markers.color("blue");
        markers.label("S");
        markers.addLocation(new LatLng(40.702147, -74.015794));
        req.markers(markers);
      }
      {
        Markers markers = new Markers();
        markers.color("green");
        markers.label("G");
        markers.addLocation(new LatLng(40.711614, -74.012318));
        req.markers(markers);
      }
      {
        Markers markers = new Markers();
        markers.color("red");
        markers.label("C");
        markers.addLocation(new LatLng(40.718217, -73.998284));
        req.markers(markers);
      }

      req.await();

      sc.assertParamValue("640x480", "size");
      sc.assertParamValue("Brooklyn Bridge, New York, NY", "center");
      sc.assertParamValue("13", "zoom");
      sc.assertParamValue("roadmap", "maptype");

      List<String> expected = new ArrayList<>();
      expected.add("color:blue|label:S|40.70214700,-74.01579400");
      expected.add("color:green|label:G|40.71161400,-74.01231800");
      expected.add("color:red|label:C|40.71821700,-73.99828400");
      sc.assertParamValues(expected, "markers");
    }
  }
}
