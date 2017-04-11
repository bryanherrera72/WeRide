package www.weride.com.classes;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.internal.zzbmn;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bryanherrera on 4/9/17.
 */

public class User {
   String id;
    HashMap<String,String> groups;
    ArrayList<Double> location;

    public User(){
//        default required for FB
    }
    public User(String id, HashMap<String,String> groups, ArrayList<Double> location){
        this.id = id;
        this.groups = groups;
        this.location = location;
    }

    public String getId(){
        return this.id;
    }
    public HashMap<String,String> getGroups(){
        return this.groups;
    }
    public ArrayList<Double> getLocation(){
        return this.location;
    }
}
