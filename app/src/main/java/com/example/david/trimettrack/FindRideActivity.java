package com.example.david.trimettrack;

import Sync.GoogleGeocodeSync;
import Sync.Info.CostEstimateDTO;
import Sync.Info.LocationDTO;
import Sync.Info.LyftClientCredentials;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    //don't know if I want these private or not?
    //TODO remove this comment once I decide what I want
    //TODO there will be no need for this if i implement location services
    public static final String TAG = HttpHandler.class.getSimpleName();
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

    }

    public void onClick(View view) {
        LyftRideInfoSync lyftRideInfoSync = new LyftRideInfoSync();
        StartAddress = "start";
        DestinationAddress = "destination";
//        lyftRideInfoSync.execute(StartAddress, DestinationAddress);

        TextView RideOutput;
        HttpHandler httpHandler = new HttpHandler();
        final EditText StartAddressET = (EditText) findViewById(R.id.StartInputBox);
        final EditText DestinationAddressET = (EditText) findViewById(R.id.DestinationInputBox);
        StartAddress = StartAddressET.getText().toString();
        DestinationAddress = DestinationAddressET.getText().toString();

        lyftRideInfoSync.execute(StartAddress, DestinationAddress);
        //the following code is from Lyft-sdk github:
        RideOutput = (TextView) findViewById(R.id.RideOutputBox);



 /*       ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId(ClientId)
                .setClientToken(ClientToken)
                .build();

        LyftButton lyftButton = (LyftButton) findViewById(R.id.lyft_button);
        lyftButton.setApiConfig(apiConfig);

        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                .setPickupAddress(StartAddress)
                .setDropoffAddress(DestinationAddress);
        //RideTypeEnum can be set to type of uber you want
        rideParamsBuilder.setRideTypeEnum(RideTypeEnum.CLASSIC);

        lyftButton.setRideParams(rideParamsBuilder.build());
        lyftButton.load();     */

    }

    public class LyftRideInfoSync extends AsyncTask<String, Void, String> {
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
        @Override
        protected void onPostExecute(String result){
            TextView RideOutput;
            RideOutput = (TextView) findViewById(R.id.RideOutputBox);
            if(result == null){
                RideOutput.setText("please input valid address");
                return;
            }
            //desirializeing json into a class object
            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(result);
            JsonElement jsonElement = json.get("cost_estimates");
            String jsonString = jsonElement.toString();
            CostEstimateDTO[] LyftCostEstimates = new GsonBuilder().create().fromJson(jsonString, CostEstimateDTO[].class);

            int size = LyftCostEstimates.length;
            double priceMax = LyftCostEstimates[0].getEstimated_cost_cents_max()/100;
            RideOutput.setText("ryde_type: " + LyftCostEstimates[0].getRide_type()
                    + "\nprice max: $" + priceMax
                    + "\ntotal results: " + size);
            this.results = "reached results";

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
                    return null;
                }

                String url = MessageFormat.format(
                        "https://api.lyft.com/v1/cost?start_lat={0}&start_lng={1}&end_lat={2}&end_lng={3}",
                        srt.getLat(), srt.getLng(), dest.getLat(), dest.getLng());
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");

                //add request header
//        con.setRequestProperty("User-Agent", USER_AGENT);
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
                this.results = response.toString();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
//            in.close();

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