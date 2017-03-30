package www.weride.com.utils;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import www.weride.com.amazonaws.mobile.AWSMobileClient;
import www.weride.com.amazonaws.mobile.push.PushManager;

/**
 * Created by Francis on 3/27/2017.
 */

/**
 * Application class responsible for initializing singletons and other common components.
 */
public class Application extends MultiDexApplication {
    private static final String LOG_TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "Application.onCreate - Initializing application...");
        super.onCreate();
        initializeApplication();
        Log.d(LOG_TAG, "Application.onCreate - Application initialized OK");
    }

    private void initializeApplication() {
        AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());

        // Set a listener for changes in push notification state
        PushManager.setPushStateListener(new PushManager.PushStateListener() {
            @Override
            public void onPushStateChange(final PushManager pushManager, boolean isEnabled) {
                Log.d(LOG_TAG, "Push Notifications Enabled = " + isEnabled);
                // ...Put any application-specific push state change logic here...
            }
        });

        // ...Put any application-specific initialization logic here...
    }
}