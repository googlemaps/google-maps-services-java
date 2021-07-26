package com.google.maps.android;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Wrapper for the Android class android.content.Context. */
public class Context {
  private Class<?> contextClass;
  private Object contextInstance;
  private PackageManager packageManager;

  Context(@NotNull Class<?> contextClass) {
    this.contextClass = contextClass;
  }

  /**
   * Performs reflection to see if the android.content.Context class is available at runtime. If so,
   * this method will return an instance of this class.
   *
   * @return an instance of this class if invoked within an Android environment, otherwise, null
   */
  @Nullable
  public static Context getApplicationContext() {
    try {
      Class<?> contextClass = Class.forName("android.content.Context");
      return new Context(contextClass);
    } catch (ClassNotFoundException e) {
      // Not Android
      return null;
    }
  }

  /** @return the package name of the Android app if available, otherwise, null */
  @Nullable
  public String getPackageName() {
    try {
      final Method method = contextClass.getMethod("getPackageName");
      return (String) method.invoke(getContextInstance());
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Similar to <a
   * href="https://developer.android.com/reference/android/content/Context#getPackageManager()">Context#getPackageManager</a>
   * but performed through reflection.
   *
   * @return the {@link PackageManager}
   */
  @Nullable
  public PackageManager getPackageManager() {
    if (packageManager != null) {
      return packageManager;
    }

    try {
      Class<?> pmClass = Class.forName("android.content.pm.PackageManager");
      final Method method = contextClass.getMethod("getPackageManager");
      final Object pm = method.invoke(getContextInstance());
      packageManager = new PackageManager(pmClass, pm);
    } catch (InvocationTargetException
        | IllegalAccessException
        | ClassNotFoundException
        | NoSuchMethodException e) {
      // Not Android
      e.printStackTrace();
    }
    return packageManager;
  }

  @Nullable
  private Object getContextInstance() {
    if (contextInstance != null) {
      return contextInstance;
    }

    try {
      final Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
      final Method method = activityThreadClass.getMethod("currentApplication");
      contextInstance = method.invoke(null);
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | InvocationTargetException
        | IllegalAccessException e) {
      e.printStackTrace();
    }
    return contextInstance;
  }
}
