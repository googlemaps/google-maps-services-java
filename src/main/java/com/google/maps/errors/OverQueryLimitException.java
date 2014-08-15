package com.google.maps.errors;

/**
 * Indicates that the requesting account has exceeded quota.
 */
public class OverQueryLimitException extends ApiException {

  public OverQueryLimitException(String errorMessage) {
    super(errorMessage);
  }
}
