package com.zxdmjr.placefinder.tabitems;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.zxdmjr.placefinder.Adapters.CategoryAdapter;
import com.zxdmjr.placefinder.R;
import com.zxdmjr.placefinder.RecyclerItemClickListener;
import com.zxdmjr.placefinder.custom_fragment.CustomFragment;
import com.zxdmjr.placefinder.map.ConnectionDetector;
import com.zxdmjr.placefinder.map.MapsActivity;
import com.zxdmjr.placefinder.map.nearby_places.PlaceDetails;

import java.util.ArrayList;
import java.util.List;

public class SavedTab extends CustomFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    List<PlaceDetails> placeDetailsList = new ArrayList<>();
    PlaceDetails placeDetails;
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;

    double aLatitude;
    double aLongitude;

    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    boolean isGPSEnabled = false;
    Bundle bundle;

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location lastLocation;
    LocationManager locationManager;

    public SavedTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        buildGoogleApiClient();
        googleApiClient.connect();

        bundle = new Bundle();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
        String placeName = sharedPreferences.getString("p_name", null);
        String vicinity = sharedPreferences.getString("s_data", null);
        String formatted_address = sharedPreferences.getString("f_data", null);
        String latitude = sharedPreferences.getString("lat_data", null);
        String longitude = sharedPreferences.getString("lng_data", null);
        String searchAddress = sharedPreferences.getString("search_address", null);

        if (placeName != null)
        {
            double lat = Double.parseDouble(latitude);
            double lng = Double.parseDouble(longitude);

            if (searchAddress != null)
            {
                vicinity = searchAddress;
            }
            placeDetails = new PlaceDetails(placeName, vicinity, lat, lng, formatted_address);
            placeDetailsList.add(placeDetails);
            categoryAdapter = new CategoryAdapter(placeDetailsList);
            categoryAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.saved_tab_fragment, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //Recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(savedAdapter);
        recyclerView.setAdapter(categoryAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (!isInternetPresent) {
                    Snackbar.make(view, "No internet connection available!", Snackbar.LENGTH_LONG).setAction("Enable", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    }).show();

                    return;
                }
                if (!isGPSEnabled) {
                    Snackbar.make(view, "GPS is not enabled.", Snackbar.LENGTH_LONG).setAction("Enable", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent goGPSSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(goGPSSettings);
                        }
                    }).show();
                    // stop executing code by return
                    return;
                }

                placeDetails = placeDetailsList.get(position);
                String placeName = placeDetails.getName();
                Log.e("Single place name.", placeName);
                String address = placeDetails.getFormatted_address();
                String vicinity = placeDetails.getVicinity();

                double lat = placeDetails.getLat();
                double lng = placeDetails.getLng();
                Log.e("doubleLat", String.valueOf(lat));
                Log.e("doubleLat", String.valueOf(lng));


                Intent goMap = new Intent(getActivity(), MapsActivity.class);

                String s = "savedTab";
                bundle.putString("savedTab", s);
                bundle.putString("pn", placeName);
                bundle.putString("ad", address);
                bundle.putString("vd", vicinity);
                bundle.putDouble("sLat", lat);
                bundle.putDouble("sLng", lng);

                //Current location data
                bundle.putDouble("cLat", aLatitude);
                bundle.putDouble("cLng", aLongitude);

                goMap.putExtras(bundle);

                startActivity(goMap);
            }
        }));

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //Check after user get back from settings
        isInternetPresent = cd.isConnectingToInternet();
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null)
        {
            aLatitude = lastLocation.getLatitude();
            aLongitude = lastLocation.getLongitude();
            Log.e("Save Tab location", String.valueOf(aLatitude));
            Log.e("Save Tab location", String.valueOf(aLongitude));

        }
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
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

        if (lastLocation != null)
        {
            aLatitude = lastLocation.getLatitude();
            aLongitude = lastLocation.getLongitude();
            Log.e("SaveTab change location", String.valueOf(aLatitude));
            Log.e("SaveTab change location", String.valueOf(aLongitude));
        }

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION))
            {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        else
        {
            return true;
        }
    }
}
