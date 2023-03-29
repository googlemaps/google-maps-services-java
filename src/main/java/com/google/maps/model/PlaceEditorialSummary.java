/*
 * Copyright 2023 Google Inc. All rights reserved.
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

/**
 * Contains a summary of the place. A summary is comprised of a textual overview, and also includes
 * the language code for these if applicable. Summary text must be presented as-is and can not be
 * modified or altered.
 */
public class PlaceEditorialSummary implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The language of the previous fields. May not always be present. */
  public String language;

  /** A medium-length textual summary of the place. */
  public String overview;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[EditorialSummary: ");
    if (language != null) {
      sb.append("language=").append(language).append(", ");
    }
    if (overview != null) {
      sb.append("overview=").append(overview);
    }
    sb.append("]");
    return sb.toString();
  }
}
