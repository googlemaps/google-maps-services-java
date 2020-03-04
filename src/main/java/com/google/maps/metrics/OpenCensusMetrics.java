package com.google.maps.metrics;

import io.opencensus.stats.Aggregation;
import io.opencensus.stats.Aggregation.Count;
import io.opencensus.stats.Aggregation.Distribution;
import io.opencensus.stats.BucketBoundaries;
import io.opencensus.stats.Measure.MeasureLong;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewManager;
import io.opencensus.tags.TagKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * OpenCensus metrics which are measured for every request.
 */
public final class OpenCensusMetrics {
  private OpenCensusMetrics() {}

  public static final class Tags {
    private Tags() {}

    public static final TagKey REQUEST_NAME = TagKey.create("request_name");
    public static final TagKey HTTP_CODE = TagKey.create("http_code");
    public static final TagKey API_STATUS = TagKey.create("api_status");
  }

  public static final class Measures {
    private Measures() {}

    public static final MeasureLong LATENCY =
        MeasureLong.create(
            "maps.googleapis.com/measure/client/latency",
            "Total time between library method called and results returned",
            "ms");

    public static final MeasureLong NETWORK_LATENCY =
        MeasureLong.create(
            "maps.googleapis.com/measure/client/network_latency",
            "Network time inside the library",
            "ms");

    public static final MeasureLong RETRY_COUNT =
        MeasureLong.create(
            "maps.googleapis.com/measure/client/retry_count",
            "How many times any request was retried",
            "1");
  }

  private static final class Aggregations {
    private Aggregations() {}

    private static final Aggregation COUNT = Count.create();

    private static final Aggregation DISTRIBUTION_INTEGERS_10 =
        Distribution.create(
            BucketBoundaries.create(
                Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0)));

    // every bucket is ~25% bigger = 20 * 2^(N/3)
    private static final Aggregation DISTRIBUTION_LATENCY =
        Distribution.create(
            BucketBoundaries.create(
                Arrays.asList(
                    0.0, 20.0, 25.2, 31.7, 40.0, 50.4, 63.5, 80.0, 100.8, 127.0, 160.0, 201.6,
                    254.0, 320.0, 403.2, 508.0, 640.0, 806.3, 1015.9, 1280.0, 1612.7, 2031.9,
                    2560.0, 3225.4, 4063.7)));
  }

  public static final class Views {
    private Views() {}

    private static final List<TagKey> fields =
        tags(Tags.REQUEST_NAME, Tags.HTTP_CODE, Tags.API_STATUS);

    public static final View REQUEST_COUNT =
        View.create(
            View.Name.create("maps.googleapis.com/client/request_count"),
            "Request counts",
            Measures.LATENCY,
            Aggregations.COUNT,
            fields);

    public static final View REQUEST_LATENCY =
        View.create(
            View.Name.create("maps.googleapis.com/client/request_latency"),
            "Latency in msecs",
            Measures.LATENCY,
            Aggregations.DISTRIBUTION_LATENCY,
            fields);

    public static final View NETWORK_LATENCY =
        View.create(
            View.Name.create("maps.googleapis.com/client/network_latency"),
            "Network latency in msecs (internal)",
            Measures.NETWORK_LATENCY,
            Aggregations.DISTRIBUTION_LATENCY,
            fields);

    public static final View RETRY_COUNT =
        View.create(
            View.Name.create("maps.googleapis.com/client/retry_count"),
            "Retries per request",
            Measures.RETRY_COUNT,
            Aggregations.DISTRIBUTION_INTEGERS_10,
            fields);
  }

  public static void registerAllViews() {
    registerAllViews(Stats.getViewManager());
  }

  public static void registerAllViews(ViewManager viewManager) {
    View[] views_to_register =
        new View[] {
          Views.REQUEST_COUNT, Views.REQUEST_LATENCY, Views.NETWORK_LATENCY, Views.RETRY_COUNT
        };
    for (View view : views_to_register) {
      viewManager.registerView(view);
    }
  }

  private static List<TagKey> tags(TagKey... items) {
    return Collections.unmodifiableList(Arrays.asList(items));
  }
}
