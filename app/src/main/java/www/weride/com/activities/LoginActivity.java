package www.weride.com.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import www.weride.com.MainActivity;
import www.weride.com.R;

public class LoginActivity extends AppCompatActivity {
    private EditText username_box, password_box;
    private Button loginbtn;
    private FirebaseAuth mAuth;
    private TextView sign_up;
    private CheckBox remember_me;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();
        username_box = (EditText) findViewById(R.id.username_box);
        password_box = (EditText) findViewById(R.id.password_box);
        remember_me = (CheckBox)findViewById(R.id.rememberMe);

        loginbtn = (Button) findViewById(R.id.login_button);
        if(mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().getPhotoUrl()!=null){
            sendToMain();
            return;
        }
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = username_box.getText().toString();
                String password = password_box.getText().toString();
                checkFirebaseCreds(username, password);

            }
        });
        sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void checkFirebaseCreds(String username, String password){
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
               }
               else{

                   FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                   SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                   SharedPreferences.Editor editor = preferences.edit();
                   if(remember_me.isChecked()){
                       editor.putBoolean("remembered", true);
                       assert user != null;
                       editor.putString("user_name", user.getDisplayName());
                       editor.putString("email", user.getEmail());
                       editor.putString("uid", user.getUid());
                   }else{
                       editor.putBoolean("remembered", false);
                       editor.remove("user_name");
                       editor.remove("email");
                       editor.remove("uid");
                   }
                   editor.apply();
                   sendToSetProfile();
               }
            }
        });
    }
    private void sendToMain(){
        Intent i = new Intent(getContext(), MainActivity.class);
        //pass the user object into main.
        startActivity(i);
        finish();
    }
    private Context getContext(){
        return (Context) this;
    }
    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onStop(){
        super.onStop();
    }

    private void sendToSetProfile(){
        Intent intent = new Intent(getContext(), SetProfilePicture.class);
        startActivity(intent);
        finish();
    }
}
