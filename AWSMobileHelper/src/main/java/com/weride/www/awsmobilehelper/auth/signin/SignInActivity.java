package com.weride.www.awsmobilehelper.auth.signin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.weride.www.awsmobilehelper.R;
import com.weride.www.awsmobilehelper.auth.IdentityManager;
import com.weride.www.awsmobilehelper.auth.IdentityProvider;
import com.weride.www.awsmobilehelper.auth.IdentityProviderType;
import com.weride.www.awsmobilehelper.auth.SignInResultHandler;

/**
 * Activity for handling Sign-in with an Identity Provider.
 */
public class SignInActivity extends Activity {
    private static final String LOG_TAG = SignInActivity.class.getSimpleName();
    private SignInManager signInManager;
    /**
     * Permission Request Code (Must be < 256).
     */
    private static final int GET_ACCOUNTS_PERMISSION_REQUEST_CODE = 93;

    /**
     * The Google OnClick listener, since we must override it to get permissions on Marshmallow and above.
     */
    private View.OnClickListener googleOnClickListener;

    /**
     * SignInProviderResultsHandler handles the final result from sign in. Making it static is a best
     * practice since it may outlive the SplashActivity's life span.
     */
    private class SignInProviderResultsHandler implements com.weride.www.awsmobilehelper.auth.signin.SignInProviderResultsHandler {
        /**
         * Receives the successful sign-in result and starts the main activity.
         *
         * @param provider the identity provider used for sign-in.
         */
        @Override
        public void onSuccess(final IdentityProvider provider) {
            Log.i(LOG_TAG, String.format("Sign-in with %s succeeded.", provider.getDisplayName()));

            // The sign-in manager is no longer needed once signed in.
            SignInManager.dispose();

            final IdentityManager identityManager = signInManager.getIdentityManager();
            final SignInResultHandler signInResultsHandler = signInManager.getResultHandler();

            // Load user name and image.
            identityManager.loadUserInfoAndImage(provider, new Runnable() {
                @Override
                public void run() {
                    // Call back the results handler.
                    signInResultsHandler.onSuccess(SignInActivity.this, provider);
                }
            });
            finish();
        }

        /**
         * Receives the sign-in result indicating the user canceled and shows a toast.
         *
         * @param provider the identity provider with which the user attempted sign-in.
         */
        @Override
        public void onCancel(final IdentityProvider provider) {
            Log.i(LOG_TAG, String.format("Sign-in with %s canceled.", provider.getDisplayName()));
            signInManager.getResultHandler()
                    .onIntermediateProviderCancel(SignInActivity.this, provider);
        }

        /**
         * Receives the sign-in result that an error occurred signing in and shows a toast.
         *
         * @param provider the identity provider with which the user attempted sign-in.
         * @param ex       the exception that occurred.
         */
        @Override
        public void onError(final IdentityProvider provider, final Exception ex) {
            Log.e(LOG_TAG, String.format("Sign-in with %s caused an error.", provider.getDisplayName()), ex);
            signInManager.getResultHandler()
                    .onIntermediateProviderError(SignInActivity.this, provider, ex);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInManager = SignInManager.getInstance();

        signInManager.setProviderResultsHandler(this, new SignInProviderResultsHandler());

        // Initialize sign-in buttons.
        signInManager.initializeSignInButton(IdentityProviderType.FACEBOOK,
                this.findViewById(R.id.fb_login_button));

        googleOnClickListener =
                signInManager.initializeSignInButton(IdentityProviderType.GOOGLE, findViewById(R.id.g_login_button));

        if (googleOnClickListener != null) {
            // if the onClick listener was null, initializeSignInButton will have removed the view.
            this.findViewById(R.id.g_login_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final Activity thisActivity = SignInActivity.this;
                    if (ContextCompat.checkSelfPermission(thisActivity,
                            Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SignInActivity.this,
                                new String[]{Manifest.permission.GET_ACCOUNTS},
                                GET_ACCOUNTS_PERMISSION_REQUEST_CODE);
                        return;
                    }

                    // call the Google onClick listener.
                    googleOnClickListener.onClick(view);
                }
            });
        }

        signInManager.initializeSignInButton(IdentityProviderType.COGNITO_USER_POOL,
                this.findViewById(R.id.signIn_imageButton_login));
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String permissions[],
                                           final int[] grantResults) {
        if (requestCode == GET_ACCOUNTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.findViewById(R.id.g_login_button).callOnClick();
            } else {
                Log.i(LOG_TAG, "Permissions not granted for Google sign-in. :(");
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        signInManager.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (signInManager.getResultHandler().onCancel(this)) {
            super.onBackPressed();
            // Since we are leaving sign-in via back, we can dispose the sign-in manager, since sign-in was cancelled.
            SignInManager.dispose();
        }
    }
}

