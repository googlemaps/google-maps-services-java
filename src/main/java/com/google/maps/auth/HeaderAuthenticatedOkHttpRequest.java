package com.google.maps.auth;

import okhttp3.Request;

public class HeaderAuthenticatedOkHttpRequest implements HeaderAuthenticatedRequest {

  private final Request.Builder authenticatedRequestBuilder;

  public HeaderAuthenticatedOkHttpRequest(Request request) {
    authenticatedRequestBuilder = request.newBuilder();
  }

  @Override
  public void addHeader(String name, String value) {
    authenticatedRequestBuilder.addHeader(name, value);
  }

  public Request authenticate() {
    return authenticatedRequestBuilder.build();
  }
}
