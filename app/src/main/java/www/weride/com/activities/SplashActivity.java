package www.weride.com.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import www.weride.com.MainActivity;
import www.weride.com.R;
import www.weride.com.classes.Users;

/*
* This activity will load initial configs for the app.
* Also gives the user something nice to look at while they wait :)
* */
public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase db;
    private DatabaseReference dbref;
    private DatabaseReference usersref;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if(!(user == null)){
                   Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                   Intent i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            Intent i;
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = (FirebaseUser) mAuth.getCurrentUser();
//
//                if(!(user == null)){
//                    i = new Intent(getContext(), MainActivity.class);
//                    startActivity(i);
//                    finish();
//                }else{
//                    i = new Intent(getContext(), LoginActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//
//            }
//        };

    }
    public Context getContext(){
        return (Context) this;
    }
//    @Override
//    public void onStart(){
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    public void onStop(){
//        super.onStop();
//        if(!(mAuthListener == null))
//            mAuth.removeAuthStateListener(mAuthListener);
//    }
}
