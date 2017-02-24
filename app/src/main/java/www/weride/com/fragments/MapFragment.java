package www.weride.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mapzen.android.graphics.MapView;
import com.mapzen.android.graphics.MapzenMap;
import com.mapzen.android.graphics.OnMapReadyCallback;
import com.mapzen.android.graphics.model.BubbleWrapStyle;
import com.mapzen.android.graphics.model.CameraType;
import com.mapzen.pelias.widget.PeliasSearchView;
import com.mapzen.tangram.LngLat;

import www.weride.com.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends com.mapzen.android.graphics.MapFragment implements OnMapReadyCallback{
    PeliasSearchView searchView;
    MapzenMap map;
    private boolean enableLocationOnResume = false;
    private ImageButton findme;
    int[] findmelocation = new int[2];

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean permissionsvalid = false;
    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View map = inflater.inflate(R.layout.fragment_map, container, false);
        MapView mapview = (MapView) map.findViewById(R.id.fragment_map);
        mapview.getMapAsync(new BubbleWrapStyle(), this);
        findme = mapview.getFindMe();
        findme.setBackgroundResource(R.drawable.find_me_fab);
        findme.setImageResource(R.drawable.ic_find_me_dark);
        findme.getLocationInWindow(findmelocation);
        Log.i("location is: ", "" + findmelocation[0]);
        return map;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setPermissionsvalid(boolean valid){
        permissionsvalid = valid;
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
    public void onResume(){
        super.onResume();
        if(!(map == null)) {
            if (enableLocationOnResume) {
                map.setMyLocationEnabled(true);
            }
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        if(!( map == null)){
            if (map.isMyLocationEnabled()) {
                map.setMyLocationEnabled(false);
                enableLocationOnResume = true;
            }
        }
    }

    @Override
    public void onMapReady(MapzenMap mapzenMap) {
        //set the current instance of the map to this "READY" map
        //allows access to it throughout the current fragment instance.
        MapFragment.this.map = mapzenMap;
        //set some initial configs
        mapzenMap.setCameraType(CameraType.ISOMETRIC);
        //determine if location is allowed, if so, display current location button.
        if(permissionsvalid) {
            Log.i("true?", "" + permissionsvalid);
            mapzenMap.setMyLocationEnabled(true);
        }
    }

    public void displayPoint(LngLat destpoint) {
        map.drawSearchResult(destpoint);
        map.setPosition(destpoint);
        map.setZoom(15);
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
        void onPermissionsValid(boolean valid);
        void something(LngLat dest);

    }



}
