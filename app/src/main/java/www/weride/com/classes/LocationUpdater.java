package www.weride.com.classes;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bryanherrera on 4/11/17.
 * Used to update current users location in Firebase.
 */

public class LocationUpdater {
    String id;

    public LocationUpdater(FirebaseUser user, FirebaseDatabase db){
        id = user.getUid();
    }

    public void updateLocation(){

    }
}
