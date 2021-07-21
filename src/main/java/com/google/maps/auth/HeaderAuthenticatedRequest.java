package com.google.maps.auth;

/** Interface definition for a request that is authenticated via HTTP headers. */
public interface HeaderAuthenticatedRequest {

  /**
   * Adds a header to this request.
   *
   * @param name the HTTP header name
   * @param value the HTTP header value
   */
  void addHeader(String name, String value);
}
