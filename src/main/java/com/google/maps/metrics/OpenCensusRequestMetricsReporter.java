package com.google.maps.metrics;

import io.opencensus.stats.Stats;
import io.opencensus.stats.StatsRecorder;
import io.opencensus.tags.Tagger;
import io.opencensus.tags.Tags;

/** An OpenCensus logger that generates success and latency metrics. */
public final class OpenCensusRequestMetricsReporter implements RequestMetricsReporter {
  private static final Tagger tagger = Tags.getTagger();
  private static final StatsRecorder statsRecorder = Stats.getStatsRecorder();

  public OpenCensusRequestMetricsReporter() {}

  @Override
  public RequestMetrics newRequest(String requestName) {
    return new OpenCensusRequestMetrics(requestName, tagger, statsRecorder);
  }
}
