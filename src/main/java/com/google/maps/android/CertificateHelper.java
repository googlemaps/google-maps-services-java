package com.google.maps.android;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Helper class for obtaining information about an Android app's signing certificate. */
public class CertificateHelper {

  /**
   * Obtains the SHA-1 fingerprint of the certificate used to sign the app.
   *
   * @param context a Context
   * @return the SHA-1 fingerprint if obtainable, otherwise, <code>null</code>
   */
  @Nullable
  public static String getSigningCertificateSha1Fingerprint(@NotNull Context context) {
    final PackageManager pm = context.getPackageManager();
    if (pm == null) {
      return null;
    }

    // PackageManage.GET_SIGNATURES == 64
    PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 64);
    if (packageInfo == null) {
      return null;
    }

    Object signingSignature = packageInfo.signingSignature();
    if (signingSignature == null) {
      return null;
    }

    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      Class<?> signatureClass = Class.forName("android.content.pm.Signature");
      Method toByteArrayMethod = signatureClass.getMethod("toByteArray");
      byte[] byteArray = (byte[]) toByteArrayMethod.invoke(signingSignature);
      byte[] digest = md.digest(byteArray);
      return bytesToHex(digest);
    } catch (NoSuchAlgorithmException
        | ClassNotFoundException
        | IllegalAccessException
        | NoSuchMethodException
        | InvocationTargetException e) {
      return null;
    }
  }

  private static String bytesToHex(byte[] byteArray) {
    final StringBuilder sb = new StringBuilder();
    for (byte b : byteArray) {
      sb.append(String.format("%02X", b));
    }
    return sb.toString();
  }
}
