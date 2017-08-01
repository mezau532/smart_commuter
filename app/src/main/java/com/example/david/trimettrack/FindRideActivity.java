package com.example.david.trimettrack;

import Sync.GoogleGeocodeSync;
import Sync.Info.CostEstimateDTO;
import Sync.Info.ListOfCostEstimateDtos;
import Sync.Info.LocationDTO;
import Sync.Info.LyftClientCredentials;
import Sync.Info.UberCostEstimateDTO;
import Sync.Info.UberListOfCostEstimateDTOs;
import Sync.UberSync;

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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.lyftbutton.RideTypeEnum;
import com.lyft.networking.ApiConfig;

import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FindRideActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener listener;
    double lat;
    double lng;
    private CheckBox locationCheckbox;
    private EditText startAdressInput;
    //don't know if I want these private or not?
    //TODO remove this comment once I decide what I want
    //TODO there will be no need for this if i implement location services
    public static final String TAG = HttpHandler.class.getSimpleName();
    public UberCostEstimateDTO cheapestUber;
    CostEstimateDTO cheapestLyft;
    private String serverToken = "XtW6q7Yu7QSHxAfUhcAQTtbkVemZoHAH7XTeIDqi";
    public String StartAddress;
    public String DestinationAddress;
    public String ClientId = "R7K9RlJA-H87";
    public String ClientSecret = "rZnj1OvXQxgk8eCHoCp7owAHCjPIWqhZ";
    public String ClientToken = "roVZU6oVJyhdGGoM/VFKhmyuTmOYvBalKiezPB5PiHiTqsB72/1chvNJ/Zdx/YgvDdKfKiOGSMNBLJbKaXVOyNfj/2cWqAbDzz9gfRh8pA9Av/n0YyUCHbs=";
    public String GoogleBaseUrl = "https://maps.googleapis.com";
    public String GoogleApiKey = "AIzaSyA8IHKgx_3xnloVW5kH8shDwaw67Mu67Co";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
    }

    public void onCheckboxClicked(View view){
        configure_button();
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.LocationCheckBox:
                if(checked){
                      geocodeAddress geocode = new geocodeAddress(this);
                      geocode.execute(lat, lng);
                }
                else{
                    startAdressInput = (EditText) findViewById(R.id.StartInputBox);
                    startAdressInput.setText("");
                }
        }
    }

    public void onClick(View view) {
        LyftRideInfoSync lyftRideInfoSync = new LyftRideInfoSync(this);
        StartAddress = "start";
        DestinationAddress = "destination";
//        lyftRideInfoSync.execute(StartAddress, DestinationAddress);

        TextView RideOutput;
        HttpHandler httpHandler = new HttpHandler();
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
        //the following code is from Lyft-sdk github:
        RideOutput = (TextView) findViewById(R.id.RideOutputBox);


    }
    public class geocodeAddress extends AsyncTask<Double, Void, String> {
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
        protected String doInBackground(Double... doubles) {
            GoogleGeocodeSync geocoder = new GoogleGeocodeSync();
            String address = geocoder.getAddress(doubles[0], doubles[1], GoogleApiKey);
            return address;
        }
    }

    public class LyftRideInfoSync extends AsyncTask<String, Void, String> {
        private Context context;
        private Exception exeption;
        public String results;
        //TODO at some point I want to take these out of the code
        //I don't know how I will do it right now because we need
        //I don't know how I will do it right now because we need
        //the clientid and secrete to make lyft api calls
        public String ClientId = "R7K9RlJA-H87";
        public String ClientSecret = "rZnj1OvXQxgk8eCHoCp7owAHCjPIWqhZ";
        public String ClientToken = "roVZU6oVJyhdGGoM/VFKhmyuTmOYvBalKiezPB5PiHiTqsB72/1chvNJ/Zdx/YgvDdKfKiOGSMNBLJbKaXVOyNfj/2cWqAbDzz9gfRh8pA9Av/n0YyUCHbs=";
        public LyftClientCredentials Creds = new LyftClientCredentials();

        public String getResults() {
            return this.results;
        }

        public LyftRideInfoSync(Context context){
            this.context = context;
        }
        @Override
        protected void onPostExecute(String result){
            Intent i = new Intent(context, UberLyftListActivity.class);
            TextView RideOutput;
            RideOutput = (TextView) findViewById(R.id.RideOutputBox);
            if(result == ""){
                RideOutput.setText("please input valid address");
                return;
            }

            if(result == null){
                RideOutput.setText("Sorry no lyft rides found");
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
                String startLng = "-122.3918";
                String startLat = "37.7763";
                String endLng = "-122.4533";
                String endLat = "37.7972";

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
//            this.results = response.toString();
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}