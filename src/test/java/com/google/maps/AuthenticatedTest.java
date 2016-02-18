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
 * (Paramaterized.class)} annotation will then ensure that each test that inherits will be run for
 * each context returned from {@link #contexts()}. That is, if an API_KEY and
 * CLIENT_ID+CLIENT_SECRET are found, each test will be run twice and supplied a context with the
 * appropriate authentication tokens set.
 */
@RunWith(Parameterized.class)
@Ignore
public class AuthenticatedTest {
  protected AuthenticatedTest() {
  }

  public static Collection<Object[]> contexts(boolean supportsClientId) {
    Collection<Object[]> contexts = new ArrayList<Object[]>();

    // Travis can't run authorized tests from pull requests.
    // http://docs.travis-ci.com/user/pull-requests/#Security-Restrictions-when-testing-Pull-Requests
    if (System.getenv("TRAVIS_PULL_REQUEST") != null
        && !"false".equals(System.getenv("TRAVIS_PULL_REQUEST"))) {
      return contexts;
    }

    String apiKey = System.getenv("API_KEY");
    if (apiKey == null) {
      apiKey = System.getProperty("api.key");
    }

    if (apiKey != null && !apiKey.equalsIgnoreCase("")) {
      GeoApiContext context = new GeoApiContext()
          .setApiKey(apiKey);
      contexts.add(new Object[]{context});
    }

    if (supportsClientId) {
      String clientId = System.getenv("CLIENT_ID");
      String clientSecret = System.getenv("CLIENT_SECRET");
      if (clientId == null && clientSecret == null) {
        clientId = System.getProperty("client.id");
        clientSecret = System.getProperty("client.secret");
      }

      if (!(clientId == null || clientId.equals("") || clientSecret == null || clientSecret.equals(""))) {
        GeoApiContext context = new GeoApiContext()
            .setEnterpriseCredentials(clientId, clientSecret);
        contexts.add(new Object[]{context});
      }
    }

    if (contexts.size() == 0) {
      throw new IllegalArgumentException("No credentials found! Set the API_KEY or CLIENT_ID and "
          + "CLIENT_SECRET environment variables to run tests requiring authentication.");
    }

    return contexts;
  }

  @Parameters
  public static Collection<Object[]> contexts() {
    return contexts(true);
  }
}
