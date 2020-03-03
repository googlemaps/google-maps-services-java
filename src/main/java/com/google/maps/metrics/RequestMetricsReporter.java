package com.google.maps.metrics;

/** A type to report common metrics shared among all request types. */
public interface RequestMetricsReporter {

  RequestMetrics newRequest(String requestName);
}
