package com.google.maps.errors;

/**
 * Indicates that the server encountered an unknown error. In some cases these are safe to retry.
 */
public class UnknownErrorException extends ApiException {

  public UnknownErrorException(String errorMessage) {
    super(errorMessage);
  }
}
