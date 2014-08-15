package com.google.maps.errors;

/**
 * Indicates that the API received a malformed request.
 */
public class InvalidRequestException extends ApiException {

  public InvalidRequestException(String errorMessage) {
    super(errorMessage);
  }
}
