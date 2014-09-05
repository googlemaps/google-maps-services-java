package com.google.maps.errors;

/**
 * Indicates at least one of the locations specified in the request's origin, destination,
 * or waypoints could not be geocoded.
 */
public class NotFoundException extends ApiException {

  public NotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
