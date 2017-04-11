package www.weride.com.classes;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bryanherrera on 4/11/17.
 * this class toggles which group is active
 *
 */

public class ActiveToggler {
    String userid;
    String groupid;
    DatabaseReference userref;
    DatabaseReference activeref;
    public ActiveToggler(FirebaseUser user, FirebaseDatabase db){
        userid = user.getUid();
        userref = db.getReference("/users").child(userid);
        syncActiveGroup();
    }

    public void setAsActiveGroup(String groupid){
        this.groupid = groupid;
        activeref = userref.child("groups").child("current_active");
        activeref.setValue(groupid);
    }
    public void turnOffActive(){
        activeref = userref.child("groups").child("current_active");
        activeref.setValue("none");
    }

    private void syncActiveGroup(){
        activeref = userref.child("groups").child("current_active");
        activeref.addValueEventListener(new ValueEventListener() {
            String groupid = "none";
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupid= (String) dataSnapshot.getValue();
                setGroupId(groupid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setGroupId(String groupid){
        this.groupid = groupid;
    }
    public String getGroupId(){
        return groupid;
    }
}
