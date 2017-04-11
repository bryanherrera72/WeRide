package www.weride.com.classes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bryanherrera on 4/9/17.
 */

public class Group {
    private String id;
    private String title;
    private HashMap<String,String> users;
    private ArrayList<Double> destination;

    public Group(){

    }
    public Group(String id, String title, HashMap<String,String> users,ArrayList<Double>destination){
        this.id = id;
        this.title = title;
        this.users = users;
        this.destination=destination;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public HashMap<String,String> getUsers() {
        return users;
    }

    public ArrayList<Double> getDestination() {
        return destination;
    }
}
