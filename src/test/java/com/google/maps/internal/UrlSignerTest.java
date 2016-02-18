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

import com.google.maps.SmallTests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import okio.ByteString;

/**
 * Test case for {@link UrlSigner}.
 */
@Category(SmallTests.class)
public class UrlSignerTest {

  // From http://en.wikipedia.org/wiki/Hash-based_message_authentication_code
  // HMAC_SHA1("key", "The quick brown fox jumps over the lazy dog")
  //     = 0xde7c9b85b8b78aa6bc8a7a36f70a90701c9db4d9
  private static final String MESSAGE = "The quick brown fox jumps over the lazy dog";
  private static final String SIGNING_KEY = ByteString.of("key".getBytes())
      .base64().replace('+', '-').replace('/', '_');
  private static final String SIGNATURE = ByteString.of(
      hexStringToByteArray("de7c9b85b8b78aa6bc8a7a36f70a90701c9db4d9")).base64()
      .replace('+', '-').replace('/', '_');

  @Test
  public void testUrlSigner() throws Exception {
    UrlSigner urlSigner = new UrlSigner(SIGNING_KEY);
    assertEquals(SIGNATURE, urlSigner.getSignature(MESSAGE));
  }

  // Helper code from http://stackoverflow.com/questions/140131/
  private static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
          + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }

}
