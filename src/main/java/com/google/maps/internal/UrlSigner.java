package com.google.maps.internal;

import okio.ByteString;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class for supporting Maps for Business Digital signatures.
 *
 * <p>See {@url https://developers.google.com/maps/documentation/business/webservices/auth#digital_signatures}
 * for more detail on this protocol.
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
      throws NoSuchAlgorithmException, InvalidKeyException, IOException {
    // TODO(macd): add test
    Mac mac = Mac.getInstance("HmacSHA1");
    mac.init(key);
    byte[] digest = mac.doFinal(path.getBytes());
    return ByteString.of(digest).base64().replace('+', '-').replace('/', '_');
  }
}
