package www.weride.com.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mapzen.android.graphics.MapzenMapPeliasLocationProvider;
import com.mapzen.android.search.MapzenSearch;
import com.mapzen.pelias.Pelias;
import com.mapzen.pelias.gson.Feature;
import com.mapzen.pelias.gson.Result;
import com.mapzen.pelias.widget.AutoCompleteAdapter;
import com.mapzen.pelias.widget.AutoCompleteListView;
import com.mapzen.pelias.widget.PeliasSearchView;
import com.mapzen.tangram.LngLat;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import www.weride.com.R;
import www.weride.com.fragments.MapFragment;

public class SearchActivity extends AppCompatActivity implements Callback<Result>, MapFragment.OnFragmentInteractionListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private Toolbar searchbar;
    private AutoCompleteListView listView;
    Pelias pelias;
    PeliasSearchView peliasSearchView;
    MapzenMapPeliasLocationProvider peliasLocationProvider;
    MapFragment map;
    double doubleLng = 0;
    double doubleLat = 0;
    private LngLat destpoint;
    boolean navselected = false;
    MapzenSearch mapzenSearch;
    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchbar = (Toolbar) findViewById(R.id.search_bar);
        setSupportActionBar(searchbar);

        pelias = new Pelias();
        peliasLocationProvider = new MapzenMapPeliasLocationProvider(this);
        pelias.setLocationProvider(peliasLocationProvider);
        //peliasLocationProvider.setMapzenMap(map);

        listView = (AutoCompleteListView) findViewById(R.id.list_view);
        AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(autoCompleteAdapter);

        peliasSearchView = new PeliasSearchView(this);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(peliasSearchView, lp);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        setupPeliasSearchView(peliasSearchView);

        fragmentManager = getSupportFragmentManager();
        try{
            map =  MapFragment.class.newInstance();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().add(R.id.flContent1, map).commit();

    }

    private void setupPeliasSearchView (PeliasSearchView searchView) {
        searchView.setAutoCompleteListView(listView);
        searchView.setPelias(pelias);
        searchView.setCallback(this);
        searchView.setIconified(false);
        searchView.setQueryHint("Search");
    }

    @Override
    public void success(Result result, Response response) {
        //LngLat destpoint = null;
        for (Feature feature : result.getFeatures()) {
            List<Double> coordinates = feature.geometry.coordinates;
            doubleLng = coordinates.get(0);
            doubleLat = coordinates.get(1);
            destpoint = new LngLat(doubleLng, doubleLat);
            something(destpoint);
//            map.drawSearchResult(destpoint);
//            map.setPosition(new LngLat(doubleLng, doubleLat));
//            map.setZoom(15);

        }
        Toast.makeText(this, "It works", Toast.LENGTH_SHORT).show();
//        current = locationclient.getLastKnownLocation();
//        LngLat currentLngLat = new LngLat(current.getLongitude(), current.getLatitude());
        //build a route once we got the destination.


    }

    @Override
    public void failure(RetrofitError error) {

        navselected = false;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPermissionsValid(boolean valid) {

    }

    @Override
    public void something(LngLat destpoint) {
        MapFragment mapfrag = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.flContent1);
        if(destpoint != null) {
            mapfrag.displayPoint(destpoint);
        }

    }
}
