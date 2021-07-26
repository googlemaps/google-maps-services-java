package com.google.maps.android;

import org.jetbrains.annotations.Nullable;

/**
 * Configuration object containing Android authentication parameters for a particular installation.
 * The parameters in this config are used by all requests so that API key restrictions can be
 * enforced.
 */
public class AndroidAuthenticationConfig {
  public static AndroidAuthenticationConfig EMPTY = new AndroidAuthenticationConfig(null, null);

  /** The package name of the Android app. */
  @Nullable public final String packageName;

  /** The SHA-1 fingerprint of the certificate used to sign the Android app. */
  @Nullable public final String certFingerprint;

  public AndroidAuthenticationConfig(
      @Nullable String packageName, @Nullable String certFingerprint) {
    this.packageName = packageName;
    this.certFingerprint = certFingerprint;
  }
}
