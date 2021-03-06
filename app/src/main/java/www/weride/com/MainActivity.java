package www.weride.com;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import www.weride.com.fragments.GroupFragment;
import www.weride.com.fragments.MapFragment;

public class MainActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener,
                                                                GroupFragment.OnFragmentInteractionListener{
    private DrawerLayout mainDrawer;
    private Toolbar toolbar;
    private NavigationView navDrawer;
    private AutoCompleteTextView aText;
    private FragmentManager fragmentManager;
    private ActionBarDrawerToggle drawerToggle;
    private MapFragment map;
    private static final int INITIAL_REQUEST= 1337;
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
//        if(!(canAccessLocation())){
//            grabPermissions(LOCATION_PERMS, INITIAL_REQUEST+3);
//        }
        setContentView(R.layout.activity_main);
        //prepare and set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set the drawer layout inside the main layout
        mainDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawer = (NavigationView) findViewById(R.id.navView);
        setupDrawerContent(navDrawer);
        drawerToggle = setupDrawerToggle();

        mainDrawer.addDrawerListener(drawerToggle);

        //set the first fragment.
        fragmentManager = getSupportFragmentManager();
        try{map =  MapFragment.class.newInstance();}
        catch(Exception e){
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.flContent, map).commit();
    }

    private ActionBarDrawerToggle setupDrawerToggle(){
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
            case R.id.map_fragment:
                fragmentClass = MapFragment.class;
                break;
            case R.id.group_fragment:
                fragmentClass = GroupFragment.class;
                break;
            default:
                fragmentClass = MapFragment.class;
        }
        try{
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

        item.setChecked(true);
        setTitle(item.getTitle());
        mainDrawer.closeDrawers();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                mainDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();

    }
    @TargetApi(Build.VERSION_CODES.M)
    private void grabPermissions(String[] perms, int request){
        requestPermissions(LOCATION_PERMS, INITIAL_REQUEST+3);
    }
    private boolean canAccessLocation(){
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean hasPermission(String perm){
        return(PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));

    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_COARSE_LOCATION};


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
}
