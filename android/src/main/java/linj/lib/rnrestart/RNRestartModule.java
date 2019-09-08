
package linj.lib.rnrestart;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import android.util.Log;

import com.facebook.soloader.SoLoader;

import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.bridge.JavaScriptExecutorFactory;
import com.facebook.react.jscexecutor.JSCExecutorFactory;
import com.facebook.hermes.reactexecutor.HermesExecutorFactory;

import static com.facebook.react.modules.systeminfo.AndroidInfoHelpers.getFriendlyDeviceName;
import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import java.io.File;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Application;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactInstanceManager;

import androidx.annotation.Nullable;

public class RNRestartModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNRestartModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  // android.os.Process.killProcess(android.os.Process.myPid())

  @ReactMethod
  public void restartApp(final @Nullable String jsBundleFile) {
    try {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (getCurrentActivity() != null) {
            Application app = getCurrentActivity().getApplication();
            ReactApplication reactApp = (ReactApplication) app;
            ReactNativeHost host = reactApp.getReactNativeHost();
            ReactInstanceManager reactInstanceManager = host.getReactInstanceManager();
            try {
              Class<?> ReactInstanceManagerClazz = reactInstanceManager.getClass();
              Method method = ReactInstanceManagerClazz.getDeclaredMethod("recreateReactContextInBackground", JavaScriptExecutorFactory.class, JSBundleLoader.class);
              method.setAccessible(true);
              method.invoke(
                reactInstanceManager,
                getJavaScriptExecutorFactory(app.getPackageName(), getFriendlyDeviceName()),
                JSBundleLoader.createFileLoader(jsBundleFile != null ? jsBundleFile : app.getFilesDir() + "/index.android.bundle")
              );
              Log.v("ReactNativeRestartApp", "success!");
            }
            catch(NoSuchMethodException e) {
              Log.v("ReactNativeRestartApp", "NoSuchMethodException");
              e.printStackTrace();
            }
            catch(IllegalAccessException e) {
              Log.v("ReactNativeRestartApp", "IllegalAccessException");
              e.printStackTrace();
            }
            catch(InvocationTargetException e) {
              Log.v("ReactNativeRestartApp", "InvocationTargetException");
              e.printStackTrace();
            }
            catch(IllegalArgumentException e) {
              Log.v("ReactNativeRestartApp", "IllegalArgumentException");
              e.printStackTrace();
            }
          }
        }

        // copy from ReactInstanceManagerBuilder.getDefaultJSExecutorFactory
        // line: 287
        private JavaScriptExecutorFactory getJavaScriptExecutorFactory(final String appName, final String deviceName) {
          try {
            // If JSC is included, use it as normal
            SoLoader.loadLibrary("jscexecutor");
            return new JSCExecutorFactory(appName, deviceName);
          }
          catch (UnsatisfiedLinkError jscE) {
            // Otherwise use Hermes
            return new HermesExecutorFactory();
          }
        }
      });
    }
    finally {}
  }

  @Override
  public String getName() {
    return "RNRestart";
  }
}