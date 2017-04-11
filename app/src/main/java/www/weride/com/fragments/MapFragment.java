package www.weride.com.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.mapzen.android.core.MapzenManager;
import com.mapzen.android.graphics.CompassView;
import com.mapzen.android.graphics.MapView;
import com.mapzen.android.graphics.MapzenMap;
import com.mapzen.android.graphics.OnMapReadyCallback;
import com.mapzen.android.graphics.model.BubbleWrapStyle;
import com.mapzen.android.graphics.model.CameraType;

import com.mapzen.android.graphics.model.Polyline;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LostApiClient;
import com.mapzen.android.routing.MapzenRouter;
import com.mapzen.helpers.RouteEngine;
import com.mapzen.helpers.RouteListener;
import com.mapzen.model.ValhallaLocation;
import com.mapzen.pelias.widget.PeliasSearchView;
import com.mapzen.tangram.LngLat;
import com.mapzen.valhalla.Route;
import com.mapzen.valhalla.RouteCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.weride.com.R;
import www.weride.com.classes.LocationUpdater;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends com.mapzen.android.graphics.MapFragment implements OnMapReadyCallback, View.OnClickListener, LostApiClient.ConnectionCallbacks, RouteListener, RouteCallback, Callback{

    PeliasSearchView searchView;
    MapzenMap map;
    private boolean enableLocationOnResume = false;
    private ImageButton findme;
    CompassView compass;
    int[] findmelocation = new int[2];
    OnFragmentInteractionListener activity;
    MapzenManager mapzenman;
    FloatingActionButton navfab;
    FloatingActionButton fab;
    LngLat dest;
    LostApiClient lostApiClient;

    MapzenRouter router;
    RouteEngine engine;
    RouteListener routeListener;
    LocationRequest locationRequest;
    ValhallaLocation valhallaLocation;
    Route currentroute;
    LngLat searchedLocation = null;
    LocationUpdater lu;
    double distance;

    FirebaseUser user;
    FirebaseDatabase db;
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
        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        lu = new LocationUpdater(user, db);

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
        mapzenman.instance(getContext()).setApiKey("mapzen-HDGPF6m");

        MapView mapview = (MapView) map.findViewById(R.id.fragment_map);
        initMapButtons(mapview);
        mapview.getMapAsync(new BubbleWrapStyle(), this);

        fab = (FloatingActionButton) map.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);
        router = new MapzenRouter(this.getContext());
        router.setCallback(this);
        valhallaLocation = new ValhallaLocation();

        lostApiClient = new LostApiClient.Builder(this.getContext()).addConnectionCallbacks(this).build();
        lostApiClient.connect();

        engine = new RouteEngine();
        engine.setListener(this);

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

            if (enableLocationOnResume) {
                map.setMyLocationEnabled(true);
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
            lostApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(MapzenMap mapzenMap) {
        //set some initial configs
        mapzenMap.setCameraType(CameraType.FLAT);
        //determine if location is allowed, if so, display current location button.
        mListener.mapIsReady();
        if(permissionsvalid) {

            mapzenMap.setMyLocationEnabled(true);
//            mapzenMap.setCompassButtonEnabled(true);
//            mapzenMap.setZoomButtonsEnabled(true);
            enableLocationOnResume = true;
        }
        if(!(searchedLocation == null)){
            mapzenMap.drawSearchResult(searchedLocation);
        }
        //set the current instance of the map to this "READY" map
        //allows access to it throughout the current fragment instance.
        MapFragment.this.map = mapzenMap;
        //map.drawSearchResult(new LngLat(-118.026126,34.570467));
    }

    /*
    * This method initializes the findme, zoom, and compass buttons.
    * also sets their layout parameters.
    * */
    private void initMapButtons(MapView mapview){
        ViewGroup.MarginLayoutParams source;
        RelativeLayout.LayoutParams params;
        //init the findme button.
        findme = mapview.getFindMe();
        findme.setBackgroundResource(R.drawable.find_me_fab);
        findme.setImageResource(R.drawable.ic_find_me_dark);
        findme.getLocationInWindow(findmelocation);
        source  = new RelativeLayout.LayoutParams(findme.getLayoutParams().width, findme.getLayoutParams().height);
        params = new RelativeLayout.LayoutParams(source);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        findme.setLayoutParams(params);
        mapview.showFindMe();
        //init compass..need to fix. Not priority :P
//        compass = mapview.getCompass();
//        source  = new RelativeLayout.LayoutParams(compass.getLayoutParams().width, compass.getLayoutParams().height);
//        params = new RelativeLayout.LayoutParams(source);
//        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//        params.addRule(RelativeLayout.BELOW, R.id.standard_toolbar);
//        compass.setLayoutParams(params);
//        mapview.showCompass();
    }

    public void displayPoint(LngLat destpoint) {
        dest = destpoint;
        searchedLocation = destpoint;
        //map.drawSearchResult(destpoint);
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
        void passPoint(LngLat dest);
        void mapIsReady();
        //void generateRoute(LatLng start, LatLng dest);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    private void generateRoute(double[] startpoint, LngLat destpoint){

        if(!(destpoint == null) && !(map == null) ) {
            double[] dest = {destpoint.longitude, destpoint.latitude};
            router.setLocation(startpoint);
            router.setLocation(dest);
            router.fetch();
        }

    }

    @Override
    public void onClick(View view) {
        double[] userLoc = null;
        int permissionCheck = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Location loc = LocationServices.FusedLocationApi.getLastLocation(lostApiClient);
            if(loc != null ) {
                userLoc = new double[]{loc.getLongitude(), loc.getAltitude()};
            }
        }
        if (dest != null) {
            generateRoute(userLoc, dest);
        }
    }

    @Override
    public void success(@NotNull Route route) {

        Toast.makeText(this.getContext(), "sweetq", Toast.LENGTH_SHORT).show();
        map.clearRouteLine();
        map.removePolyline();
        List<LngLat> coordinates = new ArrayList<>();
        for (ValhallaLocation location : route.getGeometry()) {
            coordinates.add(new LngLat(location.getLongitude(), location.getLatitude()));
        }
        Polyline polyline = new Polyline(coordinates);
        map.addPolyline(polyline);
        currentroute = route;
        engine.setRoute(route);
        distance = (double) route.getTotalDistance();
        distance = distance / 1609;
        Toast.makeText(this.getContext(), "Distance: " + (int) distance + "mi", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void failure(int i) {
        Log.w("could not be fetched", "" + i);
        map.clearRouteLine();
    }

    @Override
    public void onRouteStart() {
        Toast.makeText(this.getContext(), "Route starting", Toast.LENGTH_SHORT).show();
        if(currentroute != null){
            Log.w("current route", "" + currentroute.getRouteInstructions());
        }
    }

    @Override
    public void onRecalculate(ValhallaLocation location) {
        //map.clearRouteLine();
        //map.removePolyline();
        //this.setStart(new LngLat(location.getLongitude(), location.getLatitude()));
        //this.setDestination(this.destination);
        //this.fetch();
        Toast.makeText(this.getContext(), "Recalculating", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSnapLocation(ValhallaLocation originalLocation, ValhallaLocation snapLocation) {
        map.setPosition(new LngLat(snapLocation.getLongitude(), snapLocation.getLatitude()));

    }

    @Override
    public void onMilestoneReached(int index, RouteEngine.Milestone milestone) {
        String instruction = currentroute.getRouteInstructions().get(index).toString();
        Toast.makeText(this.getContext(), instruction, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onApproachInstruction(int index) {

    }

    @Override
    public void onInstructionComplete(int index) {

    }

    @Override
    public void onUpdateDistance(int distanceToNextInstruction, int distanceToDestination) {

    }

    @Override
    public void onRouteComplete() {

    }

    //Lost api connection
    @Override
    public void onConnected() {

    }

    @Override
    public void onConnectionSuspended() {

    }

    @Override
    public void onResponse(Call call, Response response) {

    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }
}
