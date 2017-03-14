package www.weride.com;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.mapzen.tangram.LngLat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import www.weride.com.fragments.GroupFragment;
import www.weride.com.fragments.MapFragment;
import www.weride.com.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener,
                                                                GroupFragment.OnFragmentInteractionListener,
                                                                SearchFragment.OnFragmentInteractionListener{
    public DrawerLayout mainDrawer;
    private Toolbar toolbar, standardtoolbar;
    private  NavigationView navDrawer;

    private FragmentManager fragmentManager;
    public ActionBarDrawerToggle drawerToggle;
    private MapFragment map;

    private LocationManager lm;
    private boolean locationaccess = false;


    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.activity_main);
        //prepare and set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        standardtoolbar = (Toolbar) findViewById(R.id.standard_toolbar);

        //set the drawer layout inside the main layout
        mainDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawer = (NavigationView) findViewById(R.id.navView);
        setupDrawerContent(navDrawer);
        swapToMapToolbar();

        //set the first fragment.
        fragmentManager = getSupportFragmentManager();
        try{
            map =  MapFragment.class.newInstance();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().add(R.id.flContent, map).commit();
    }

    //assign a drawer toggle to a toolbar parameter.
    private ActionBarDrawerToggle setupDrawerToggle(Toolbar toolbar){
        return new ActionBarDrawerToggle(this, mainDrawer,toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    //Setup the drawer content within the nav view
    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }


        });
    }
    public void selectDrawerItem(MenuItem item){
        Fragment frag = null;
        Class fragmentClass;
        switch(item.getItemId()){
            //map is visible
            case R.id.map_fragment:
                fragmentClass = MapFragment.class;
                swapToMapToolbar();
                break;
            //group fragment, shows list of groups + option to create
            case R.id.group_fragment:
                fragmentClass = GroupFragment.class;
                swapToFragmentToolbar();
                break;
            default:
                fragmentClass = MapFragment.class;
        }
        try{
            //This ensures that we're getting an instance of a map fragment set, if that's what we need.
            if(item.getItemId() == R.id.map_fragment){
                frag = map;
            }
            else {
                frag = (Fragment) fragmentClass.newInstance();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, frag).commit();
        //set the nav drawer item as checked and close it.
        item.setChecked(true);
        setTitle(item.getTitle());
        mainDrawer.closeDrawers();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        //resync the drawer toggle.
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(MapFragment.class ==fragmentManager.findFragmentById(R.id.flContent).getClass()){
            toolbar.removeView(findViewById(R.id.search_toolbar));
            swapToMapToolbar();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        //This changes drawer toggle depending on orientation
        drawerToggle.onConfigurationChanged(newConfig);
    }
    //inflate the menu that is shown in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // thats the only one getting search icon.
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Class fragmentClass= null;
        SearchFragment frag = null;
        switch(item.getItemId()){
            //hamburger was clicked
            case android.R.id.home:
                mainDrawer.openDrawer(GravityCompat.START);
                break;
            //search button was clicked
            case R.id.search:
                fragmentClass = SearchFragment.class;
                break;
        }
        if(!(fragmentClass == null)){
            try {
                frag = (SearchFragment) fragmentClass.newInstance();

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            fragmentManager.beginTransaction().replace(R.id.flContent, frag).addToBackStack("MainMapBackStack").commit();
        }
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();
        //if we have a mapfragment, ensure the permissions are prepped.
        //MapFragment frag = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        onPermissionsValid(canAccessLocation());
    }

    public Toolbar getToolbar(){
        Toolbar rtn;
        if(toolbar.isShown()){
            rtn = toolbar;
        }
        else{
            rtn = standardtoolbar;
        }
        return rtn;
    }

    //Change the toolbar to the cardview toolbar
    private void swapToMapToolbar(){
        RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.activity_main);
        //show the main cardview toolbar
        toolbar.setVisibility(View.VISIBLE);
        View cardview = mainlayout.findViewById(R.id.toolbar_card);
        cardview.setVisibility(View.VISIBLE);
        if(toolbar.getChildCount() == 0){}
        standardtoolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        drawerToggle = setupDrawerToggle(toolbar);
        mainDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
    //Change the toolbar to standard toolbar.
    private void swapToFragmentToolbar(){
        RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.activity_main);
        //hide the main cardview toolbar
        View maintoolbar = mainlayout.findViewById(R.id.toolbar);
        maintoolbar.setVisibility(View.GONE);
        View cardview = mainlayout.findViewById(R.id.toolbar_card);
        cardview.setVisibility(View.GONE);

        standardtoolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(standardtoolbar);
        drawerToggle = setupDrawerToggle(standardtoolbar);
        mainDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private boolean canAccessLocation(){
        boolean enabled = false;
        try{
            enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception e){
            e.printStackTrace();
        }
        //return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) || hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
        return enabled;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean hasPermission(String perm){
        return(PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));

    }

    //check for permissions and if they are not there, request them.
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);

        }
    }

    //this method checks the permissions and tells the mapfragment whether or not location is allowed
    @Override
    public void onPermissionsValid(boolean valid){
        MapFragment mapfrag = null;
        if(MapFragment.class ==  getSupportFragmentManager().findFragmentById(R.id.flContent).getClass()) {
            mapfrag = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        }
        if(mapfrag != null){
            mapfrag.setPermissionsvalid(valid);
        }
    }

    @Override
    public void mapIsReady(){
        onPermissionsValid(canAccessLocation());
    }
    @Override
    public void something(LngLat dest) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



}
