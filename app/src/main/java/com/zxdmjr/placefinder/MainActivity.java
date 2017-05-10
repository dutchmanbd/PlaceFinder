package com.zxdmjr.placefinder;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.zxdmjr.placefinder.custom_activity.CustomActivity;
import com.zxdmjr.placefinder.map.ConnectionDetector;
import com.zxdmjr.placefinder.map.MapsActivity;
import com.zxdmjr.placefinder.nav_drawer.AboutUs;
import com.zxdmjr.placefinder.nav_drawer.Home;
import com.zxdmjr.placefinder.tabitems.AllTab;

public class MainActivity extends CustomActivity
        implements NavigationView.OnNavigationItemSelectedListener, AllTab.OnLocationDataPass {


    FragmentTransaction fragmentTransaction;
    CoordinatorLayout coordinatorLayout;

    DrawerLayout drawer;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    double fragmentLatitude, fragmentLongitude;

    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    boolean isGPSEnabled = false;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String google_map_key = this.getResources().getString(R.string.google_maps_key);
        String banner_ad_unit_id = this.getResources().getString(R.string.banner_ad_unit_id);

        if(google_map_key.startsWith("Write your") || banner_ad_unit_id.startsWith("Write your")){

            showAlertMessage(google_map_key, banner_ad_unit_id);

        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set up home fragment
        setUpHomeFragment();

        //Check internet connection and GPS
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);





    }


    public void showAlertMessage(String map_key, String banner_id){

        //when it is not valid map key and banner id
        View view = getLayoutInflater().inflate(R.layout.update_dialog_alert, null);
        TextView tvUpdateAlert = (TextView) view.findViewById(R.id.tvUpdateAlert);

        tvUpdateAlert.setText(map_key+"\n\n\n"+banner_id);

        TextView title = new TextView(this);
        title.setText("UPDATE YOUR CODE");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(this.getResources().getColor(R.color.redTextColor));
        title.setTextSize(25);


        new AlertDialog.Builder(this)
                .setTitle("Update your code")
                .setCustomTitle(title)
                //.setMessage(map_key+"\n"+banner_id)
                .setView(view)
                .setCancelable(false)
                .show();



    }

    private void setUpHomeFragment()
    {
        //Home fragment view
        Home home = new Home();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_view, home, "homeFragment").commit();
        getSupportActionBar().setTitle(R.string.app_name);
    }

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    @Override
    public void onBackPressed()
    {
        Home homeFragment = (Home)getSupportFragmentManager().findFragmentByTag("homeFragment");
        AboutUs aboutUsFragment = (AboutUs)getSupportFragmentManager().findFragmentByTag("aboutUsFragment");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (homeFragment != null && homeFragment.isVisible())
        {
            if (back_pressed + TIME_DELAY > System.currentTimeMillis())
            {
                super.onBackPressed();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
        else if (aboutUsFragment != null && aboutUsFragment.isVisible())
        {
            setUpHomeFragment();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.home:
                setUpHomeFragment();
                break;

            case R.id.about_us:
                AboutUs aboutUs = new AboutUs();
                fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view, aboutUs, "aboutUsFragment");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle(R.string.about_us);

                break;
            case R.id.moreApps:

                final String appPackageName = getApplicationContext().getPackageName();
                Log.e("packageName", appPackageName);
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://dutchmanplanet.blogspot.com/")));
                }
                catch (android.content.ActivityNotFoundException e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://dutchmanplanet.blogspot.com/")));
                }
                break;
        }
        item.setChecked(true);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_fav).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_search:

                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).
                            setBoundsBias(new LatLngBounds(new LatLng(23.6850, 90.3563), new LatLng(23.8103, 90.4125)))
                            .build(this);

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                }
                catch (GooglePlayServicesRepairableException e)
                {
                    e.printStackTrace();
                }
                catch (GooglePlayServicesNotAvailableException e)
                {
                    e.printStackTrace();
                }
                return true;

            case R.id.action_share:
                if (!isInternetPresent)
                {
                    // Internet Connection is not present
                    Snackbar.make(coordinatorLayout, "No internet connection available!", Snackbar.LENGTH_LONG).setAction("Enable", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    }).show();

                    return false;

                }
                if (!isGPSEnabled)
                {
                    Snackbar.make(coordinatorLayout, "GPS is not enabled.", Snackbar.LENGTH_LONG).setAction("Enable", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent goGPSSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(goGPSSettings);
                        }
                    }).show();
                    // stop executing code by return
                    return false;
                }
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                String sharedString = "http://maps.google.com/?q=" + fragmentLatitude + "," + fragmentLongitude;
                Log.e("Sharing Url", sharedString);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, sharedPreferences.getString("message_subject", "Message Subject"));
                shareIntent.putExtra(Intent.EXTRA_TEXT, sharedPreferences.getString("message_body", "Click link to see the map ") + sharedString);
                try
                {
                    startActivity(Intent.createChooser(shareIntent, "Choose One"));
                }
                catch (ActivityNotFoundException e)
                {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "Share your current location.", Toast.LENGTH_SHORT).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Search Place", place.getName().toString());

                LatLng latLng = place.getLatLng();
                double lat = latLng.latitude;
                double lng = latLng.longitude;

                Log.e("Search Lat..", String.valueOf(lat));
                Log.e("Search Lng..", String.valueOf(lng));

                Log.e("From Fragment", String.valueOf(fragmentLatitude));
                Log.e("From Fragment", String.valueOf(fragmentLongitude));

                String s = "Search";
                String placeName = place.getName().toString();
                String searchAddress = (String) place.getAddress();
                Log.e("Search Address", searchAddress);
                Bundle bundle = new Bundle();
                bundle.putString("SearchPlaceName", placeName);
                bundle.putString("searchAddress", searchAddress);
                bundle.putString("SearchCase", s);
                bundle.putDouble("SearchLat", lat);
                bundle.putDouble("SearchLng", lng);

                bundle.putDouble("CurrentLat", fragmentLatitude);
                bundle.putDouble("CurrentLng", fragmentLongitude);

                Intent goMap = new Intent(MainActivity.this, MapsActivity.class);
                goMap.putExtras(bundle);
                startActivity(goMap);
            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("Status", status.getStatusMessage());

            }
            else if (resultCode == RESULT_CANCELED)
            {
                //The user canceled the operation.
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Check after user get back from settings
        isInternetPresent = cd.isConnectingToInternet();
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void passLocationData(double l, double ln) {

        fragmentLatitude = l;
        fragmentLongitude = ln;

    }
}