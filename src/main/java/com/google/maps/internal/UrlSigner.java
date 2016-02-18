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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okio.ByteString;

/**
 * Utility class for supporting Maps for Work Digital signatures.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/directions/get-api-key#client-id">Using
 * a client ID</a> for more detail on this protocol.
 */
public class UrlSigner {
  private final SecretKeySpec key;

  public UrlSigner(final String keyString) {
    // Convert from URL-safe base64 to regular base64.
    String base64 = keyString.replace('-', '+').replace('_', '/');

    ByteString decodedKey = ByteString.decodeBase64(base64);
    if (decodedKey == null) {
      // NOTE: don't log the exception, in case some of the private key leaks to an end-user.
      throw new IllegalArgumentException("Private key is invalid.");
    }
    this.key = new SecretKeySpec(decodedKey.toByteArray(), "HmacSHA1");
  }

  /**
   * Generate url safe HmacSHA1 of a path.
   */
  public String getSignature(String path)
      throws NoSuchAlgorithmException, InvalidKeyException {
    // TODO(macd): add test
    Mac mac = Mac.getInstance("HmacSHA1");
    mac.init(key);
    byte[] digest = mac.doFinal(path.getBytes());
    return ByteString.of(digest).base64().replace('+', '-').replace('/', '_');
  }
}
