package com.google.maps.model;

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
