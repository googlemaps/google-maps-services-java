package com.google.maps.android;

/**
 * Provides a {@link AndroidAuthenticationConfig} that is specific to the environment the library is
 * in.
 */
public class AndroidAuthenticationConfigProvider {

  /** @return the environment specific {@link AndroidAuthenticationConfig} */
  public AndroidAuthenticationConfig provide() {
    Context context = Context.getApplicationContext();
    if (context == null) {
      return AndroidAuthenticationConfig.EMPTY;
    }

    return new AndroidAuthenticationConfig(
        context.getPackageName(), CertificateHelper.getSigningCertificateSha1Fingerprint(context));
  }
}
