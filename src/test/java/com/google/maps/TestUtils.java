package com.google.maps;

import java.io.InputStream;
import java.util.Scanner;

public class TestUtils {
  public static String retrieveBody(String filename) {
    InputStream input = TestUtils.class.getResourceAsStream(filename);
    Scanner s = new java.util.Scanner(input).useDelimiter("\\A");
    String body = s.next();
    if (body == null || body.length() == 0) {
      throw new IllegalArgumentException("filename '" + filename + "' resulted in null or empty body");
    }
    return body;
  }
}
