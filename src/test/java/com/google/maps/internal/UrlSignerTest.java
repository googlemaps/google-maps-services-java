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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.maps.SmallTests;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okio.ByteString;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/** Test case for {@link UrlSigner}. */
@Category(SmallTests.class)
public class UrlSignerTest {

  // From http://en.wikipedia.org/wiki/Hash-based_message_authentication_code
  // HMAC_SHA1("key", "The quick brown fox jumps over the lazy dog")
  //     = 0xde7c9b85b8b78aa6bc8a7a36f70a90701c9db4d9
  private static final String MESSAGE = "The quick brown fox jumps over the lazy dog";
  private static final String SIGNING_KEY =
      ByteString.of("key".getBytes(UTF_8)).base64().replace('+', '-').replace('/', '_');
  private static final String SIGNATURE =
      ByteString.of(hexStringToByteArray("de7c9b85b8b78aa6bc8a7a36f70a90701c9db4d9"))
          .base64()
          .replace('+', '-')
          .replace('/', '_');

  @Test
  public void testUrlSigner() throws Exception {
    UrlSigner urlSigner = new UrlSigner(SIGNING_KEY);
    assertEquals(SIGNATURE, urlSigner.getSignature(MESSAGE));
  }

  @Test
  public void testMustSupportParallelSignatures() throws Exception {
    int attempts = 100;
    ExecutorService executor = Executors.newFixedThreadPool(attempts);

    final UrlSigner urlSigner = new UrlSigner(SIGNING_KEY);
    final List<Boolean> fails = Collections.synchronizedList(new ArrayList<Boolean>());

    for (int i = 0; i < attempts; i++) {
      executor.execute(
          new Runnable() {
            @Override
            public void run() {
              try {
                if (!SIGNATURE.equals(urlSigner.getSignature(MESSAGE))) {
                  fails.add(true);
                }
              } catch (Exception e) {
                fails.add(true);
              }
            }
          });
    }

    executor.shutdown();
    executor.awaitTermination(20, TimeUnit.SECONDS);

    assertTrue(fails.isEmpty());
  }

  // Helper code from http://stackoverflow.com/questions/140131/
  private static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] =
          (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }
}
