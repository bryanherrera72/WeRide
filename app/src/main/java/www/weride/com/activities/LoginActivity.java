package www.weride.com.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        username_box = (EditText) findViewById(R.id.username_box);
        password_box = (EditText) findViewById(R.id.password_box);

        loginbtn = (Button) findViewById(R.id.login_button);

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
                   sendToMain();
               }
            }
        });
    }
    private void sendToMain(){
        Intent i = new Intent(getContext(), MainActivity.class);
        //pass the user object into main.
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
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
}
