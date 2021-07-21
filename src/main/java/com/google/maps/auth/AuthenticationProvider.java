package com.google.maps.auth;

/**
 * Interface definition for an authentication provider for requests made to Google Maps Platform
 * APIs.
 */
public interface AuthenticationProvider {

  /**
   * Provides authentication to <code>request</code>.
   *
   * @param request the unauthenticated request to provide authentication to
   */
  void provideAuth(HeaderAuthenticatedRequest request);
}
