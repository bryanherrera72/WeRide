package www.weride.com.classes;

import android.location.Location;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bryanherrera on 4/11/17.
 * Used to update current users location in Firebase.
 * Locations are processed as LAT, LNG
 */

public class LocationUpdater {
    String userid;
    ArrayList<DatabaseReference> groupmemberrefs;
    DatabaseReference locationref,groupref, userref;
    double[] previouspnt = new double[2];
    FirebaseDatabase db;
    public LocationUpdater(FirebaseUser user, FirebaseDatabase db){
        userid = user.getUid();
        this.db = db;
        locationref = db.getReference("/users").child(userid).child("location");
    }

    public void updateLocation(double[] point){
        Double lat, lng;
        if(!(previouspnt[0] == point[0]) || !(previouspnt[1] == point[1])) {
            previouspnt[0] = point[0];
            previouspnt[1] = point[1];
            lat = point[0];
            lng = point[1];
            locationref.child("0").setValue(lng);
            locationref.child("1").setValue(lat);
        }
    }
    /*
    *  Sets the data change listeners for the group at the specified location
    * */
    public void setListenersForGroup(String groupid){
        groupref = getGroupRef(groupid);
        groupref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> group = (HashMap<String, Object>) dataSnapshot.getValue();
                if (!(group == null)){
                    HashMap<String, String> groupusers = (HashMap<String, String>) group.get("users");
                    ArrayList<DatabaseReference> friends = new ArrayList<DatabaseReference>();
                    for (Map.Entry<String, String> user : groupusers.entrySet()) {
                        if (!(user.equals(userid))) {
                            userref = getUserRef(user.getValue());
                            friends.add(userref);
                        }
                    }
                    setFriendLocationReferences(friends);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setFriendLocationReferences(ArrayList<DatabaseReference> friends){
        groupmemberrefs = friends;
    }

    private ArrayList<DatabaseReference> getFriendLocationReferences(){
        return groupmemberrefs;
    }
    private DatabaseReference getUserRef(String userid){
        return db.getReference("/users").child(userid);
    }
    private DatabaseReference getGroupRef(String groupid){
        return  db.getReference("/groups").child(groupid);
    }
}
