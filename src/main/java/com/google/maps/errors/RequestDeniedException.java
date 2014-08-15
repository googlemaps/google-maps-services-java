package com.google.maps.errors;

/**
 * Indicates that the API denied the request. Check the message for more detail.
 */
public class RequestDeniedException extends ApiException {

  public RequestDeniedException(String errorMessage) {
    super(errorMessage);
  }
}
