package www.weride.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import www.weride.com.MainActivity;
import www.weride.com.R;
/*
* This activity will load initial configs for the app.
* Also gives the user something nice to look at while they wait :)
* */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Intent intent = new Intent(this, LoginActivity.class);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
