package com.zxdmjr.placefinder.map.nearby_places;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zxdmjr.placefinder.Adapters.CategoryAdapter;
import com.zxdmjr.placefinder.R;
import com.zxdmjr.placefinder.RecyclerItemClickListener;
import com.zxdmjr.placefinder.custom_activity.CustomActivity;
import com.zxdmjr.placefinder.map.ConnectionDetector;
import com.zxdmjr.placefinder.map.HttpConnection;
import com.zxdmjr.placefinder.map.MapsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NearbyPlaces extends CustomActivity
{
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 1000;

    PlaceDetails placeDetails;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    Boolean isGPSEnabled = false;
    ProgressDialog progressDialog;
    ArrayList<String> arrayList;

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;

    String bundleString;
    String placeType;
    LocationManager locationManager;

    List<PlaceDetails> placeDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_places_activity);

        Bundle gotBundle = getIntent().getExtras();
        bundleString = gotBundle.getString("name");
        //Change the actionbar title
        getSupportActionBar().setTitle(bundleString);

        placeDetails = new PlaceDetails();

        arrayList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(placeDetailsList);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        latitude = gotBundle.getDouble("AllTabLat");
        longitude = gotBundle.getDouble("AllTabLng");
        Log.e("Got All Tab Location", String.valueOf(latitude));
        Log.e("Got All Tab Location", String.valueOf(longitude));


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //Method calling
        selectedCategory(bundleString);

    }

    private void selectedCategory(String category)
    {
        //Show categories from bundle instance
        switch (category)
        {
            case "Bank":
                placeType  = "bank";
                getPlace(placeType);
                break;

            case "ATM":
                placeType  = "atm";
                getPlace(placeType);
                break;

            case "Restaurant":
                placeType  = "restaurant";
                getPlace(placeType);
                break;

            case "Shopping Mall":
                placeType  = "shopping_mall";
                getPlace(placeType);
                break;

            case "Hotel":
                placeType  = "hotel";
                getPlaceByText(placeType);
                break;

            case "Mosque":
                placeType  = "mosque";
                getPlace(placeType);
                break;

            case "Beauty Salon":
                placeType  = "beauty_salon";
                getPlace(placeType);
                break;

            case "Gymnasium":
                placeType  = "gym";
                getPlace(placeType);
                break;

            case "Bar":
                placeType  = "bar";
                getPlace(placeType);
                break;

            case "Money Exchange":
                placeType  = "moneyexchange";
                getPlaceByText(placeType);
                break;

            case "Car Repair":
                placeType  = "car_repair";
                getPlace(placeType);
                break;

            case "Hospital":
                placeType  = "hospital";
                getPlace(placeType);
                break;

            case "Gas Station":
                placeType  = "gas_station";
                getPlace(placeType);
                break;

            case "Bus Stand":
                placeType  = "bus_station";
                getPlace(placeType);
                break;


            case "Police Station":
                placeType  = "police";
                getPlace(placeType);
                break;

            case "Post Office":
                placeType  = "post_office";
                getPlace(placeType);
                break;

            case "Airport":
                placeType  = "airport";
                getPlaceByText(placeType);
                break;

            case "University":
                placeType = "university";
                getPlace(placeType);
                break;

            case "Train Station":
                placeType = "train_station";
                getPlace(placeType);
                break;

            case "Pharmacy":
                placeType = "pharmacy";
                getPlace(placeType);
                break;

            case "Parking":
                placeType = "parking";
                getPlace(placeType);
                break;

            case "Park":
                placeType = "amusement_park";
                getPlace(placeType);
                break;

            case "Taxi Stand":
                placeType = "taxi_stand";
                getPlace(placeType);
                break;

            case "Travel Agency":
                placeType = "travel_agency";
                getPlace(placeType);
                break;

            case "Real Estate Agency":
                placeType = "real_estate_agency";
                getPlace(placeType);
                break;

            case "Jewelry Store":
                placeType = "jewelry_store";
                getPlace(placeType);
                break;

            case "Govt. Office":
                placeType = "local_government_office";
                getPlace(placeType);
                break;

            case "Bicycle Store":
                placeType = "bicycle_store";
                getPlace(placeType);
                break;

            case "Cafe":
                placeType = "cafe";
                getPlace(placeType);
                break;

            case "Book Store":
                placeType = "book_store";
                getPlace(placeType);
                break;

        }
    }

    private void getPlace(String pType)
    {
        Log.e("showing", pType);
        String url = getUrl(latitude, longitude, pType);

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(url);
        Toast.makeText(NearbyPlaces.this,"Showing Nearby " + bundleString, Toast.LENGTH_LONG).show();
    }

    private void getPlaceByText(String pType)
    {
        Log.e("showing", pType);
        String url = getTextUrl(latitude, longitude, pType);

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(url);
        Toast.makeText(NearbyPlaces.this,"Showing Nearby " + bundleString, Toast.LENGTH_LONG).show();
    }

    private String getTextUrl(double latitude, double longitude, String nearbyPlace)
    {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&rankby=distance");
        //googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&query=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAPbwF0xmnhjzAjkIEVTqDr7PF5CvAhk90");
        Log.e("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }


    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        //googlePlacesUrl.append("&rankby=distance");
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAPbwF0xmnhjzAjkIEVTqDr7PF5CvAhk90");
        Log.e("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private class GetNearbyPlacesData extends AsyncTask<String, String, String> {

        String googlePlacesData;
        String url;

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.e("GetNearbyPlacesData", "doInBackground entered");
                url = params[0];
                HttpConnection downloadUrl = new HttpConnection();
                googlePlacesData = downloadUrl.readUrl(url);
                Log.e("GooglePlacesReadTask", "doInBackground Exit");
            }
            catch (Exception e)
            {
                Log.e("GooglePlacesReadTask", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(NearbyPlaces.this, null, "Loading nearby " + bundleString + "...");
        }

        @Override
        protected void onPostExecute(String result) {

            if (progressDialog != null)
            {
                progressDialog.dismiss();
            }
            Log.e("GooglePlacesReadTask", "onPostExecute Entered");

            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList =  dataParser.parse(result);
			//////////////////////////Add message when no suggesitons found. There are no nearby bundleString/////////////////////////////////////////////
            //Method calling
            showNearbyPlaces(nearbyPlacesList);
            Log.e("GooglePlacesReadTask", "onPostExecute Exit");
        }

        private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList)
        {
            for (int i = 0; i < nearbyPlacesList.size(); i++)
            {
                Log.e("onPostExecute","Entered into showing locations");
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);

                double lat = Double.parseDouble(googlePlace.get("lat"));

                double lng = Double.parseDouble(googlePlace.get("lng"));

                String placeName = googlePlace.get("place_name");

                String vicinity = googlePlace.get("vicinity");

                String formatted_address = googlePlace.get("formatted_address");

				//////////////////////////Add message when no suggesitons found. There are no nearby bundleString/////////////////////////////////////////////
				
                placeDetails = new PlaceDetails(placeName, vicinity, lat, lng, formatted_address);
                placeDetailsList.add(placeDetails);
                categoryAdapter.notifyDataSetChanged();
            }

            //Recycler view
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(categoryAdapter);   // Add Category Adapter

            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener(){
                @Override
                public void onItemClick(View view, int position) {

                    placeDetails = placeDetailsList.get(position);
                    String placeName = placeDetails.getName();
                    Log.e("Single place name.", placeName);
                    String address = placeDetails.getFormatted_address();
                    String vicinity = placeDetails.getVicinity();
                    Log.e("Formatted Address..", address);

                    double lat = placeDetails.getLat();
                    double lng = placeDetails.getLng();
                    String s = "Grid";

                    Intent goMap = new Intent(NearbyPlaces.this, MapsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("place_name", placeName);
                    bundle.putString("address", address);
                    bundle.putString("vicinity_address", vicinity);
                    bundle.putString("Grid", s);
                    bundle.putDouble("lat", lat);
                    bundle.putDouble("lng", lng);
                    bundle.putDouble("TabLat", latitude);
                    bundle.putDouble("TabLng", longitude);
                    goMap.putExtras(bundle);
                    startActivity(goMap);
                }
            }));
        }
    }
}
