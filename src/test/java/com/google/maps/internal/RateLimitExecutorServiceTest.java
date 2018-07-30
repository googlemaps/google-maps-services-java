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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.maps.MediumTests;
import java.util.AbstractMap;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Category(MediumTests.class)
public class RateLimitExecutorServiceTest {

  private static final Logger LOG =
      LoggerFactory.getLogger(RateLimitExecutorServiceTest.class.getName());

  @Test
  public void testRateLimitDoesNotExceedSuppliedQps() throws Exception {
    int qps = 10;
    RateLimitExecutorService service = new RateLimitExecutorService();
    service.setQueriesPerSecond(qps);
    final ConcurrentHashMap<Integer, Integer> executedTimestamps = new ConcurrentHashMap<>();

    for (int i = 0; i < 100; i++) {
      Runnable emptyTask =
          new Runnable() {
            @Override
            public void run() {
              int nearestSecond = (int) (new Date().getTime() / 1000);
              if (executedTimestamps.containsKey(nearestSecond)) {
                executedTimestamps.put(nearestSecond, executedTimestamps.get(nearestSecond) + 1);
              } else {
                executedTimestamps.put(nearestSecond, 1);
              }
            }
          };
      service.execute(emptyTask);
    }

    // Sleep until finished, or 20s expires (to prevent waiting forever)
    long sleepTime = 0;
    while (sleepTime < 20000 && countTotalRequests(executedTimestamps) < 100) {
      long sleepFor = TimeUnit.SECONDS.toMillis(1);
      sleepTime += sleepFor;
      Thread.sleep(sleepFor);
    }

    // Check that we executed at the correct rate
    for (Integer timestamp : executedTimestamps.keySet()) {
      Integer actualQps = executedTimestamps.get(timestamp);
      // Logging QPS here to detect if a previous iteration had qps-1 and this is qps+1.
      LOG.info(
          String.format(
              "Timestamp(%d) logged %d queries (target of %d qps)", timestamp, actualQps, qps));
      assertTrue(
          String.format("Expected <= %d queries in a second, got %d.", qps, actualQps),
          actualQps <= qps);
    }
    // Check that we executed every request
    assertEquals(100, countTotalRequests(executedTimestamps));

    service.shutdown();
  }

  private static int countTotalRequests(AbstractMap<?, Integer> hashMap) {
    int counter = 0;
    for (Integer value : hashMap.values()) {
      counter += value;
    }
    return counter;
  }

  @Test
  public void testDelayThreadIsStoppedAfterShutdownIsCalled() throws InterruptedException {
    RateLimitExecutorService service = new RateLimitExecutorService();
    final Thread delayThread = service.delayThread;
    assertNotNull(
        "Delay thread should be created in constructor of RateLimitExecutorService", delayThread);
    assertTrue(
        "Delay thread should start in constructor of RateLimitExecutorService",
        delayThread.isAlive());
    // this is needed to make sure that delay thread has reached queue.take()
    delayThread.join(10);
    service.shutdown();
    delayThread.join(10);
    assertFalse(delayThread.isAlive());
  }
}
