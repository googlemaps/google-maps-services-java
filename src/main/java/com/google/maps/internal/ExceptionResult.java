package com.google.maps.internal;

import com.google.maps.PendingResult;

/**
 * This class centralizes failure handling, independent of the calling style.
 */
public class ExceptionResult<T> implements PendingResult<T> {
  private final Exception exception;

  public ExceptionResult(Exception exception) {
    this.exception = exception;
  }

  @Override
  public void setCallback(Callback<T> callback) {
    callback.onFailure(exception);
  }

  @Override
  public T await() throws Exception {
    throw exception;
  }

  @Override
  public T awaitIgnoreError() {
    return null;
  }

  @Override
  public void cancel() {
  }
}
