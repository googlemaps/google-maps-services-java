package com.google.maps.model;

/**
 * <p>This describes a photo available with a Search Result.</p>
 *
 * <p>Please see <a href="https://developers.google.com/places/web-service/photos">Photos</a> for more details.</p>
 */
public class Photo {
  /**
   * photoReference is used to identify the photo when you perform a Photo request.
   */
  public String photoReference;

  /**
   * height is the maximum height of the image.
   */
  public int height;

  /**
   * width is the maximum width of the image.
   */
  public int width;

  /**
   * htmlAttributions contains any required attributions.
   */
  public String[] htmlAttributions;
}
