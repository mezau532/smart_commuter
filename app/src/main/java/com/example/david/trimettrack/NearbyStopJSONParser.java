package com.example.david.trimettrack;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David on 8/3/2017.
 */

public class NearbyStopJSONParser {

    private String TAG = "Nearby Stop JSON Parser";

    //Variable
    ArrayList<HashMap<String,String>> nearbyStopInfoList;

    //Constructor
    public NearbyStopJSONParser(String url) {
        nearbyStopInfoList = new ArrayList<>();
        try{
            getNearbyStop(url);
        }catch (Exception e){
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
    }

    //Default Methods
    public ArrayList<HashMap<String, String>> getNearbyStopInfoList() {
        return nearbyStopInfoList;
    }

    public void setNearbyStopInfoList(ArrayList<HashMap<String, String>> nearbyStopInfoList) {
        this.nearbyStopInfoList = nearbyStopInfoList;
    }


    //Method, get Nearby Stop JSON response and Parse
    public void getNearbyStop(String url) throws Exception{

        // Make a request to the URL and get response from it
        HttpHandler request = new HttpHandler();
        String jsonStr = request.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);
    }
}
