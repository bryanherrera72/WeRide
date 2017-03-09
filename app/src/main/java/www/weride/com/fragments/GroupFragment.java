package www.weride.com.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import www.weride.com.MainActivity;
import www.weride.com.R;
import www.weride.com.classes.GroupInfo;
import www.weride.com.classes.GroupsListAdapter;

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

    //extra initializers
    private EditText group_name;
    private Button group_create;
    private String groupId;
    private List<GroupInfo> li;
    /////

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView groupslist;
    private GroupsListAdapter groupadapter;
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        li = getData();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        groupslist = (RecyclerView) view.findViewById(R.id.groups_list);
        groupadapter = new GroupsListAdapter(getActivity(), li);
        groupslist.setAdapter(groupadapter);
        groupslist.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final Button createGroup = (Button) view.findViewById(R.id.create_group_button);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create the dialog.
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.create_group_dialog);
                group_name = (EditText)dialog.findViewById(R.id.group_name);
                group_create = (Button)dialog.findViewById(R.id.group_create);

                //non functioning firebase stuff
                /*
                db = FirebaseDatabase.getInstance();
                reference = db.getReference("Groups");
                */
                ////////////

                group_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = group_name.getText().toString();
                        if(TextUtils.isEmpty(groupId)){
                            createGroup(name);
                            groupadapter.notifyDataSetChanged();
                            dialog.hide();
                        }

                        //startActivity(new Intent(CreateGroup.this, GroupActivity.class));
                        //finish();
                    }

                });
                dialog.show();
            }
        });
        return view;
    }
    public static List<GroupInfo> getData(){
        List<GroupInfo> data = new ArrayList<>();
        String[] titles = {"Bryan's Group", "Friends", "Work"};
        for(int i = 0; i < titles.length; i++){
            GroupInfo current = new GroupInfo();
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }

    public void createGroup(String s){
        GroupInfo group = new GroupInfo();
        group.iconId = R.drawable.ic_group;
        group.title = s;
        li.add(group);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
