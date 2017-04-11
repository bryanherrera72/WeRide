package www.weride.com.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            Intent i;
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = (FirebaseUser) mAuth.getCurrentUser();

                if(!(user == null)){
//                   mAuth.signOut();
                    i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        };

    }
    public Context getContext(){
        return (Context) this;
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop(){
        super.onStop();
        if(!(mAuthListener == null))
            mAuth.removeAuthStateListener(mAuthListener);
    }
}
