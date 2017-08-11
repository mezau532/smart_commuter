/**
 * Copyright (c) 2017 Ulysses Meza and Jinghui Han
 * This code is available under the "MIT License".
 * Please see the file LICENSE in this distribution for license terms
 */

package com.example.david.trimettrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.MessageFormat;

import Sync.Info.CostEstimateDTO;
import Sync.Info.ListOfCostEstimateDtos;
import Sync.Info.UberCostEstimateDTO;
import Sync.Info.UberListOfCostEstimateDTOs;

public class UberLyftListActivity extends AppCompatActivity {
    CostEstimateDTO cheapestLyft;
    UberCostEstimateDTO cheapestUber;
    ListView list;
    String[] rideTypes;
    String[] rideDistances;
    String[] rideDurations;
    String[] rideCosts;

    String[] defaultRideTypes = { "Sorry no rides found" };
    String[] defaultRideDistances = { " " };
    String[] defaultRideDurations = { " " };
    String[] defaultRideCosts = { " " };
    TextView init;
    TextView dest;
    int arraySize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_lyft_list);

        arraySize = 0;
        Bundle data = getIntent().getExtras();
        if (data == null)
            return;
        String result = data.getString("LyftList");
        String results = data.getString("UberList");
        String startAdd = data.getString("startAdd");
        String endAdd = data.getString("endAdd");
        init = (TextView) findViewById(R.id.InitOut);
        dest = (TextView) findViewById(R.id.DestOut);
        init.setText(startAdd);
        dest.setText(endAdd);

        if(! (result.equals("null"))) {
            JsonParser jsonParser = new JsonParser();
            JsonObject json = (JsonObject) jsonParser.parse(result);
            JsonElement lyftJsonElement = json.get("cost_estimates");
            String lyftJsonString = lyftJsonElement.toString();
            CostEstimateDTO[] LyftCostEstimates = new GsonBuilder().create().fromJson(lyftJsonString, CostEstimateDTO[].class);

            int liftListSize = LyftCostEstimates.length;
            ListOfCostEstimateDtos lyftlist = new ListOfCostEstimateDtos();
            lyftlist.setListOfCostEstimates(LyftCostEstimates);
            cheapestLyft = lyftlist.getCheapest();
        }
        else{
            cheapestLyft = null;
        }
      //
        if(! results.equals("null")) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = (JsonObject) parser.parse(results);
            JsonElement jsonElement = jsonObj.get("prices");
            String jsonString = jsonElement.toString();
            UberCostEstimateDTO[] uberCostEstimates = new GsonBuilder().create().fromJson(jsonString, UberCostEstimateDTO[].class);
            int size = uberCostEstimates.length;
            UberListOfCostEstimateDTOs uberList = new UberListOfCostEstimateDTOs();
            uberList.setUberListOfCostEstimates(uberCostEstimates);
            cheapestUber = uberList.getCheapest();
        }
        else{
            cheapestUber = null;
        }
        if(cheapestLyft != null && cheapestUber != null){
            rideTypes = new String[] { cheapestUber.getDisplay_name(), cheapestLyft.getRide_type() };
          //  String uberCost = MessageFormat.format("${0}-{1}", cheapestUber.getLow_estimate(), cheapestUber.getHigh_estimate());
            String lyftCost = cheapestLyft.getFormattedCost();
            rideCosts = new String[] { cheapestUber.getEstimate(), lyftCost};
            String uberDuration = MessageFormat.format("Duration: {0}", cheapestUber.getFormattedDuration());
            String lyftDuration = MessageFormat.format("Duration: {0}", cheapestLyft.getFormattedDuration());
            rideDurations = new String[] {uberDuration, lyftDuration};
            String uberDistance = MessageFormat.format("Distance: {0}m", cheapestUber.getDistance());
            String lyftDistance = MessageFormat.format("Distance: {0}m", cheapestLyft.getEstimated_distance_miles());
            rideDistances = new String[] {uberDistance, lyftDistance};
        }
        else{
            if(cheapestLyft != null){
                rideTypes = new String[] { cheapestLyft.getRide_type() };
                rideCosts = new String[] { cheapestLyft.getFormattedCost() };
                String lyftDuration = MessageFormat.format("Duration: {0}m", cheapestLyft.getFormattedDuration());
                rideDurations = new String[] { lyftDuration };
                String lyftDistance = MessageFormat.format("Distance: {0}m", cheapestLyft.getEstimated_distance_miles());
                rideDistances = new String[] { lyftDistance };
            }
            if(cheapestUber != null){
                rideTypes = new String[] { cheapestUber.getDisplay_name() };
                rideCosts = new String[] { cheapestUber.getEstimate() };
                String uberDuration = MessageFormat.format("Duration: {0}", cheapestUber.getFormattedDuration());
                rideDurations = new String[] { uberDuration };
                String uberDistance = MessageFormat.format("Distance: {0}m", cheapestUber.getDistance());
                rideDistances = new String[] { uberDistance };
            }
        }
        list = (ListView) findViewById(R.id.uberLyftList);
        if(cheapestLyft == null && cheapestUber == null){
            CustomListview customListview = new CustomListview(this, defaultRideTypes, defaultRideCosts, defaultRideDurations, defaultRideDistances);
            list.setAdapter(customListview);
        }
        else {
            CustomListview customListview = new CustomListview(this, rideTypes, rideCosts, rideDurations, rideDistances);
            list.setAdapter(customListview);
        }
    }
}
