package com.zxdmjr.placefinder.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zxdmjr.placefinder.R;
import com.zxdmjr.placefinder.custom_activity.CustomActivity;

public class MapsActivity extends CustomActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location lastLocation;
    Marker currentLocationMarker;

    LatLng currentLocation;
    LatLng destination;
    String destinationName, address, formatted_address, searchName, searchAddress;
    GoogleMap mMap;

    double mLatitude = 0, searchLat = 0;
    double mLongitude = 0, searchLng = 0;

    TextView distanceDuration;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Getting data from others Activity
        Bundle bundle = getIntent().getExtras();
        destinationName = bundle.getString("place_name");   //Place name for title
        String search = bundle.getString("SearchCase"); //Auto search key
        String grid = bundle.getString("Grid"); //Grid select key
        String savedTab = bundle.getString("savedTab"); //Saved tab key
        searchName = bundle.getString("SearchPlaceName");    //Auto search palce name
        String savedPlaceName = bundle.getString("pn");     //Saved place name

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Set title by different approach
        if (searchName != null)
        {
            destinationName = searchName;
            actionBar.setTitle(destinationName);
        }
        else if(savedTab != null)
        {
            destinationName = savedPlaceName;
            actionBar.setTitle(destinationName);
        }
        else
        {
            actionBar.setTitle(destinationName);
        }

        //Set the permission for android M
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        distanceDuration = (TextView) findViewById(R.id.distanceDuration);

        //Getting data from others Activity
        address = bundle.getString("vicinity_address");
        formatted_address = bundle.getString("address");

        Double lat = bundle.getDouble("lat"); //Destination data
        Double lng = bundle.getDouble("lng");

        Double tabLat = bundle.getDouble("TabLat"); //Current location data
        Double tabLng = bundle.getDouble("TabLng");

        Double fragmentLat = bundle.getDouble("CurrentLat"); //Search current location data
        Double fragmentLng = bundle.getDouble("CurrentLng");

        //Saved tab location data
        double saveTabLat = bundle.getDouble("sLat");
        double saveTabLng = bundle.getDouble("sLng");
        double currentLat = bundle.getDouble("cLat");
        double currentLng = bundle.getDouble("cLng");

        //Search data
        searchAddress = bundle.getString("searchAddress");
        searchLat = bundle.getDouble("SearchLat"); //Search destination data
        searchLng = bundle.getDouble("SearchLng");

        //Set the current and destination LatLang
        if (search != null)
        {
            currentLocation = new LatLng(fragmentLat, fragmentLng);
            destination = new LatLng(searchLat, searchLng);
        }
        else if (grid != null)
        {
            currentLocation = new LatLng(tabLat, tabLng);
            destination = new LatLng(lat, lng);
        }
        else if (savedTab != null)
        {
            destination = new LatLng(saveTabLat, saveTabLng);
            currentLocation = new LatLng(currentLat,currentLng);
        }


        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9810707576316287~3547660052");
        AdView mAdView = (AdView) findViewById(R.id.ad_View);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (mMap != null)
        {
            mMap.clear();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();
                googleApiClient.connect();
                mMap.setMyLocationEnabled(true);
            }
        }
        else
        {
            buildGoogleApiClient();
            googleApiClient.connect();
            mMap.setMyLocationEnabled(true);
        }

        MarkerOptions options = new MarkerOptions();
        options.position(currentLocation);
        options.position(destination).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(destinationName);
        mMap.addMarker(options);
        String url = getMapsApiDirectionsUrl(currentLocation, destination);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        //LatLngBound to add multiple location
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(currentLocation);
        builder.include(destination);
        LatLngBounds bounds = builder.build();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width - ((int) (((double) width) * 0.5d)), height - ((int) (((double) height) * 0.5d)), 10));
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private String getMapsApiDirectionsUrl(LatLng src, LatLng dest)
    {
        String origin = "origin=" + src.latitude + "," + src.longitude;

        String destination = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";

        String params = origin + "&" + destination + "&" + sensor;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;

        Log.e("Direction url", url);
        return url;
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null)
        {
            //Getting longitude and latitude
            mLatitude = lastLocation.getLatitude();
            mLongitude = lastLocation.getLongitude();
            Log.e("Current Location..", String.valueOf(mLatitude));
            Log.e("Current Location..", String.valueOf(mLongitude));

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        getCurrentLocation();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;

        if (currentLocationMarker != null)
        {
            currentLocationMarker.remove();
        }

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null)
        {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(googleApiClient != null && googleApiClient.isConnected())
        {
            googleApiClient.disconnect();
        }
    }

    private class ReadTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... url)
        {
            String data = "";
            try
            {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            }
            catch (Exception e)
            {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
    {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData)
        {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try
            {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MapsActivity.this, null, "Getting directions...");
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes)
        {
            if (progressDialog != null)
            {
                progressDialog.dismiss();

            }

            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;
            String distance = "";
            String duration = "";

            //Traversing through routes
            for (int i = 0; i < routes.size(); i++)
            {
                points = new ArrayList<>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++)
                {
                    HashMap<String, String> point = path.get(j);

                    if(j==0)
                    {   // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }
                    else if(j==1)
                    {   // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);

            String textLength = "Distance: " + distance + ", Duration: " + duration;
            Log.e("textLength", String.valueOf(textLength.length()));
            if (textLength.length() > 35)
            {
                distanceDuration.setTextSize(15);
                distanceDuration.setText(textLength);
            }
            else
            {
                distanceDuration.setText("Distance: " + distance + ", Duration: " + duration);
            }

        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_fav:

                SharedPreferences sharedPreferences = getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("p_name", destinationName);
                edit.putString("s_data", address);
                edit.putString("f_data", formatted_address);
                double latitude = destination.latitude;
                double longitude = destination.longitude;
                edit.putString("lat_data", String.valueOf(latitude));
                edit.putString("lng_data", String.valueOf(longitude));

                //Search data
                if (searchName != null)
                {
                    edit.putString("search_address", searchAddress);
                }

                edit.commit();
                Toast.makeText(MapsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
