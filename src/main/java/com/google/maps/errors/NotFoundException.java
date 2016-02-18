package com.google.maps.errors;

/**
 * Indicates at least one of the locations specified in the request's origin, destination, or
 * waypoints could not be geocoded.
 */
public class NotFoundException extends ApiException {

  private static final long serialVersionUID = -5447625132975504651L;

  public NotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
