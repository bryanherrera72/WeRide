package www.weride.com.classes;

import java.util.HashMap;

/**
 * Created by bryanherrera on 4/9/17.
 */

public class Users {
    private HashMap<String,User> users;

    public Users(){

    }
    public Users(HashMap<String,User> users){
        this.users = users;
    }

    public HashMap<String,User> getUsers(){
        return users;
    }
}
