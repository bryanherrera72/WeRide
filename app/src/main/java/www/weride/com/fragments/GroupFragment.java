package www.weride.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.weride.com.MainActivity;
import www.weride.com.R;
import www.weride.com.classes.ActiveToggler;
import www.weride.com.classes.Group;
import www.weride.com.classes.GroupsListAdapter;
import www.weride.com.classes.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference dbref;
    HashMap<String,String> mygroups;
    ListView groupsview;
    List<Group> groupsdetail;
    Button creategroup;
    DatabaseReference userref;
    DatabaseReference groupref;
    ArrayList<Group> retrievedgrouplist;
    ActiveToggler at;
    GroupsListAdapter adapter;
    public GroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        at = new ActiveToggler(user,db);
        grabListForUser(user);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        groupsview = (ListView) view.findViewById(R.id.groups_list);
        creategroup = (Button) view.findViewById(R.id.create_group_button);
        creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createGroup(user, "");
                CreateGroupDialogFragment dialog = new CreateGroupDialogFragment();
                dialog.show(getFragmentManager(), "create");
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                ((MainActivity)getActivity()).mainDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
    Grab current users groups that they belong to, and update the UI.
    */
    private void grabListForUser(FirebaseUser user){

        dbref=db.getReference("/users");
        userref = dbref.child(user.getUid()).child("groups");
        ValueEventListener groupsListener = new ValueEventListener() {

            ArrayList<String> list=new ArrayList<String>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> groups = (HashMap<String,String>) dataSnapshot.getValue();
                if(!(groups == null)){
                    /*We'll add the id's that are part of current users group list, into a proper list*/
                    List<String> idslist = new ArrayList<String>();
                    for(Map.Entry<String,String> groupid: groups.entrySet()){
                        String key = groupid.getKey();
                        if(!(key.equals("current_active"))){
                            idslist.add(groupid.getValue());
                        }
                    }
                    /*now that we have the list, we can search the DB for their info*/
                    retrieveGroupDetailFromDb(idslist);
                    /*now we have a detailed groups list stored inside 'retrievedgrouplist'
                    Create a list view from it.*/
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        userref.addValueEventListener(groupsListener);
    }

    //Display the list view for the current user.
    private void updateUI(){

        if(!(retrievedgrouplist == null)){

            final ArrayList<Group> groups = retrievedgrouplist;
            if(getActivity()!=null){
                adapter = new GroupsListAdapter(getContext(),  groups, at);
                groupsview.setAdapter(adapter);
            }

        }

    }
    private void retrieveGroupDetailFromDb(List<String> groupids){
        final List<String> final_ids= groupids;
        dbref=db.getReference("/groups");
        if(!(groupids.isEmpty())){
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        retrievedgrouplist = new ArrayList<Group>();
                        for(String id: final_ids){

                            Group retrievedgroup = (Group) dataSnapshot.child(id).getValue(Group.class);
                            retrievedgrouplist.add(retrievedgroup);
                            updateUI();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        }
    }
    public void createGroup(String title){
        createGroup(user, title);
    }
    //Create group method
    private void createGroup(FirebaseUser user, String title){
        dbref = db.getReference("/");
        HashMap<String,String> groupuserlist = new HashMap<String,String>();
        ArrayList<Double> currentuserlocation = new ArrayList<Double>();
        /*a value of zero will indicate a creator*/
        groupuserlist.put("creator", user.getUid());
        DatabaseReference pushedGroupRef = dbref.child("groups").push();
        Group newgroup = new Group(pushedGroupRef.getKey(), title,groupuserlist,currentuserlocation);
        pushedGroupRef.setValue(newgroup);
        /*We also need to add the new groups id to the current users group list since, you know, they created it*/
        dbref.child("users").child(user.getUid()).child("groups").push().setValue(pushedGroupRef.getKey());
    }

}
