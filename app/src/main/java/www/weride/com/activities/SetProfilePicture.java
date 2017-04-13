package www.weride.com.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import www.weride.com.MainActivity;
import www.weride.com.R;
import www.weride.com.classes.FirebaseStorageHelper;

public class SetProfilePicture extends AppCompatActivity {

    private ImageView image;
    private Button save_btn;
    private FirebaseUser user;
    private static final int SELECT_PICTURE = 2000;
    private Bitmap bitmap;
    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile_picture);

        user = FirebaseAuth.getInstance().getCurrentUser();

        image = (ImageView) findViewById(R.id.image_profile);
        save_btn = (Button)findViewById(R.id.save_btn);

        image.setOnClickListener(onProfileImageListener);
        save_btn.setOnClickListener(onProfileSave);
    }

    View.OnClickListener onProfileImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Intent it = new Intent(Intent.ACTION_GET_CONTENT);
            it.setType("image/*");
            startActivityForResult(it, SELECT_PICTURE);
        }
    };

    View.OnClickListener onProfileSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(imgPath.isEmpty()){
                save_btn.setEnabled(false);
            }else{
                saveProfile(Uri.parse(imgPath));
                Intent it = new Intent(SetProfilePicture.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==SELECT_PICTURE){
            Uri imageUri = data.getData();
            imgPath = getPath(imageUri);
            FirebaseStorageHelper storage = new FirebaseStorageHelper(this);
            storage.saveImage(user.getUid(), imageUri, image);
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri,projection, null, null, null);
        assert cursor!=null;
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(index);
        cursor.close();
        return filePath;
    }


    private void saveProfile(Uri uri){
        UserProfileChangeRequest updatePic = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri).build();
        user.updateProfile(updatePic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(SetProfilePicture.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
