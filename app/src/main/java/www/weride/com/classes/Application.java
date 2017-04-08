package www.weride.com.classes;

/**
 * Created by Francis on 2/28/17.
 */
import android.support.multidex.MultiDexApplication;
import android.util.Log;

public class Application extends MultiDexApplication{

    private final static String LOG_TAG = Application.class.getSimpleName();

    @Override
    public void onCreate(){
        Log.d(LOG_TAG, "Application.onCreate - Initializing application...");
        super.onCreate();
        initializeApplication();
        Log.d(LOG_TAG, "Application.onCreate - Application initialized OK");
    }

    private void initializeApplication() {

    }

}
