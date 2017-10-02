/*
 * Copyright (C) 2009 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * Copyright 2017 Google Inc. All rights reserved.
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

package com.google.maps.internal.ratelimiter;

import java.util.Locale;

/**
 * Methods factored out so that they can be emulated differently in GWT.
 *
 * <p>This is a minimal port of Google Guava's com.google.common.base.Platform, sufficient to
 * implement the ratelimiter classes.
 *
 * @author Jesse Wilson
 */
final class Platform {
  private Platform() {}

  /** Calls {@link System#nanoTime()}. */
  static long systemNanoTime() {
    return System.nanoTime();
  }

  static String formatCompact4Digits(double value) {
    return String.format(Locale.ROOT, "%.4g", value);
  }
}
