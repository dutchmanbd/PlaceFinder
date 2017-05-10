package com.zxdmjr.placefinder.tabitems;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.zxdmjr.placefinder.Adapters.GridAdapter;
import com.zxdmjr.placefinder.GridItem;
import com.zxdmjr.placefinder.R;
import com.zxdmjr.placefinder.custom_fragment.CustomFragment;
import com.zxdmjr.placefinder.map.ConnectionDetector;
import com.zxdmjr.placefinder.map.nearby_places.NearbyPlaces;

import java.util.ArrayList;


public class AllTab extends CustomFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GridView gridView;
    ArrayList<GridItem> gridItemArray = new ArrayList<>();
    GridAdapter gridAdapter;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    Bundle bundle;

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location lastLocation;

    double latitude;
    double longitude;

    boolean isGPSEnabled = false;

    LocationManager locationManager;

    OnLocationDataPass onDataPass;

    public AllTab() {
        // Required empty public constructor
    }

    //Calling a interface to pass data in host activity
    public interface OnLocationDataPass {
        void passLocationData(double l, double ln);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onDataPass = (OnLocationDataPass) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement passLocationData");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = new Bundle();

        cd = new ConnectionDetector(getActivity());

        isInternetPresent = cd.isConnectingToInternet();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        buildGoogleApiClient();
        googleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.all_tab_fragment, container, false);

        Bitmap bankIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_bank);
        Bitmap atmIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_local_atm);
        Bitmap shoppingMallIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_shopping_mall);
        Bitmap restaurantIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_restaurant);
        Bitmap hotelIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_hotel);
        Bitmap mosqueIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_mosque);
        Bitmap beautySalonIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_beauty_salon);
        Bitmap gymIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_gym);
        Bitmap barIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_local_bar);
        Bitmap moneyExchangeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_money_exchange);
        Bitmap carRepairIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_car_repair);
        Bitmap hospitalIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_local_hospital);
        Bitmap gasIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_local_gas_station);
        Bitmap busIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_bus_station);
        Bitmap policeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_police_station);
        Bitmap postOfficeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_post_office);
        Bitmap airportIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_local_airport);
        Bitmap university = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_university);
        Bitmap pharmacyIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_pharmacy);
        Bitmap train = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_train);
        Bitmap parking = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_parking);
        Bitmap park = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_park);
        Bitmap taxiIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_local_taxi);
        Bitmap travelIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_travel);
        Bitmap realState = BitmapFactory.decodeResource(this.getResources(), R.drawable.real_estate);
        Bitmap jewelry = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_jewelry);
        Bitmap governmentOffice = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_government_office);
        Bitmap bicycle = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_bicycle_store);
        Bitmap cafe = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_cafe);
        Bitmap book = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_book);


        gridItemArray.add(new GridItem(bankIcon, "Bank"));
        gridItemArray.add(new GridItem(atmIcon, "ATM"));
        gridItemArray.add(new GridItem(shoppingMallIcon, "Shopping Mall"));
        gridItemArray.add(new GridItem(restaurantIcon, "Restaurant"));
        gridItemArray.add(new GridItem(hotelIcon, "Hotel"));
        gridItemArray.add(new GridItem(cafe, "Cafe"));
        gridItemArray.add(new GridItem(pharmacyIcon, "Pharmacy"));
        gridItemArray.add(new GridItem(hospitalIcon, "Hospital"));
        gridItemArray.add(new GridItem(moneyExchangeIcon, "Money Exchange"));
        gridItemArray.add(new GridItem(university, "University"));
        gridItemArray.add(new GridItem(carRepairIcon, "Car Repair"));
        gridItemArray.add(new GridItem(gasIcon, "Gas Station"));
        gridItemArray.add(new GridItem(travelIcon, "Travel Agency"));
        gridItemArray.add(new GridItem(realState, "Real Estate Agency"));

        gridItemArray.add(new GridItem(busIcon, "Bus Stand"));
        gridItemArray.add(new GridItem(taxiIcon, "Taxi Stand"));
        gridItemArray.add(new GridItem(book, "Book Store"));
        gridItemArray.add(new GridItem(park, "Park"));
        gridItemArray.add(new GridItem(mosqueIcon, "Mosque"));
        gridItemArray.add(new GridItem(governmentOffice, "Govt. Office"));
        gridItemArray.add(new GridItem(jewelry, "Jewelry Store"));
        gridItemArray.add(new GridItem(beautySalonIcon, "Beauty Salon"));
        gridItemArray.add(new GridItem(gymIcon, "Gymnasium"));
        gridItemArray.add(new GridItem(barIcon, "Bar"));
        gridItemArray.add(new GridItem(bicycle, "Bicycle Store"));
        gridItemArray.add(new GridItem(parking, "Parking"));
        gridItemArray.add(new GridItem(policeIcon, "Police Station"));
        gridItemArray.add(new GridItem(postOfficeIcon, "Post Office"));
        gridItemArray.add(new GridItem(airportIcon, "Airport"));
        gridItemArray.add(new GridItem(train, "Train Station"));


        gridView = (GridView) v.findViewById(R.id.gridView);

        gridAdapter = new GridAdapter(getActivity(), R.layout.row_grid, gridItemArray);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Get the name of selected Grid Item
                GridItem gridItems = gridItemArray.get(i);
                String name = gridItems.getTitle();
                Log.e("Grid item name.", name);

                switch (name) {
                    case "Bank":
                        // Check if Internet present
                        if (!isInternetPresent) {
                            // Internet Connection is not present
                            Snackbar.make(view, "No internet connection available!", Snackbar.LENGTH_LONG).setAction("Enable", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                }
                            }).show();
                            // stop executing code by return
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goBank = new Intent(getActivity(), NearbyPlaces.class);
                        goBank.putExtras(bundle);
                        startActivity(goBank);

                        break;

                    case "ATM":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goATM = new Intent(getActivity(), NearbyPlaces.class);
                        goATM.putExtras(bundle);
                        startActivity(goATM);

                        break;

                    case "Restaurant":
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

                        Intent goRestaurant = new Intent(getActivity(), NearbyPlaces.class);
                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        goRestaurant.putExtras(bundle);
                        startActivity(goRestaurant);

                        break;

                    case "Shopping Mall":
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

                        Intent goShoppingMall = new Intent(getActivity(), NearbyPlaces.class);
                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        goShoppingMall.putExtras(bundle);
                        startActivity(goShoppingMall);

                        break;

                    case "Hotel":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goHotel = new Intent(getActivity(), NearbyPlaces.class);
                        goHotel.putExtras(bundle);
                        startActivity(goHotel);

                        break;

                    case "Mosque":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goMosque = new Intent(getActivity(), NearbyPlaces.class);
                        goMosque.putExtras(bundle);
                        startActivity(goMosque);

                        break;

                    case "Beauty Salon":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goBeautySalon = new Intent(getActivity(), NearbyPlaces.class);
                        goBeautySalon.putExtras(bundle);
                        startActivity(goBeautySalon);

                        break;

                    case "Gymnasium":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goGymnasium = new Intent(getActivity(), NearbyPlaces.class);
                        goGymnasium.putExtras(bundle);
                        startActivity(goGymnasium);

                        break;


                    case "Bar":

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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goBar = new Intent(getActivity(), NearbyPlaces.class);
                        goBar.putExtras(bundle);
                        startActivity(goBar);

                        break;

                    case "Money Exchange":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goMoneyExchange = new Intent(getActivity(), NearbyPlaces.class);
                        goMoneyExchange.putExtras(bundle);
                        startActivity(goMoneyExchange);

                        break;

                    case "Car Repair":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goCarRepair = new Intent(getActivity(), NearbyPlaces.class);
                        goCarRepair.putExtras(bundle);
                        startActivity(goCarRepair);

                        break;

                    case "Hospital":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goHospital = new Intent(getActivity(), NearbyPlaces.class);
                        goHospital.putExtras(bundle);
                        startActivity(goHospital);

                        break;

                    case "Gas Station":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goGasStation = new Intent(getActivity(), NearbyPlaces.class);
                        goGasStation.putExtras(bundle);
                        startActivity(goGasStation);

                        break;


                    case "Bus Stand":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goBusStation = new Intent(getActivity(), NearbyPlaces.class);
                        goBusStation.putExtras(bundle);
                        startActivity(goBusStation);

                        break;

                    case "Police Station":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goPoliceStation = new Intent(getActivity(), NearbyPlaces.class);
                        goPoliceStation.putExtras(bundle);
                        startActivity(goPoliceStation);

                        break;

                    case "Post Office":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goPostOffice = new Intent(getActivity(), NearbyPlaces.class);
                        goPostOffice.putExtras(bundle);
                        startActivity(goPostOffice);

                        break;

                    case "Airport":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goAirport = new Intent(getActivity(), NearbyPlaces.class);
                        goAirport.putExtras(bundle);
                        startActivity(goAirport);

                        break;
                    case "University":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goUv = new Intent(getActivity(), NearbyPlaces.class);
                        goUv.putExtras(bundle);
                        startActivity(goUv);

                        break;

                    case "Pharmacy":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goPharmacy = new Intent(getActivity(), NearbyPlaces.class);
                        goPharmacy.putExtras(bundle);
                        startActivity(goPharmacy);

                        break;

                    case "Train Station":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goTrainStation = new Intent(getActivity(), NearbyPlaces.class);
                        goTrainStation.putExtras(bundle);
                        startActivity(goTrainStation);

                        break;

                    case "Parking":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goParking = new Intent(getActivity(), NearbyPlaces.class);
                        goParking.putExtras(bundle);
                        startActivity(goParking);

                        break;

                    case "Park":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goPark = new Intent(getActivity(), NearbyPlaces.class);
                        goPark.putExtras(bundle);
                        startActivity(goPark);

                        break;

                    case "Taxi Stand":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goTaxi = new Intent(getActivity(), NearbyPlaces.class);
                        goTaxi.putExtras(bundle);
                        startActivity(goTaxi);

                        break;

                    case "Travel Agency":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goTravel = new Intent(getActivity(), NearbyPlaces.class);
                        goTravel.putExtras(bundle);
                        startActivity(goTravel);

                        break;

                    case "Real Estate Agency":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goRealEstateAgency = new Intent(getActivity(), NearbyPlaces.class);
                        goRealEstateAgency.putExtras(bundle);
                        startActivity(goRealEstateAgency);

                        break;

                    case "Jewelry Store":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goJewelryShop = new Intent(getActivity(), NearbyPlaces.class);
                        goJewelryShop.putExtras(bundle);
                        startActivity(goJewelryShop);

                        break;

                    case "Govt. Office":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goGovtOffice = new Intent(getActivity(), NearbyPlaces.class);
                        goGovtOffice.putExtras(bundle);
                        startActivity(goGovtOffice);

                        break;
                    case "Bicycle Store":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goBicycle = new Intent(getActivity(), NearbyPlaces.class);
                        goBicycle.putExtras(bundle);
                        startActivity(goBicycle);

                        break;
                    case "Cafe":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goCafe = new Intent(getActivity(), NearbyPlaces.class);
                        goCafe.putExtras(bundle);
                        startActivity(goCafe);

                        break;

                    case "Book Store":
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

                        bundle.putString("name", name);
                        bundle.putDouble("AllTabLat", latitude);
                        bundle.putDouble("AllTabLng", longitude);
                        Intent goBook = new Intent(getActivity(), NearbyPlaces.class);
                        goBook.putExtras(bundle);
                        startActivity(goBook);

                        break;

                }
            }
        });

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
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            //Pass the location data in interface method
            onDataPass.passLocationData(latitude, longitude);
            Log.e("All Tab location", String.valueOf(latitude));
            Log.e("All Tab location", String.valueOf(longitude));

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
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            Log.e("All Tab change location", String.valueOf(latitude));
            Log.e("All Tab change location", String.valueOf(longitude));
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
