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
import java.util.Arrays;

/**
 * Represents a descriptor of an address.
 *
 * <p>Please see <a href="https://mapsplatform.google.com/demos/address-descriptors/">Address
 * Descriptors</a> for more detail.
 */
public class AddressDescriptor implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Points of interest that provide a reference point for a location. */
  public static class Landmark implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The Place ID of the underlying establishment serving as the landmark. Can be used to resolve
     * more information about the landmark through Place Details or Place Id Lookup.
     */
    public String placeId;

    /** The best name for the landmark. */
    public LocalizedText displayName;

    /**
     * One or more values indicating the type of the returned result. Please see <a
     * href="https://developers.google.com/maps/documentation/places/web-service/supported_types">Types
     * </a> for more detail.
     */
    public String types[];

    /** An enum representing the relationship in space between the landmark and the target. */
    public enum SpatialRelationship {
      /** This is the default relationship when nothing more specific below applies. */
      NEAR("near"),
      /** The landmark has a spatial geometry and the target is within its bounds. */
      WITHIN("within"),
      /** The target is directly adjacent to the landmark or landmark's access point. */
      BESIDE("beside"),
      /** The target is directly opposite the landmark on the other side of the road. */
      ACROSS_THE_ROAD("across_the_road"),
      /** On the same route as the landmark but not besides or across. */
      DOWN_THE_ROAD("down_the_road"),
      /** Not on the same route as the landmark but a single 'turn' away. */
      AROUND_THE_CORNER("around_the_corner"),
      /** Close to the landmark's structure but further away from its access point. */
      BEHIND("behind");

      private final String spatialRelationship;

      SpatialRelationship(final String spatialRelationship) {
        this.spatialRelationship = spatialRelationship;
      }

      @Override
      public String toString() {
        return spatialRelationship;
      }

      public String toCanonicalLiteral() {
        return toString();
      }
    }

    /** Defines the spatial relationship between the target location and the landmark. */
    public SpatialRelationship spatialRelationship;

    /**
     * The straight-line distance between the target location and one of the landmark's access
     * points.
     */
    public float straightLineDistanceMeters;

    /**
     * The travel distance along the road network between the target location's closest point on a
     * road, and the landmark's closest access point on a road. This can be unpopulated if the
     * landmark is disconnected from the road network the target is closest to OR if the target
     * location was not actually considered to be on the road network.
     */
    public float travelDistanceMeters;

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("[Landmark: ");
      if (placeId != null) {
        sb.append("placeId=").append(placeId);
      }
      if (displayName != null) {
        sb.append(", displayName=").append(displayName);
      }
      if (types != null && types.length > 0) {
        sb.append(", types=").append(Arrays.toString(types));
      }
      if (spatialRelationship != null) {
        sb.append(", spatialRelationship=").append(spatialRelationship);
      }
      sb.append(", straightLineDistanceMeters=").append(straightLineDistanceMeters);
      sb.append(", travelDistanceMeters=").append(travelDistanceMeters);
      sb.append("]");
      return sb.toString();
    }
  }

  /**
   * A ranked list of nearby landmarks. The most useful (recognizable and nearby) landmarks are
   * ranked first.
   */
  public Landmark landmarks[];

  /** Precise regions that are useful at describing a location. */
  public static class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The Place ID of the underlying area feature. Can be used to resolve more information about
     * the area through Place Details or Place Id Lookup.
     */
    public String placeId;

    /** The best name for the area. */
    public LocalizedText displayName;

    /** An enum representing the relationship in space between the area and the target. */
    public enum Containment {
      /** Indicates an unknown containment returned by the server. */
      CONTAINMENT_UNSPECIFIED("containment_unspecified"),

      /** The target location is within the area region, close to the center. */
      WITHIN("within"),

      /** The target location is within the area region, close to the edge. */
      OUTSKIRTS("outskirts"),

      /** The target location is outside the area region, but close by. */
      NEAR("near");

      private final String containment;

      Containment(final String containment) {
        this.containment = containment;
      }

      @Override
      public String toString() {
        return containment;
      }

      public String toCanonicalLiteral() {
        return toString();
      }
    }

    /** Defines the spatial relationship between the target location and the political region. */
    public Containment containment;

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("[Area: ");
      if (placeId != null) {
        sb.append("placeId=").append(placeId);
      }
      if (displayName != null) {
        sb.append(", displayName=").append(displayName);
      }
      if (containment != null) {
        sb.append(", containment=").append(containment);
      }
      sb.append("]");
      return sb.toString();
    }
  }

  /**
   * A ranked list of containing or adjacent areas. The most useful (recognizable and precise) areas
   * are ranked first.
   */
  public Area areas[];

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[AddressDescriptor: ");
    if (areas != null && areas.length > 0) {
      sb.append("areas=").append(Arrays.toString(areas));
    }
    if (landmarks != null && landmarks.length > 0) {
      sb.append(", landmarks=").append(Arrays.toString(landmarks));
    }
    sb.append("]");
    return sb.toString();
  }
}
