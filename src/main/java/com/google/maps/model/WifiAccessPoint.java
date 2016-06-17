package com.google.maps.model;

/**
 * WiFi access point objects
 *
 * The request body's wifiAccessPoints array must contain two or more WiFi access point objects.
 * {@code macAddress} is required; all other fields are optional.
 *
 * <p>Please see <a href="https://developers.google.com/maps/documentation/geolocation/intro#wifi_access_point_object">
 * WiFi Access Point Objects</a> for more detail.
 *
 */
public class WifiAccessPoint {
  /**
  * {@code macAddress}: (required) The MAC address of the WiFi node. Separators must be : (colon) and
  * hex digits must use uppercase.
  */
  public String macAddress;

  /**
  * {@code signalStrength}: The current signal strength measured in dBm.
  */
  public int signalStrength;

  /**
   * {@code age}: The number of milliseconds since this access point was detected.
   */
  public int age;

  /**
   * {@code channel}: The channel over which the client is communicating with the access point.
   */
  public int channel;

  /**
   * {@code signalToNoiseRatio}: The current signal to noise ratio measured in dB.
   */
  public int signalToNoiseRatio;
}
