package com.google.maps.internal;

import static org.junit.Assert.assertEquals;

import okio.ByteString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test case for {@link UrlSigner}.
 */
@RunWith(JUnit4.class)
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
  private  static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
            + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }

}
