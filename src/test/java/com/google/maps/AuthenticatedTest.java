package com.google.maps;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Base class for tests requiring automated authentication.
 *
 * <p>Sub-classes need to implement a 1-arg constructor that takes a GeoApiContext that will be
 * supplied with an appropriate API key or client ID and secret set. The {@code RunWith
 * (Paramaterized.class)} annotation will then ensure that each test that inherits will be run
 * for each context returned from {@link #contexts()}. That is,
 * if an API_KEY and CLIENT_ID+CLIENT_SECRET are found, each test will be run twice and supplied
 * a context with the appropriate authentication tokens set.
 */
@RunWith(Parameterized.class) @Ignore
public class AuthenticatedTest {
  @Parameters
  public static Collection<Object[]> contexts() {
    Collection<Object[]> contexts = new ArrayList<>();

   if (System.getenv("API_KEY") != null) {
      GeoApiContext context = new GeoApiContext()
          .setApiKey(System.getenv("API_KEY"));
      contexts.add(new Object[]{context});
    }

    if (!(System.getenv("CLIENT_ID") == null || System.getenv("CLIENT_SECRET") == null)) {
      GeoApiContext context = new GeoApiContext()
          .setEnterpriseCredentials(System.getenv("CLIENT_ID"), System.getenv("CLIENT_SECRET"));
      contexts.add(new Object[]{context});
    }

    if (contexts.size() == 0) {
      throw new IllegalArgumentException("No credentials found! Set the API_KEY or CLIENT_ID and "
          + "CLIENT_SECRET environment variables to run tests requiring authentication.");
    }

    return contexts;
  }

  protected AuthenticatedTest() {
  }
}
