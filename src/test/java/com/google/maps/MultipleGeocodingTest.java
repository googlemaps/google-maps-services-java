package com.google.maps;

import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LocationType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by iblesa on 21/06/2016.
 */
public class MultipleGeocodingTest extends AuthenticatedTest {

    private static final double EPSILON = 0.000001;
    private GeoApiContext context;

    public MultipleGeocodingTest(GeoApiContext context) {
        this.context = context
                .setQueryRateLimit(3)
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    @Test
    public void testMultipleAsync() throws Exception {
        final List<GeocodingResult[]> resps = new ArrayList<GeocodingResult[]>();
        final List<Throwable> errors = new ArrayList<Throwable>();
        int limit = 2500;
        int batchSize = 100;

        batchSize = Math.min(batchSize, limit);

        int maxBatches = limit / batchSize;
        for (int batch = 0; batch < maxBatches; batch++) {
//            System.out.println("Batch " + batch);
            int start = batch * batchSize;
            int end = (batch + 1) * batchSize;
//            System.out.println(" Lets start at " + start + " and end at " + end);
            for (int i = start; i < end; i++) {
                PendingResult.Callback<GeocodingResult[]> callback =
                        new PendingResult.Callback<GeocodingResult[]>() {
                            @Override
                            public void onResult(GeocodingResult[] result) {
                                resps.add(result);
                            }

                            @Override
                            public void onFailure(Throwable e) {
                                errors.add(e);
                                fail("Got error when expected success.");
                            }
                        };
                GeocodingApi.newRequest(context).address("Sydney").setCallback(callback);
            }
            if (errors.isEmpty()) {
                Thread.sleep(10000);
            } else {
                break;
            }
        }

        assertFalse(resps.isEmpty());
        assertTrue(errors.isEmpty());
        assertNotNull(resps.get(0));
        Set<GeocodingResult[]> all = new HashSet<GeocodingResult[]>(resps);
        for (GeocodingResult[] result : all) {
            checkSydneyResult(result);

        }
    }

    private void checkSydneyResult(GeocodingResult[] results) {
        assertNotNull(results);
        assertNotNull(results[0].geometry);
        assertNotNull(results[0].geometry.location);
        assertEquals(-33.8674869, results[0].geometry.location.lat, EPSILON);
        assertEquals(151.2069902, results[0].geometry.location.lng, EPSILON);
        assertEquals("ChIJP3Sa8ziYEmsRUKgyFmh9AQM", results[0].placeId);
        assertEquals(LocationType.APPROXIMATE, results[0].geometry.locationType);
    }

}
