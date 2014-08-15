package com.google.maps.internal;

import com.google.maps.errors.ApiException;

/**
 * All Geo API responses implement this Interface.
 */
public interface ApiResponse<T> {
  boolean successful();

  T getResult();

  ApiException getError();
}
