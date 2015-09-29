package com.google.maps;

import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.PhotoResult;

/**
 * A <a href="https://developers.google.com/places/web-service/photos#place_photo_requests">Place Photo</a> request.
 */
public class PhotoRequest
    extends PendingResultBase<PhotoResult, PhotoRequest, PhotoRequest.Response> {

  static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/place/photo");


  public PhotoRequest(GeoApiContext context) {
    super(context, API_CONFIG, Response.class);
  }

  @Override
  protected void validateRequest() {
    if (!params().containsKey("photoreference")) {
      throw new IllegalArgumentException("Request must contain  'photoReference'.");
    }
    if (!params().containsKey("maxheight") && !params().containsKey("maxwidth")) {
      throw new IllegalArgumentException("Request must contain  'maxHeight' or 'maxWidth.");
    }
  }

  /**
   * Set the photoReference for this request.
   *
   * @param photoReference A string identifier that uniquely identifies a photo. Photo references are returned from
   *                       either a Place Search or Place Details request.
   * @return Returns the configured PhotoRequest.
   */
  public PhotoRequest photoReference(String photoReference) {
    return param("photoreference", photoReference);
  }

  /**
   * Set the maxHeight for this request.
   *
   * @param maxHeight Specifies the maximum desired height, in pixels, of the image returned by the Place Photos service.
   * @return Returns the configured PhotoRequest.
   */
  public PhotoRequest maxHeight(int maxHeight) {
    return param("maxheight", String.valueOf(maxHeight));
  }

  /**
   * Set the maxWidth for this request.
   *
   * @param maxWidth Specifies the maximum desired width, in pixels, of the image returned by the Place Photos service.
   * @return Returns the configured PhotoRequest.
   */
  public PhotoRequest maxWidth(int maxWidth) {
    return param("maxwidth", String.valueOf(maxWidth));
  }

  public static class Response implements ApiResponse<PhotoResult> {
    // This class is here to keep the type system happy.
    // It is not actually instantiated by OkHttpPendingResult.

    @Override
    public boolean successful() {
      return false;
    }

    @Override
    public PhotoResult getResult() {
      return null;
    }

    @Override
    public ApiException getError() {
      return null;
    }
  }
}
