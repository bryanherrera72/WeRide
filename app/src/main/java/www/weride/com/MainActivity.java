package www.weride.com;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapzen.tangram.LngLat;
import com.weride.www.awsmobilehelper.auth.IdentityManager;
import com.weride.www.awsmobilehelper.auth.IdentityProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import www.weride.com.amazonaws.mobile.AWSMobileClient;
import www.weride.com.fragments.GroupFragment;
import www.weride.com.fragments.MapFragment;
import www.weride.com.fragments.SearchFragment;
import www.weride.com.utils.PushListenerService;
import www.weride.com.utils.SignInHandler;

public class MainActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener,
                                                                GroupFragment.OnFragmentInteractionListener,
                                                                SearchFragment.OnFragmentInteractionListener,
        View.OnClickListener{

    /** Class name for log messages. */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public DrawerLayout mainDrawer;
    private Toolbar toolbar, standardtoolbar;
    private  NavigationView navDrawer;
    private FragmentManager fragmentManager;
    public ActionBarDrawerToggle drawerToggle;
    private MapFragment map;
    private LocationManager lm;
    private boolean locationaccess = false;
    private Button signInButton;
    private Button signOutButton;
    private ImageView imageView;
    private TextView userNameView;
    /** The identity manager used to keep track of the current user account. */
    private IdentityManager identityManager;


    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        checkPermissions();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        standardtoolbar = (Toolbar) findViewById(R.id.standard_toolbar);
        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);
        //prepare and set toolbar

        // Obtain a reference to the mobile client. It is created in the Application class.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // Obtain a reference to the identity manager.
        identityManager = awsMobileClient.getIdentityManager();


        //set the drawer layout inside the main layout
        mainDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawer = (NavigationView) findViewById(R.id.navView);

        setupDrawerContent(navDrawer);
        swapToMapToolbar();
        //have to force the view to initialize
        setupSignInButtons(navDrawer.getHeaderView(0));

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
    /**
     * Initializes the sign-in and sign-out buttons.
     */
    private void setupSignInButtons(View view) {

        signOutButton = (Button)view.findViewById(R.id.button_signout);
        signOutButton.setOnClickListener(this);

        signInButton = (Button) view.findViewById(R.id.button_signin);
        signInButton.setOnClickListener(this);

        final boolean isUserSignedIn = identityManager.isUserSignedIn();
        signOutButton.setVisibility(isUserSignedIn ? View.VISIBLE : View.INVISIBLE);
        signInButton.setVisibility(!isUserSignedIn ? View.VISIBLE : View.INVISIBLE);

    }

    //assign a drawer toggle to a toolbar parameter.
    private ActionBarDrawerToggle setupDrawerToggle(Toolbar toolbar){
        return new ActionBarDrawerToggle(this, mainDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
// {
//            @Override
//            public void syncState(){
//                super.syncState();
//                updateUserName();
//                updateUserImage();
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView){
//                super.onDrawerOpened(drawerView);
//                updateUserName();
//                updateUserImage();
//            }
//        };

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
        //if(MapFragment.class ==fragmentManager.findFragmentById(R.id.flContent).getClass()){
          //  toolbar.removeView(findViewById(R.id.search_toolbar));

            swapToMapToolbar();
        //}
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
        setupSignInButtons(navDrawer.getHeaderView(0));
        // register notification receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver,
                new IntentFilter(PushListenerService.ACTION_SNS_NOTIFICATION));
        // register settings changed receiver.


        onPermissionsValid(canAccessLocation());
    }

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "Received notification from local broadcast. Display it in a dialog.");

            Bundle data = intent.getBundleExtra(PushListenerService.INTENT_SNS_NOTIFICATION_DATA);
            String message = PushListenerService.getMessage(data);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.push_demo_title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    };

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
        //first check if the child view of the toolbar is set
        if(!(findViewById(R.id.search) == null)){
            toolbar.removeView(findViewById(R.id.search_toolbar));
        }
        //show the main cardview toolbar
        toolbar.setVisibility(View.VISIBLE);
        View cardview = mainlayout.findViewById(R.id.toolbar_card);
        cardview.setVisibility(View.VISIBLE);
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
    public void passPoint(LngLat dest) {
        if(!(map == null)){
            swapToMapToolbar();
            //check if the map is up front.
            if(!(map.isVisible())){
//                if its not, place it up front
                fragmentManager.beginTransaction().replace(R.id.flContent, map).commit();
            }
            if(!(dest == null)){
                map.displayPoint(dest);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // unregister notification receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);

    }


    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    public void onClick(View view) {
        if (view == signOutButton) {
            // The user is currently signed in with a provider. Sign out of that provider.
            identityManager.signOut();
            // Show the sign-in button and hide the sign-out button.
            signOutButton.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.VISIBLE);

            // Close the navigation drawer.
            mainDrawer.closeDrawers();
            return;
        }
        if (view == signInButton) {
            identityManager.signInOrSignUp(this, new SignInHandler());

            // Close the navigation drawer.
            mainDrawer.closeDrawers();
            return;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        if(standardtoolbar!=null){
            bundle.putCharSequence("Title", standardtoolbar.getTitle());
        }
    }

    private void updateUserName() {
        final IdentityManager identityManager =
                AWSMobileClient.defaultMobileClient().getIdentityManager();
        final IdentityProvider identityProvider =
                identityManager.getCurrentIdentityProvider();

         userNameView = (TextView)findViewById(R.id.userName);

        if (identityProvider == null) {
            // Not signed in
            userNameView.setText(getString(R.string.main_nav_menu_default_user_text));
            userNameView.setBackgroundColor(getResources().getColor(R.color.nav_drawer_no_user_background));
            return;
        }

        final String userName = identityProvider.getUserName();

        if (userName != null) {
            userNameView.setText(userName);
            userNameView.setBackgroundColor(
                    getResources().getColor(R.color.nav_drawer_top_background));
        }
    }

    private void updateUserImage() {

        final IdentityManager identityManager =
                AWSMobileClient.defaultMobileClient().getIdentityManager();
        final IdentityProvider identityProvider =
                identityManager.getCurrentIdentityProvider();

         imageView = (ImageView)findViewById(R.id.userImage);

        if (identityProvider == null) {
            // Not signed in
            if (Build.VERSION.SDK_INT < 22) {
                imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_account));
            }
            else {
                imageView.setImageDrawable(getDrawable(R.drawable.ic_account));
            }

            return;
        }

        final Bitmap userImage = identityManager.getUserImage();
        if (userImage != null) {
            imageView.setImageBitmap(userImage);
        }
    }
}
