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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.maps.GeolocationApi;
import com.google.maps.ImageResult;
import com.google.maps.PendingResult;
import com.google.maps.errors.ApiException;
import com.google.maps.metrics.RequestMetrics;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.Fare;
import com.google.maps.model.LatLng;
import com.google.maps.model.LocationType;
import com.google.maps.model.OpeningHours.Period.OpenClose.DayOfWeek;
import com.google.maps.model.PlaceDetails.Review.AspectRating.RatingType;
import com.google.maps.model.PriceLevel;
import com.google.maps.model.TravelMode;
import com.google.maps.model.VehicleType;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A PendingResult backed by a HTTP call executed by OkHttp, a deserialization step using Gson, rate
 * limiting and a retry policy.
 *
 * <p>{@code T} is the type of the result of this pending result, and {@code R} is the type of the
 * request.
 */
public class OkHttpPendingResult<T, R extends ApiResponse<T>>
    implements PendingResult<T>, Callback {
  private final Request request;
  private final OkHttpClient client;
  private final Class<R> responseClass;
  private final FieldNamingPolicy fieldNamingPolicy;
  private final Integer maxRetries;
  private final RequestMetrics metrics;

  private Call call;
  private Callback<T> callback;
  private long errorTimeOut;
  private int retryCounter = 0;
  private long cumulativeSleepTime = 0;
  private ExceptionsAllowedToRetry exceptionsAllowedToRetry;

  private static final Logger LOG = LoggerFactory.getLogger(OkHttpPendingResult.class.getName());
  private static final List<Integer> RETRY_ERROR_CODES = Arrays.asList(500, 503, 504);

  /**
   * @param request HTTP request to execute.
   * @param client The client used to execute the request.
   * @param responseClass Model class to unmarshal JSON body content.
   * @param fieldNamingPolicy FieldNamingPolicy for unmarshaling JSON.
   * @param errorTimeOut Number of milliseconds to re-send erroring requests.
   * @param maxRetries Number of times allowed to re-send erroring requests.
   * @param exceptionsAllowedToRetry The exceptions to retry.
   */
  public OkHttpPendingResult(
      Request request,
      OkHttpClient client,
      Class<R> responseClass,
      FieldNamingPolicy fieldNamingPolicy,
      long errorTimeOut,
      Integer maxRetries,
      ExceptionsAllowedToRetry exceptionsAllowedToRetry,
      RequestMetrics metrics) {
    this.request = request;
    this.client = client;
    this.responseClass = responseClass;
    this.fieldNamingPolicy = fieldNamingPolicy;
    this.errorTimeOut = errorTimeOut;
    this.maxRetries = maxRetries;
    this.exceptionsAllowedToRetry = exceptionsAllowedToRetry;
    this.metrics = metrics;

    metrics.startNetwork();
    this.call = client.newCall(request);
  }

  @Override
  public void setCallback(Callback<T> callback) {
    this.callback = callback;
    call.enqueue(this);
  }

  /** Preserve a request/response pair through an asynchronous callback. */
  private class QueuedResponse {
    private final OkHttpPendingResult<T, R> request;
    private final Response response;
    private final IOException e;

    public QueuedResponse(OkHttpPendingResult<T, R> request, Response response) {
      this.request = request;
      this.response = response;
      this.e = null;
    }

    public QueuedResponse(OkHttpPendingResult<T, R> request, IOException e) {
      this.request = request;
      this.response = null;
      this.e = e;
    }
  }

  @Override
  public T await() throws ApiException, IOException, InterruptedException {
    // Handle sleeping for retried requests
    if (retryCounter > 0) {
      // 0.5 * (1.5 ^ i) represents an increased sleep time of 1.5x per iteration,
      // starting at 0.5s when i = 0. The retryCounter will be 1 for the 1st retry,
      // so subtract 1 here.
      double delaySecs = 0.5 * Math.pow(1.5, retryCounter - 1);

      // Generate a jitter value between -delaySecs / 2 and +delaySecs / 2
      long delayMillis = (long) (delaySecs * (Math.random() + 0.5) * 1000);

      LOG.debug(
          String.format(
              "Sleeping between errors for %dms (retry #%d, already slept %dms)",
              delayMillis, retryCounter, cumulativeSleepTime));
      cumulativeSleepTime += delayMillis;
      try {
        Thread.sleep(delayMillis);
      } catch (InterruptedException e) {
        // No big deal if we don't sleep as long as intended.
      }
    }

    final BlockingQueue<QueuedResponse> waiter = new ArrayBlockingQueue<>(1);
    final OkHttpPendingResult<T, R> parent = this;

    // This callback will be called on another thread, handled by the RateLimitExecutorService.
    // Calling call.execute() directly would bypass the rate limiting.
    call.enqueue(
        new okhttp3.Callback() {
          @Override
          public void onFailure(Call call, IOException e) {
            metrics.endNetwork();
            waiter.add(new QueuedResponse(parent, e));
          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {
            metrics.endNetwork();
            waiter.add(new QueuedResponse(parent, response));
          }
        });

    QueuedResponse r = waiter.take();
    if (r.response != null) {
      return parseResponse(r.request, r.response);
    } else {
      metrics.endRequest(r.e, 0, retryCounter);
      throw r.e;
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
    call.cancel();
  }

  @Override
  public void onFailure(Call call, IOException ioe) {
    metrics.endNetwork();
    if (callback != null) {
      metrics.endRequest(ioe, 0, retryCounter);
      callback.onFailure(ioe);
    }
  }

  @Override
  public void onResponse(Call call, Response response) throws IOException {
    metrics.endNetwork();
    if (callback != null) {
      try {
        callback.onResult(parseResponse(this, response));
      } catch (Exception e) {
        callback.onFailure(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private T parseResponse(OkHttpPendingResult<T, R> request, Response response)
      throws ApiException, InterruptedException, IOException {
    try {
      T result = parseResponseInternal(request, response);
      metrics.endRequest(null, response.code(), retryCounter);
      return result;
    } catch (Exception e) {
      metrics.endRequest(e, response.code(), retryCounter);
      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  private T parseResponseInternal(OkHttpPendingResult<T, R> request, Response response)
      throws ApiException, InterruptedException, IOException {
    if (shouldRetry(response)) {
      // since we are retrying the request we must close the response
      response.close();

      // Retry is a blocking method, but that's OK. If we're here, we're either in an await()
      // call, which is blocking anyway, or we're handling a callback in a separate thread.
      return request.retry();
    }

    byte[] bytes;
    try (ResponseBody body = response.body()) {
      bytes = body.bytes();
    }
    R resp;
    String contentType = response.header("Content-Type");

    if (contentType != null
        && contentType.startsWith("image")
        && responseClass == ImageResult.Response.class
        && response.code() == 200) {
      ImageResult result = new ImageResult(contentType, bytes);
      return (T) result;
    }

    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .registerTypeAdapter(Distance.class, new DistanceAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Fare.class, new FareAdapter())
            .registerTypeAdapter(LatLng.class, new LatLngAdapter())
            .registerTypeAdapter(
                AddressComponentType.class,
                new SafeEnumAdapter<AddressComponentType>(AddressComponentType.UNKNOWN))
            .registerTypeAdapter(
                AddressType.class, new SafeEnumAdapter<AddressType>(AddressType.UNKNOWN))
            .registerTypeAdapter(
                TravelMode.class, new SafeEnumAdapter<TravelMode>(TravelMode.UNKNOWN))
            .registerTypeAdapter(
                LocationType.class, new SafeEnumAdapter<LocationType>(LocationType.UNKNOWN))
            .registerTypeAdapter(
                RatingType.class, new SafeEnumAdapter<RatingType>(RatingType.UNKNOWN))
            .registerTypeAdapter(
                VehicleType.class, new SafeEnumAdapter<VehicleType>(VehicleType.OTHER))
            .registerTypeAdapter(DayOfWeek.class, new DayOfWeekAdapter())
            .registerTypeAdapter(PriceLevel.class, new PriceLevelAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(GeolocationApi.Response.class, new GeolocationResponseAdapter())
            .setFieldNamingPolicy(fieldNamingPolicy)
            .create();

    // Attempt to de-serialize before checking the HTTP status code, as there may be JSON in the
    // body that we can use to provide a more descriptive exception.
    try {
      resp = gson.fromJson(new String(bytes, "utf8"), responseClass);
    } catch (JsonSyntaxException e) {
      // Check HTTP status for a more suitable exception
      if (!response.isSuccessful()) {
        // Some of the APIs return 200 even when the API request fails, as long as the transport
        // mechanism succeeds. In these cases, INVALID_RESPONSE, etc are handled by the Gson
        // parsing.
        throw new IOException(
            String.format("Server Error: %d %s", response.code(), response.message()));
      }

      // Otherwise just cough up the syntax exception.
      throw e;
    }

    if (resp.successful()) {
      // Return successful responses
      return resp.getResult();
    } else {
      ApiException e = resp.getError();
      if (shouldRetry(e)) {
        return request.retry();
      } else {
        throw e;
      }
    }
  }

  private T retry() throws ApiException, InterruptedException, IOException {
    retryCounter++;
    LOG.info("Retrying request. Retry #" + retryCounter);
    metrics.startNetwork();
    this.call = client.newCall(request);
    return this.await();
  }

  private boolean shouldRetry(Response response) {
    return RETRY_ERROR_CODES.contains(response.code())
        && cumulativeSleepTime < errorTimeOut
        && (maxRetries == null || retryCounter < maxRetries);
  }

  private boolean shouldRetry(ApiException exception) {
    return exceptionsAllowedToRetry.contains(exception.getClass())
        && cumulativeSleepTime < errorTimeOut
        && (maxRetries == null || retryCounter < maxRetries);
  }
}
