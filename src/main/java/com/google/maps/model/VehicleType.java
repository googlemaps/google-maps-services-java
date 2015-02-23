package com.google.maps.model;

/**
 * The vehicle types.
 *
 * <p>See <a href="https://developers.google.com/maps/documentation/directions/#VehicleType">
 * Vehicle Type</a> for more detail.
 */
public enum VehicleType {

  /**
   * Rail.
   */
  RAIL,

  /**
   * Light rail transit.
   */
  METRO_RAIL,

  /**
   * Underground light rail.
   */
  SUBWAY,

  /**
   * Above ground light rail.
   */
  TRAM,

  /**
   * Monorail.
   */
  MONORAIL,

  /**
   * Heavy rail.
   */
  HEAVY_RAIL,

  /**
   * Commuter rail.
   */
  COMMUTER_TRAIN,

  /**
   * High speed train.
   */
  HIGH_SPEED_TRAIN,

  /**
   * Bus.
   */
  BUS,

  /**
   * Intercity bus.
   */
  INTERCITY_BUS,

  /**
   * Trolleybus.
   */
  TROLLEYBUS,

  /**
   * Share taxi is a kind of bus with the ability to drop off and pick up passengers anywhere on its
   * route.
   */
  SHARE_TAXI,

  /**
   * Ferry.
   */
  FERRY,

  /**
   * A vehicle that operates on a cable, usually on the ground. Aerial cable cars may be of the type
   * {@code GONDOLA_LIFT}.
   */
  CABLE_CAR,

  /**
   * An aerial cable car.
   */
  GONDOLA_LIFT,

  /**
   * A vehicle that is pulled up a steep incline by a cable. A Funicular typically consists of two
   * cars, with each car acting as a counterweight for the other.
   */
  FUNICULAR,
  
  /**
   * All other vehicles will return this type.
   */
  OTHER
}
