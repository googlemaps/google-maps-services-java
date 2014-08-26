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
import static org.junit.Assert.assertTrue;

import com.google.maps.MediumTests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.AbstractMap;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Category(MediumTests.class)
public class RateLimitExecutorServiceTest {

  @Test
  public void testRateLimitDoesNotExceedSuppliedQps() throws Exception {
    int qps = 10;
    RateLimitExecutorService service = new RateLimitExecutorService(qps, 50);
    final ConcurrentHashMap<Integer, Integer> executedTimestamps = new ConcurrentHashMap<>();

    for (int i = 0; i < 100; i++) {
      Runnable emptyTask = new Runnable() {
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
      assertTrue(executedTimestamps.get(timestamp) <= qps);
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
}