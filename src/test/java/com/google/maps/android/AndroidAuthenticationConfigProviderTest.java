package com.google.maps.android;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.google.maps.SmallTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.MockedStatic;

@Category(SmallTests.class)
public class AndroidAuthenticationConfigProviderTest {

  private AndroidAuthenticationConfigProvider provider;

  @Before
  public void setUp() {
    provider = new AndroidAuthenticationConfigProvider();
  }

  @Test
  public void testEmptyConfigIfNotAndroid() {
    AndroidAuthenticationConfig config = provider.provide();
    Assert.assertEquals(AndroidAuthenticationConfig.EMPTY, config);
  }

  @Test
  public void testConfigPresentIfAndroid() {
    String packageName = "com.test.test.test";

    Context mockContext = mock(Context.class);
    when(mockContext.getPackageName()).thenReturn(packageName);
    MockedStatic<Context> mockedStaticContext = mockStatic(Context.class);
    mockedStaticContext.when(Context::getApplicationContext).thenReturn(mockContext);

    PackageInfo mockPi = mock(PackageInfo.class);
    when(mockPi.signingSignature()).thenReturn(null);

    PackageManager mockPm = mock(PackageManager.class);
    when(mockPm.getPackageInfo(packageName, 64)).thenReturn(mockPi);
    when(mockContext.getPackageManager()).thenReturn(mockPm);

    AndroidAuthenticationConfig config = provider.provide();
    Assert.assertEquals(packageName, config.packageName);
    Assert.assertNull(config.certFingerprint);

    mockedStaticContext.close();
  }
}
