package com.google.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.maps.internal.ApiConfig;
import com.google.maps.metrics.OpenCensusMetrics;
import com.google.maps.metrics.OpenCensusRequestMetricsReporter;
import com.google.maps.model.GeocodingResult;
import io.opencensus.stats.AggregationData;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewData;
import io.opencensus.tags.TagValue;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(MediumTests.class)
public class OpenCensusTest {

  private MockWebServer server;
  private GeoApiContext context;

  @Before
  public void Setup() {
    server = new MockWebServer();
    context =
        new GeoApiContext.Builder()
            .apiKey("AIza...")
            .requestMetricsReporter(new OpenCensusRequestMetricsReporter())
            .baseUrlOverride("http://127.0.0.1:" + server.getPort())
            .build();
    OpenCensusMetrics.registerAllViews();
  }

  @After
  @SuppressWarnings("CatchAndPrintStackTrace")
  public void Teardown() {
    try {
      server.shutdown();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private MockResponse mockResponse(int code, String status, int delayMs) {
    MockResponse response = new MockResponse();
    response.setResponseCode(code);
    if (status != null) {
      response.setBody("{\"results\" : [{}], \"status\" : \"" + status + "\" }");
    }
    response.setBodyDelay(delayMs, TimeUnit.MILLISECONDS);
    return response;
  }

  private void sleep(int milliseconds) {
    try {
      TimeUnit.MILLISECONDS.sleep(milliseconds);
    } catch (Exception e) {
    }
  }

  private Map.Entry<List<TagValue>, AggregationData> getMetric(String name) {
    sleep(10);
    ViewData viewData = Stats.getViewManager().getView(View.Name.create(name));
    Map<List<TagValue>, AggregationData> values = viewData.getAggregationMap();
    assertEquals(1, values.size());
    for (Map.Entry<List<TagValue>, AggregationData> entry : values.entrySet()) {
      return entry;
    }
    return null;
  }

  @Test
  public void testSuccess() throws Exception {
    server.enqueue(mockResponse(500, "OK", 100)); // retry 1
    server.enqueue(mockResponse(500, "OK", 100)); // retry 2
    server.enqueue(mockResponse(200, "OK", 300)); // succeed

    GeocodingResult[] result =
        context.get(new ApiConfig("/path"), GeocodingApi.Response.class, "k", "v").await();
    assertEquals(1, result.length);

    List<TagValue> tags =
        Arrays.asList(TagValue.create(""), TagValue.create("200"), TagValue.create("/path"));

    Map.Entry<List<TagValue>, AggregationData> latencyMetric =
        getMetric("maps.googleapis.com/client/request_latency");
    assertNotNull(latencyMetric);
    assertEquals(tags, latencyMetric.getKey());
    AggregationData.DistributionData latencyDist =
        (AggregationData.DistributionData) latencyMetric.getValue();
    assertEquals(1, latencyDist.getCount());
    assertTrue(latencyDist.getMean() > 500);

    Map.Entry<List<TagValue>, AggregationData> retryMetric =
        getMetric("maps.googleapis.com/client/retry_count");
    assertNotNull(retryMetric);
    assertEquals(tags, retryMetric.getKey());
    AggregationData.DistributionData retryDist =
        (AggregationData.DistributionData) retryMetric.getValue();
    assertEquals(1, retryDist.getCount());
    assertEquals(2.0, retryDist.getMean(), 0.1);

    Map.Entry<List<TagValue>, AggregationData> countMetric =
        getMetric("maps.googleapis.com/client/request_count");
    assertNotNull(countMetric);
    assertEquals(tags, countMetric.getKey());
    AggregationData.CountData count = (AggregationData.CountData) countMetric.getValue();
    assertEquals(1, count.getCount());
  }
}
