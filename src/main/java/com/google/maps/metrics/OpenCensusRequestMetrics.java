package com.google.maps.metrics;

import io.opencensus.stats.StatsRecorder;
import io.opencensus.tags.TagContext;
import io.opencensus.tags.TagValue;
import io.opencensus.tags.Tagger;

/** An OpenCensus logger that generates success and latency metrics. */
final class OpenCensusRequestMetrics implements RequestMetrics {
  private final String requestName;
  private final Tagger tagger;
  private final StatsRecorder statsRecorder;

  private long requestStart;
  private long networkStart;
  private long networkTime;
  private boolean finished;

  OpenCensusRequestMetrics(String requestName, Tagger tagger, StatsRecorder statsRecorder) {
    this.requestName = requestName;
    this.tagger = tagger;
    this.statsRecorder = statsRecorder;
    this.requestStart = milliTime();
    this.networkStart = milliTime();
    this.networkTime = 0;
    this.finished = false;
  }

  @Override
  public void startNetwork() {
    this.networkStart = milliTime();
  }

  @Override
  public void endNetwork() {
    this.networkTime += milliTime() - this.networkStart;
  }

  @Override
  public void endRequest(Exception exception, int httpStatusCode, long retryCount) {
    // multiple endRequest are ignored
    if (this.finished) {
      return;
    }
    this.finished = true;
    long requestTime = milliTime() - this.requestStart;

    TagContext tagContext =
        tagger
            .currentBuilder()
            .putLocal(OpenCensusMetrics.Tags.REQUEST_NAME, TagValue.create(requestName))
            .putLocal(
                OpenCensusMetrics.Tags.HTTP_CODE, TagValue.create(Integer.toString(httpStatusCode)))
            .putLocal(OpenCensusMetrics.Tags.API_STATUS, TagValue.create(exceptionName(exception)))
            .build();
    statsRecorder
        .newMeasureMap()
        .put(OpenCensusMetrics.Measures.LATENCY, requestTime)
        .put(OpenCensusMetrics.Measures.NETWORK_LATENCY, this.networkTime)
        .put(OpenCensusMetrics.Measures.RETRY_COUNT, retryCount)
        .record(tagContext);
  }

  private String exceptionName(Exception exception) {
    if (exception == null) {
      return "";
    } else {
      return exception.getClass().getName();
    }
  }

  private long milliTime() {
    return System.currentTimeMillis();
  }
}
