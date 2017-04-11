package www.weride.com.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import www.weride.com.MainActivity;
import www.weride.com.R;
import www.weride.com.classes.User;

public class SignUpActivity extends AppCompatActivity {
    String fname, lname, email, password, vpassword;
    EditText fname_box, lname_box,email_box, password_box,vpassword_box;
    Button sign_up;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth= FirebaseAuth.getInstance();
        fname_box = (EditText) findViewById(R.id.first_name_box);
        lname_box = (EditText) findViewById(R.id.last_name_box);
        email_box = (EditText) findViewById(R.id.sign_up_email_box);
        password_box = (EditText) findViewById(R.id.sign_up_password_box);
        vpassword_box = (EditText) findViewById(R.id.verify_password_box);

        sign_up = (Button) findViewById(R.id.sign_up_button);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname = fname_box.getText().toString();
                lname = lname_box.getText().toString();
                email = email_box.getText().toString();
                password = password_box.getText().toString();
                vpassword = vpassword_box.getText().toString();
                signUp(fname,lname,email,password,vpassword);
            }
        });
    }

    private void signUp(String first_name, String last_name, String email, String password, String vpassword){
        final String fname = first_name;
        final String lname = last_name;
        if(password.equals(vpassword) && !(first_name.equals("")) && !(last_name.equals(""))) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(getContext(), "Could not make the user!!",Toast.LENGTH_SHORT).show();
                    }
                    else{

                        FirebaseUser user;
                        User currentuser;
                        user = mAuth.getCurrentUser();
                        setDefaultUser(user);

                        UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fname + " " + lname)
                                .build();

                        user.updateProfile(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {

                                    Intent i = new Intent(getContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });

                    }
                }
            });
        }
    }
    //This will setup the user in the RealtimeDB
    private void setDefaultUser(FirebaseUser user){
        db = FirebaseDatabase.getInstance();
        dbref = db.getReference("/");
        HashMap<String,String> defaultgroupslist = new HashMap<>();
        ArrayList<Double> defaultlocation = new ArrayList<Double>();
        defaultgroupslist.put("current_active","none");
        defaultlocation.add(0.0);
        defaultlocation.add(0.0);
        User currentuser = new User(user.getUid(), defaultgroupslist, defaultlocation);
        dbref.child("users").child(currentuser.getId()).setValue(currentuser);
    }
    private Context getContext(){
        return (Context) this;
    }
}
