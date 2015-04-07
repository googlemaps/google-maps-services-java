/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.google.maps.internal;

import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.maps.PendingResult;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.OverQueryLimitException;
import com.google.maps.model.*;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * A PendingResult backed by a HTTP call executed by OkHttp, a deserialization step using Gson,
 * rate limiting and a retry policy.
 *
 * <p>{@code T} is the type of the result of this pending result, and {@code R} is the type of the
 * request.
 */
public class OkHttpPendingResult<T, R extends ApiResponse<T>>
    implements PendingResult<T>/*, Callback*/ {
  private final HTTPRequest request;
  private final URLFetchService client;
  private final Class<R> responseClass;
  private final FieldNamingPolicy fieldNamingPolicy;

//  private Call call;
  private Callback<T> callback;
  private long errorTimeOut;
  private int retryCounter = 0;
  private long cumulativeSleepTime = 0;
  private Future<HTTPResponse> call;

  private static Logger log = Logger.getLogger(OkHttpPendingResult.class.getName());
  private static final List<Integer> RETRY_ERROR_CODES =  Arrays.asList(500, 503, 504);

  /**
   * @param request           HTTP request to execute.
   * @param client            The client used to execute the request.
   * @param responseClass     Model class to unmarshal JSON body content.
   * @param fieldNamingPolicy FieldNamingPolicy for unmarshaling JSON.
   * @param errorTimeOut      Number of milliseconds to re-send erroring requests.
   */
  public OkHttpPendingResult(HTTPRequest request, URLFetchService client, Class<R> responseClass,
      FieldNamingPolicy fieldNamingPolicy, long errorTimeOut) {
    this.request = request;
    this.client = client;
    this.responseClass = responseClass;
    this.fieldNamingPolicy = fieldNamingPolicy;
    this.errorTimeOut = errorTimeOut;

    this.call = client.fetchAsync(request);
  }

  @Override
  public void setCallback(Callback<T> callback) {
    this.callback = callback;
//    call.enqueue(this);
  }

  /**
   * Preserve a request/response pair through an asynchronous callback.
   */
  private class QueuedResponse {
    private final OkHttpPendingResult<T, R> request;
    private final HTTPResponse response;
    private final Exception e;

    public QueuedResponse(OkHttpPendingResult<T, R> request, HTTPResponse response) {
      this.request = request;
      this.response = response;
      this.e = null;
    }
    public QueuedResponse(OkHttpPendingResult<T, R> request, Exception e) {
      this.request = request;
      this.response = null;
      this.e = e;
    }
  }

  @Override
  public T await() throws Exception {
//    // Handle sleeping for retried requests
//    if (retryCounter > 0) {
//      // 0.5 * (1.5 ^ i) represents an increased sleep time of 1.5x per iteration,
//      // starting at 0.5s when i = 0. The retryCounter will be 1 for the 1st retry,
//      // so subtract 1 here.
//      double delaySecs = 0.5 * Math.pow(1.5, retryCounter - 1);
//
//      // Generate a jitter value between -delaySecs / 2 and +delaySecs / 2
//      long delayMillis = (long) (delaySecs * (Math.random() + 0.5) * 1000);
//
//      log.config(String.format("Sleeping between errors for %dms (retry #%d, already slept %dms)",
//          delayMillis, retryCounter, cumulativeSleepTime));
//      cumulativeSleepTime += delayMillis;
//      try {
//        Thread.sleep(delayMillis);
//      } catch (InterruptedException e) {
//        // No big deal if we don't sleep as long as intended.
//      }
//    }
//
//    final BlockingQueue<QueuedResponse> waiter = new ArrayBlockingQueue<QueuedResponse>(1);
//    final OkHttpPendingResult<T, R> parent = this;
//
//    // This callback will be called on another thread, handled by the RateLimitExecutorService.
//    // Calling call.execute() directly would bypass the rate limiting.
////    call.enqueue(new com.squareup.okhttp.Callback() {
////      @Override
////      public void onFailure(HTTPRequest request, IOException e) {
////        waiter.add(new QueuedResponse(parent, e));
////      }
////
////      @Override
////      public void onResponse(HTTPResponse response) throws IOException {
////        waiter.add(new QueuedResponse(parent, response));
////      }
////    });
//
//    QueuedResponse r = waiter.take();
    HTTPResponse response = call.get();
    
    if( response != null) {
      return parseResponse( this, response );
    } else {
      throw new Exception(response.getResponseCode() + " " + new String(response.getContent()));
    }
  }

  @Override
  public T awaitIgnoreError() {
    try {
      return await();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public void cancel() {
    call.cancel(true);
  }

//  @Override
//  public void onFailure(HTTPRequest request, IOException ioe) {
//    if (callback != null) {
//      callback.onFailure(ioe);
//    }
//  }

//  @Override
//  public void onResponse(HTTPResponse response) throws IOException {
//    if (callback != null) {
//      try {
//        callback.onResult(parseResponse(this, response));
//      } catch (Exception e) {
//        callback.onFailure(e);
//      }
//    }
//  }

  private T parseResponse(OkHttpPendingResult<T, R> request, HTTPResponse response) throws Exception {
    if (RETRY_ERROR_CODES.contains(response.getResponseCode()) && cumulativeSleepTime < errorTimeOut) {
        // Retry is a blocking method, but that's OK. If we're here, we're either in an await()
        // call, which is blocking anyway, or we're handling a callback in a separate thread.
        return request.retry();
    }

    Gson gson = new GsonBuilder()
        .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
        .registerTypeAdapter(Distance.class, new DistanceAdapter())
        .registerTypeAdapter(Duration.class, new DurationAdapter())
        .registerTypeAdapter(Fare.class, new FareAdapter())
        .registerTypeAdapter(LatLng.class, new LatLngAdapter())
        .registerTypeAdapter(AddressComponentType.class,
              new SafeEnumAdapter<AddressComponentType>(AddressComponentType.UNKNOWN))
        .registerTypeAdapter(AddressType.class, new SafeEnumAdapter<AddressType>(AddressType.UNKNOWN))
        .registerTypeAdapter(TravelMode.class, new SafeEnumAdapter<TravelMode>(TravelMode.UNKNOWN))
        .registerTypeAdapter(LocationType.class, new SafeEnumAdapter<LocationType>(LocationType.UNKNOWN))
        .setFieldNamingPolicy(fieldNamingPolicy)
        .create();

    byte[] bytes = getBytes(response);
    R resp;

    // Attempt to de-serialize before checking the HTTP status code, as there may be JSON in the
    // body that we can use to provide a more descriptive exception.
    try {
      resp = gson.fromJson(new String(bytes, "utf8"), responseClass);
    } catch (JsonSyntaxException e) {
      // Check HTTP status for a more suitable exception
      if (!(response.getResponseCode() < 400)) {
        // Some of the APIs return 200 even when the API request fails, as long as the transport
        // mechanism succeeds. In these cases, INVALID_RESPONSE, etc are handled by the Gson
        // parsing.
        throw new IOException(String.format("Server Error: %d %s", response.getResponseCode(),
            new String(response.getContent())));
      }

      // Otherwise just cough up the syntax exception.
      throw e;
    }

    if (resp.successful()) {
      // Return successful responses
      return resp.getResult();
    } else {
      ApiException e = resp.getError();
      if (e instanceof OverQueryLimitException && cumulativeSleepTime < errorTimeOut) {
        // Retry over_query_limit errors
        return request.retry();
      } else {
        // Throw anything else, including OQLs if we've spent too much time retrying
        throw e;
      }
    }
  }

  private byte[] getBytes(HTTPResponse response) throws IOException {
    return response.getContent();
  }

  private T retry() throws Exception {
    retryCounter++;
    log.info("Retrying request. Retry #" + retryCounter);
    this.call = client.fetchAsync(request);
    return this.await();
  }
}
