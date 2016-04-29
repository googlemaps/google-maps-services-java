package com.google.maps;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.GaePendingResult;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by brettmorgan on 4/29/16.
 */
public class GaeRequestHandler implements GeoApiContext.RequestHandler {
  private static final Logger LOG = Logger.getLogger(GaeRequestHandler.class.getName());
  private final URLFetchService client = URLFetchServiceFactory.getURLFetchService();

  @Override
  public <T, R extends ApiResponse<T>> PendingResult<T> handle(String hostName, String url, String userAgent, Class<R> clazz, FieldNamingPolicy fieldNamingPolicy, long errorTimeout) {
    FetchOptions fetchOptions = FetchOptions.Builder.withDeadline(10);
    HTTPRequest req = null;
    try {
      req = new HTTPRequest(new URL(hostName + url), HTTPMethod.POST, fetchOptions);
    } catch (MalformedURLException e) {
      LOG.log(Level.SEVERE, "Requesting '"+hostName + url+"'", e);
    }

    return new GaePendingResult<T, R>(req, client, clazz, fieldNamingPolicy, errorTimeout);
  }

  @Override
  public void setConnectTimeout(long timeout, TimeUnit unit) {
    throw new RuntimeException("setConnectTimeout not implemented for Google App Engine");
  }

  @Override
  public void setReadTimeout(long timeout, TimeUnit unit) {
    throw new RuntimeException("setReadTimeout not implemented for Google App Engine");
  }

  @Override
  public void setWriteTimeout(long timeout, TimeUnit unit) {
    throw new RuntimeException("setWriteTimeout not implemented for Google App Engine");
  }

  @Override
  public void setQueriesPerSecond(int maxQps) {
    throw new RuntimeException("setQueriesPerSecond not implemented for Google App Engine");
  }

  @Override
  public void setQueriesPerSecond(int maxQps, int minimumInterval) {
    throw new RuntimeException("setQueriesPerSecond not implemented for Google App Engine");
  }

  @Override
  public void setProxy(Proxy proxy) {
    throw new RuntimeException("setProxy not implemented for Google App Engine");
  }
}
