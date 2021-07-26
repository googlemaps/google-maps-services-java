package com.google.maps.android;

import java.lang.reflect.Field;
import org.jetbrains.annotations.Nullable;

/** Wrapper for the Android class android.content.pm.PackageInfo */
public class PackageInfo {
  private Class<?> piClass;
  private Object piInstance;

  public PackageInfo(Class<?> piClass, Object piInstance) {
    this.piClass = piClass;
    this.piInstance = piInstance;
  }

  /** @return the signing signature for the app */
  @Nullable
  public Object signingSignature() {
    try {
      Field signaturesField = piClass.getField("signatures");
      Object[] signatures = (Object[]) signaturesField.get(piInstance);
      if (signatures == null || signatures.length == 0 || signatures[0] == null) {
        return null;
      }
      return signatures[0];
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
}
