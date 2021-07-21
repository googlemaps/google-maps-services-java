package com.google.maps.android;

import com.google.maps.internal.HttpHeaders;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

/**
 * Intercepts requests and provides Android-specific headers so that API key restrictions can be
 * enforced.
 */
public class AndroidAuthenticationInterceptor implements Interceptor {

  private final AndroidAuthenticationConfig config;

  public AndroidAuthenticationInterceptor(AndroidAuthenticationConfig config) {
    this.config = config;
  }

  @NotNull
  @Override
  public Response intercept(@NotNull Chain chain) throws IOException {
    final Request request = chain.request();

    if (config == AndroidAuthenticationConfig.EMPTY) {
      // Not in Android environment
      return chain.proceed(request);
    }

    final Request.Builder builder = chain.request().newBuilder();
    if (config.packageName != null) {
      builder.addHeader(HttpHeaders.X_ANDROID_PACKAGE, config.packageName);
    }

    if (config.certFingerprint != null) {
      builder.addHeader(HttpHeaders.X_ANDROID_CERT, config.certFingerprint);
    }

    return chain.proceed(builder.build());
  }
}
