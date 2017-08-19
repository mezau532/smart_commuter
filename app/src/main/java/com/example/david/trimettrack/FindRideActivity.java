/**
 * Copyright (c) 2017 Ulysses Meza and Jinghui Han
 * This code is available under the "MIT License".
 * Please see the file LICENSE in this distribution for license terms
 */

package com.example.david.trimettrack;

import Sync.GoogleGeocodeSync;
import Sync.Info.CostEstimateDTO;
import Sync.Info.LocationDTO;
import Sync.Info.LyftClientCredentials;
import Sync.Info.UberCostEstimateDTO;
import config.configFile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

public class FindRideActivity extends AppCompatActivity {
    public configFile Config = new configFile();
    Location loc;
    private LocationManager locationManager;
    private LocationListener listener;
    String lat;
    String lng;
    private CheckBox locationCheckbox;
    private EditText startAdressInput;
    //don't know if I want these private or not?
    //TODO remove this comment once I decide what I want
    //TODO there will be no need for this if i implement location services
    public static final String TAG = HttpHandler.class.getSimpleName();
    public UberCostEstimateDTO cheapestUber;
    CostEstimateDTO cheapestLyft;
    public String serverToken = Config.uberApiKey;
    public String StartAddress;
    public String DestinationAddress;
    public String GoogleApiKey = Config.googleApiKey;
    ProgressBar loading;
    TextView invalidInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride);
        loading = (ProgressBar) findViewById(R.id.loadIndicator);
        loading.setVisibility(View.INVISIBLE);
        invalidInput = (TextView) findViewById(R.id.InvalidInput);
        invalidInput.setVisibility(View.INVISIBLE);
        /* location listener implementation based on "Android GPS, Location Manager Tutorial" by RAVI TAMADA.
        * More detail on https://www.androidhive.info/2012/07/android-gps-location-manager-tutorial
        */
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = MessageFormat.format("{0}", location.getLatitude());
                lng = MessageFormat.format("{0}", location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configure_button();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, listener);
            Log.d("Network", "Network");
            if (locationManager != null) {
                loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc != null) {
                    lng = String.valueOf(loc.getLongitude());
                    lat = String.valueOf(loc.getLatitude());
                }
            }
        }
        // if GPS Enabled get lat/long using GPS Services
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (loc == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, listener);
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (loc != null) {
                        lng = String.valueOf(loc.getLongitude());
                        lat = String.valueOf(loc.getLatitude());
                    }
                }
            }
        }
    }

    public void onCheckboxClicked(View view){
        configure_button();
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.LocationCheckBox:
                if(checked){
                      startAdressInput = (EditText) findViewById(R.id.StartInputBox);
                      startAdressInput.setEnabled(false);
                      geocodeAddress geocode = new geocodeAddress(this);
                      geocode.execute(lat, lng);
                }
                else{
                    startAdressInput = (EditText) findViewById(R.id.StartInputBox);
                    startAdressInput.setEnabled(true);
                    startAdressInput.setText("");
                }
        }
    }

    public void onClick(View view) {
        loading.setVisibility(View.VISIBLE);
        invalidInput.setVisibility(View.INVISIBLE);
        LyftRideInfoSync lyftRideInfoSync = new LyftRideInfoSync(this);
        StartAddress = "start";
        DestinationAddress = "destination";

        final EditText StartAddressET = (EditText) findViewById(R.id.StartInputBox);
        final EditText DestinationAddressET = (EditText) findViewById(R.id.DestinationInputBox);
        StartAddress = StartAddressET.getText().toString();
        DestinationAddress = DestinationAddressET.getText().toString();

        locationCheckbox = (CheckBox) findViewById(R.id.LocationCheckBox);
        if(! (locationCheckbox.isChecked())) {
            StartAddressET.setText("");
        }
        DestinationAddressET.setText("");

        lyftRideInfoSync.execute(StartAddress, DestinationAddress);


    }
    public class geocodeAddress extends AsyncTask<String, Void, String> {
        private Context context;
        public geocodeAddress(Context context){
            this.context = context;
        }
        @Override
        protected void onPostExecute(String s) {
            if(s != null) {
                startAdressInput = (EditText) findViewById(R.id.StartInputBox);
                startAdressInput.setText(s);
            }
            else{
                startAdressInput = (EditText) findViewById(R.id.StartInputBox);
                startAdressInput.setText("Current Location Not found sorry");
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            GoogleGeocodeSync geocoder = new GoogleGeocodeSync();
            String address = geocoder.getAddress(strings[0], strings[1], GoogleApiKey);
            return address;
        }
    }

    public class LyftRideInfoSync extends AsyncTask<String, Void, String> {
        private Context context;
        private Exception exeption;
        public String results;
        public String ClientToken = Config.liftApiKey;
        public LyftClientCredentials Creds = new LyftClientCredentials();

        public String getResults() {
            return this.results;
        }

        public LyftRideInfoSync(Context context){
            this.context = context;
        }
        @Override
        protected void onPostExecute(String result){
            loading.setVisibility(View.INVISIBLE);
            Intent i = new Intent(context, UberLyftListActivity.class);
            if(result == ""){
                //TODO: add an invisible output for invalid output
                invalidInput.setVisibility(View.VISIBLE);
                return;
            }

            i.putExtra("startAdd", StartAddress);
            i.putExtra("endAdd", DestinationAddress);
            if(result == null){
                i.putExtra("LyftList", "null");
                cheapestLyft = null;
            }
            else {
                i.putExtra("LyftList", result);
                //desirializeing json into a class object
            }

            if(this.results == null){
                cheapestUber = null;
                i.putExtra("UberList", "null");
            }
            else {
                i.putExtra("UberList", this.results);
            }
            startActivity(i);

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String startAddress = strings[0];
                String destinationAddress = strings[1];

                LocationDTO srt;
                LocationDTO dest;
                GoogleGeocodeSync geocoder = new GoogleGeocodeSync();
                srt = geocoder.getCoordinates(startAddress, GoogleApiKey);
                dest = geocoder.getCoordinates(destinationAddress, GoogleApiKey);

                if(srt == null || dest == null){
                    return "";
                }

                String uberUrl = MessageFormat.format(
                        "https://api.uber.com/v1.2/estimates/price?start_latitude={0}&start_longitude={1}&end_latitude={2}&end_longitude={3}",
                        srt.getLat(), srt.getLng(), dest.getLat(), dest.getLng());
                URL Url = new URL(uberUrl);
                HttpURLConnection connection = (HttpURLConnection) Url.openConnection();

                // optional default is GET
                connection.setRequestMethod("GET");

                //add request header
                connection.setRequestProperty("Authorization", "Token " + serverToken);
                connection.setRequestProperty("Accept-Language", "en_US");
                connection.setRequestProperty("Content-Type", "application/json");

                int respCode = connection.getResponseCode();

                if(respCode != 200){
                    this.results = null;
                }
                else {
                    System.out.println("\nSending 'GET' request to URL : " + uberUrl);
                    System.out.println("Response Code : " + respCode);

                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String inLine;
                    StringBuffer resp = new StringBuffer();
                    while ((inLine = input.readLine()) != null) {
                        resp.append(inLine);
                    }
                    input.close();

                    //print result
                    System.out.println(resp.toString());
                    this.results = resp.toString();
                }


                String url = MessageFormat.format(
                        "https://api.lyft.com/v1/cost?start_lat={0}&start_lng={1}&end_lat={2}&end_lng={3}",
                        srt.getLat(), srt.getLng(), dest.getLat(), dest.getLng());
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");

                //add request header
                con.setRequestProperty("Authorization", "Bearer " + ClientToken);

                int responseCode = con.getResponseCode();

                if(responseCode != 200){
                    return null;
                }
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
