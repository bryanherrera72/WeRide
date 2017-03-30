package www.weride.com.utils;

/**
 * Created by Francis on 3/26/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.weride.www.awsmobilehelper.auth.DefaultSignInResultHandler;
import com.weride.www.awsmobilehelper.auth.IdentityProvider;
import www.weride.com.MainActivity;
import www.weride.com.R;

/**
 * Handles Re-directing to the main activity upon sign-in.
 */
/*package*/
public class SignInHandler extends DefaultSignInResultHandler {
    private static final String LOG_TAG = SignInHandler.class.getSimpleName();

    @Override
    public void onSuccess(final Activity callingActivity, final IdentityProvider provider) {
        if (provider != null) {
            Log.d(LOG_TAG, String.format("User sign-in with %s provider succeeded",
                    provider.getDisplayName()));
            Toast.makeText(callingActivity, String.format(
                    callingActivity.getString(R.string.sign_in_succeeded_message_format),
                    provider.getDisplayName()), Toast.LENGTH_LONG).show();
        }

        goMain(callingActivity);
    }

    @Override
    public boolean onCancel(final Activity callingActivity) {
        // User abandoned sign in flow.
        // For Mandatory Auth the app will exit unless an activity is launched here.
        final boolean shouldFinishSignInActivity = true;
        return shouldFinishSignInActivity;
    }

    /** Go to the main activity. */
    private void goMain(final Activity callingActivity) {
        callingActivity.startActivity(new Intent(callingActivity, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
