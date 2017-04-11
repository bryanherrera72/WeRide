package www.weride.com.classes;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bryanherrera on 4/11/17.
 * Used to update current users location in Firebase.
 */

public class LocationUpdater {
    String id;
    DatabaseReference locationref;
    public LocationUpdater(FirebaseUser user, FirebaseDatabase db){
        id = user.getUid();
        locationref = db.getReference("/users").child(id).child("location");
    }

    public void updateLocation(){
        /*find out what type of variable should be passed into here to represent point*/
        /*should it be Lat = 0 Lng = 1?*/
    }
}
