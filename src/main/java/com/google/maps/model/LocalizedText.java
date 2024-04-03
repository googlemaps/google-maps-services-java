/*
 * Copyright 2024 Google Inc. All rights reserved.
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

package com.google.maps.model;

import java.io.Serializable;

/** Localized variant of a text in a particular language. */
public class LocalizedText implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Localized string in the language corresponding to language_code below. */
  public String text;

  /**
   * The text's BCP-47 language code, such as "en-US" or "sr-Latn". For more information, see
   * http://www.unicode.org/reports/tr35/#Unicode_locale_identifier.
   */
  public String languageCode;

  @Override
  public String toString() {
    return String.format("(text=%d, languageCode=%d)", text, languageCode);
  }
}
