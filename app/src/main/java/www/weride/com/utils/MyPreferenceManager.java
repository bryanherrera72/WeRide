package www.weride.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Francis on 3/31/2017.
 */

public class MyPreferenceManager {
    private String TAG = MyPreferenceManager.class.getSimpleName();

    //Shared Preferences
    SharedPreferences preferences;
    //Editor for Shared preferences
    SharedPreferences.Editor editor;
    //Context
   private Context context;
    //Shared preference mode
    private int PRIVATE_MODE = 0;
    //Shared preference file name
    private static final String PREF_NAME = "weride_chat";

    //All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_NOTIFICATIONS = "notifications";

    //constructor
    public MyPreferenceManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void addNotification(String notification){
        //get old notifications
        String oldNotification = getNotifications();
        if(oldNotification != null){
            oldNotification += "|" + notification;
        }else{
            oldNotification = notification;
        }
        editor.putString(KEY_NOTIFICATIONS, oldNotification);
        editor.commit();
    }

    public String getNotifications(){
        return preferences.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
