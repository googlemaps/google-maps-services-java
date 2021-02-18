/*
 * Copyright 2016 Google Inc. All rights reserved.
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

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.maps.GeolocationApi;
import com.google.maps.ImageResult;
import com.google.maps.PendingResult;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.UnknownErrorException;
import com.google.maps.metrics.RequestMetrics;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.Fare;
import com.google.maps.model.LatLng;
import com.google.maps.model.LocationType;
import com.google.maps.model.OpeningHours.Period.OpenClose.DayOfWeek;
import com.google.maps.model.PlaceDetails.Review.AspectRating.RatingType;
import com.google.maps.model.PriceLevel;
import com.google.maps.model.TravelMode;
import com.google.maps.model.VehicleType;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A PendingResult backed by a HTTP call executed by Google App Engine URL Fetch capability, a
 * deserialization step using Gson, and a retry policy.
 *
 * <p>{@code T} is the type of the result of this pending result, and {@code R} is the type of the
 * request.
 */
public class GaePendingResult<T, R extends ApiResponse<T>> implements PendingResult<T> {
  private final HTTPRequest request;
  private final URLFetchService client;
  private final Class<R> responseClass;
  private final FieldNamingPolicy fieldNamingPolicy;
  private final Integer maxRetries;
  private final ExceptionsAllowedToRetry exceptionsAllowedToRetry;
  private final RequestMetrics metrics;

  private long errorTimeOut;
  private int retryCounter = 0;
  private long cumulativeSleepTime = 0;
  private Future<HTTPResponse> call;

  private static final Logger LOG = LoggerFactory.getLogger(GaePendingResult.class.getName());
  private static final List<Integer> RETRY_ERROR_CODES = Arrays.asList(500, 503, 504);

  /**
   * @param request HTTP request to execute.
   * @param client The client used to execute the request.
   * @param responseClass Model class to unmarshal JSON body content.
   * @param fieldNamingPolicy FieldNamingPolicy for unmarshaling JSON.
   * @param errorTimeOut Number of milliseconds to re-send erroring requests.
   * @param maxRetries Number of times allowed to re-send erroring requests.
   */
  public GaePendingResult(
      HTTPRequest request,
      URLFetchService client,
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
    this.call = client.fetchAsync(request);
  }

  @Override
  public void setCallback(Callback<T> callback) {
    throw new RuntimeException("setCallback not implemented for Google App Engine");
  }

  @Override
  public T await() throws ApiException, IOException, InterruptedException {
    try {
      HTTPResponse result = call.get();
      metrics.endNetwork();
      return parseResponse(this, result);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      } else {
        // According to
        // https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/urlfetch/URLFetchService
        // all exceptions should be subclass of IOException so this should not happen.
        throw new UnknownErrorException("Unexpected exception from " + e.getMessage());
      }
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

  @SuppressWarnings("unchecked")
  private T parseResponse(GaePendingResult<T, R> request, HTTPResponse response)
      throws IOException, ApiException, InterruptedException {
    try {
      T result = parseResponseInternal(request, response);
      metrics.endRequest(null, response.getResponseCode(), retryCounter);
      return result;
    } catch (Exception e) {
      metrics.endRequest(e, response.getResponseCode(), retryCounter);
      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  private T parseResponseInternal(GaePendingResult<T, R> request, HTTPResponse response)
      throws IOException, ApiException, InterruptedException {
    if (shouldRetry(response)) {
      // Retry is a blocking method, but that's OK. If we're here, we're either in an await()
      // call, which is blocking anyway, or we're handling a callback in a separate thread.
      return request.retry();
    }

    byte[] bytes = response.getContent();
    R resp;

    String contentType = null;
    for (HTTPHeader header : response.getHeaders()) {
      if (header.getName().equalsIgnoreCase("Content-Type")) {
        contentType = header.getValue();
      }
    }

    if (contentType != null
        && contentType.startsWith("image")
        && responseClass == ImageResult.Response.class
        && response.getResponseCode() == 200) {
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
                AddressComponentType.class, new SafeEnumAdapter<>(AddressComponentType.UNKNOWN))
            .registerTypeAdapter(AddressType.class, new SafeEnumAdapter<>(AddressType.UNKNOWN))
            .registerTypeAdapter(TravelMode.class, new SafeEnumAdapter<>(TravelMode.UNKNOWN))
            .registerTypeAdapter(LocationType.class, new SafeEnumAdapter<>(LocationType.UNKNOWN))
            .registerTypeAdapter(RatingType.class, new SafeEnumAdapter<>(RatingType.UNKNOWN))
            .registerTypeAdapter(VehicleType.class, new SafeEnumAdapter<>(VehicleType.OTHER))
            .registerTypeAdapter(DayOfWeek.class, new DayOfWeekAdapter())
            .registerTypeAdapter(PriceLevel.class, new PriceLevelAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(GeolocationApi.Response.class, new GeolocationResponseAdapter())
            .registerTypeAdapter(EncodedPolyline.class, new EncodedPolylineInstanceCreator(""))
            .setFieldNamingPolicy(fieldNamingPolicy)
            .create();

    // Attempt to de-serialize before checking the HTTP status code, as there may be JSON in the
    // body that we can use to provide a more descriptive exception.
    try {
      resp = gson.fromJson(new String(bytes, "utf8"), responseClass);
    } catch (JsonSyntaxException e) {
      // Check HTTP status for a more suitable exception
      if (response.getResponseCode() > 399) {
        // Some of the APIs return 200 even when the API request fails, as long as the transport
        // mechanism succeeds. In these cases, INVALID_RESPONSE, etc are handled by the Gson
        // parsing.
        throw new IOException(
            String.format(
                "Server Error: %d %s",
                response.getResponseCode(),
                new String(response.getContent(), Charset.defaultCharset())));
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
        // Retry over_query_limit errors
        return request.retry();
      } else {
        // Throw anything else, including OQLs if we've spent too much time retrying
        throw e;
      }
    }
  }

  private T retry() throws IOException, ApiException, InterruptedException {
    retryCounter++;
    LOG.info("Retrying request. Retry #{}", retryCounter);
    metrics.startNetwork();
    this.call = client.fetchAsync(request);
    return this.await();
  }

  private boolean shouldRetry(HTTPResponse response) {
    return RETRY_ERROR_CODES.contains(response.getResponseCode())
        && cumulativeSleepTime < errorTimeOut
        && (maxRetries == null || retryCounter < maxRetries);
  }

  private boolean shouldRetry(ApiException exception) {
    return exceptionsAllowedToRetry.contains(exception.getClass())
        && cumulativeSleepTime < errorTimeOut
        && (maxRetries == null || retryCounter < maxRetries);
  }
}
