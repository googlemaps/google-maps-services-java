package com.google.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.maps.model.Size;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import org.junit.Test;

public class StaticMapsApiTest {

  private final int WIDTH = 640;
  private final int HEIGHT = 480;

  @Test
  public void testGetSydneyStaticMap() throws Exception {
    try (LocalTestServerContext sc =
        new LocalTestServerContext(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB))) {

      StaticMapsRequest req = StaticMapsApi.newRequest(sc.context, new Size(WIDTH, HEIGHT));
      req.center("Google Sydney");
      req.zoom(16);
      ByteArrayInputStream bais = new ByteArrayInputStream(req.await().imageData);
      BufferedImage img = ImageIO.read(bais);

      assertNotNull(img);
      assertEquals(WIDTH, img.getWidth());
      assertEquals(HEIGHT, img.getHeight());
    }
  }
}
