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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okio.ByteString;

/**
 * Utility class for supporting Maps for Work Digital signatures.
 *
 * <p>See <a
 * href="https://developers.google.com/maps/documentation/directions/get-api-key#client-id">Using a
 * client ID</a> for more detail on this protocol.
 */
public class UrlSigner {
  private static final String ALGORITHM_HMAC_SHA1 = "HmacSHA1";
  private final Mac mac;

  public UrlSigner(final String keyString) throws NoSuchAlgorithmException, InvalidKeyException {
    // Convert from URL-safe base64 to regular base64.
    String base64 = keyString.replace('-', '+').replace('_', '/');

    ByteString decodedKey = ByteString.decodeBase64(base64);
    if (decodedKey == null) {
      // NOTE: don't log the exception, in case some of the private key leaks to an end-user.
      throw new IllegalArgumentException("Private key is invalid.");
    }

    mac = Mac.getInstance(ALGORITHM_HMAC_SHA1);
    mac.init(new SecretKeySpec(decodedKey.toByteArray(), ALGORITHM_HMAC_SHA1));
  }

  /** Generate url safe HmacSHA1 of a path. */
  public String getSignature(String path) {
    byte[] digest = getMac().doFinal(path.getBytes(UTF_8));
    return ByteString.of(digest).base64().replace('+', '-').replace('/', '_');
  }

  private Mac getMac() {
    // Mac is not thread-safe. Requires a new clone for each signature.
    try {
      return (Mac) mac.clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException(e);
    }
  }
}
