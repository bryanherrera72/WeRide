package www.weride.com.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import www.weride.com.MainActivity;

/**
 * Created by Francis on 4/13/17.
 */

public class FirebaseStorageHelper {

    private FirebaseStorage firebaseStorage;
    private StorageReference rootRef;
    private Context context;
    private FirebaseUser user;


    public FirebaseStorageHelper(Context context){
        this.context = context;
        this.firebaseStorage = FirebaseStorage.getInstance();
        rootRef = firebaseStorage.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void saveImage(String uid, final Uri imageuri, final ImageView imageView){
        StorageReference photoParentRef = rootRef.child(uid);
        StorageReference photoRef = photoParentRef.child(imageuri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(imageuri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Glide.with(context).load(downloadUrl.getPath()).into(imageView);
                UserProfileChangeRequest change = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(imageuri).build();
                user.updateProfile(change).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {


                        }
                    }
                });
            }
        });
    }
}
