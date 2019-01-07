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

package com.google.maps;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TestUtils {
  public static String retrieveBody(String filename) {
    InputStream input = TestUtils.class.getResourceAsStream(filename);
    try (Scanner s = new java.util.Scanner(input, StandardCharsets.UTF_8.name())) {
      s.useDelimiter("\\A");
      String body = s.next();

      if (body == null || body.length() == 0) {
        throw new IllegalArgumentException(
            "filename '" + filename + "' resulted in null or empty body");
      }
      return body;
    }
  }

  public static Thread findLastThreadByName(String name) {
    ThreadGroup currentThreadGroup = Thread.currentThread().getThreadGroup();
    Thread[] threads = new Thread[1000];
    currentThreadGroup.enumerate(threads);
    Thread delayThread = null;
    for (Thread thread : threads) {
      if (thread == null) break;
      if (thread.getName().equals(name)) {
        delayThread = thread;
      }
    }
    return delayThread;
  }
}
