/*
 * Copyright 2015 Google Inc. All rights reserved.
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

import java.util.Collection;

/**
 * A specific authenticated test that will never attempt to use client ID and secret credentials to
 * run.
 */
@RunWith(Parameterized.class)
@Ignore
public class KeyOnlyAuthenticatedTest extends AuthenticatedTest {
  protected KeyOnlyAuthenticatedTest() {
  }

  @Parameters
  public static Collection<Object[]> contexts() {
    return contexts(false);
  }
}
