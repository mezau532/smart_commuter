package com.example.david.trimettrack;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David on 8/3/2017.
 * This class is based on https://www.androidhive.info/2012/01/android-json-parsing-tutorial/ by RAVI TAMADA
 */


public class NearbyStopJSONParser {

    private String TAG = "Nearby Stop JSON Parser";
    private String googleApiKey = "AIzaSyD1bFc2-m-ReAA94qeBEHa9Rcnca5gjEFY";

    //Variable
    private ArrayList<HashMap<String,String>> nearbyStopResultList;

    //Constructor
    public NearbyStopJSONParser(String url,String cur_lat, String cur_lng) {
        nearbyStopResultList = new ArrayList<>();
        try{
            getNearbyStop(url,cur_lat,cur_lng);
        }catch (Exception e){
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
    }

    //Default Methods
    public ArrayList<HashMap<String, String>> getNearbyStopResultList() {
        return nearbyStopResultList;
    }

    public void setNearbyStopResultList(ArrayList<HashMap<String, String>> nearbyStopResultList) {
        this.nearbyStopResultList = nearbyStopResultList;
    }

    //Method, get Nearby Stop JSON response and Parse
    public void getNearbyStop(String url,String cur_lat,String cur_lng) throws Exception{

        // Make a request to the URL and get response from it
        HttpHandler request = new HttpHandler();
        String jsonStr = request.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);

        if(jsonStr != null){
            try{
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject resultSet = jsonObj.getJSONObject("resultSet");
                if(resultSet.has("location")){

                    // Parse JSON location object to get information for each stop
                    JSONArray location = resultSet.getJSONArray("location");
                    for(int i = 0; i < location.length();++i) {
                        JSONObject stop = location.getJSONObject(i);

                        // Data from JSON Response
                        String des_lat = stop.getString("lat");
                        String des_lng = stop.getString("lng");
                        // Calculate distance between two pairs of coordinate
                        String result[] = calculateDistance(cur_lat, cur_lng, des_lat, des_lng);
                        String distance = result[0];
                        String duration = result[1];

                        // Data from JSON Response
                        String sign = stop.getString("desc") + " (" + stop.getString("dir") + ")";
                        String locationId = stop.getString("locid");

                        // temporary hash map for single stop info item
                        HashMap<String, String> stopInfo = new HashMap<>();
                        stopInfo.put("distance", distance);
                        stopInfo.put("duration", duration);
                        stopInfo.put("sign", sign);
                        stopInfo.put("locationId", locationId);

                        // adding contact to contact list
                        nearbyStopResultList.add(stopInfo);

                    }
                }
                else {
                    nearbyStopResultList = null;
                }
            } catch (Exception e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }else{
            Log.e(TAG, "Couldn't get json from server.");
        }
    }

    //Method,Calculate between two pairs of coordinates using Google API
    protected String[] calculateDistance(String cur_latitude,String cur_longitude,
                                       String des_latitude,String des_longitude){

        //Request URL for Google Distance Matrix API
        String googleDistanceRequestUrl = MessageFormat.format("https://maps.googleapis.com/maps/api/distancem" +
                "atrix/json?units=imperial&mode=walking&origins={0},{1}&destinations={2},{3}&key={4}",
                 cur_latitude,cur_longitude,des_latitude,des_longitude,googleApiKey);
        Log.e(TAG, "Response from url: " + googleDistanceRequestUrl);

        //Get Response from URL
        HttpHandler request = new HttpHandler();
        String jsonStr = request.makeServiceCall(googleDistanceRequestUrl);
        if(jsonStr.isEmpty()){
            return null;
        }
        else
        {
            try{
                //Parse JSON
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray resultRows = jsonObject.getJSONArray("rows");
                JSONArray resultElements = resultRows.getJSONObject(0).getJSONArray("elements");
                JSONObject distance = resultElements.getJSONObject(0).getJSONObject("distance");
                JSONObject duration = resultElements.getJSONObject(0).getJSONObject("duration");

                //Get only two elements from JSON response (duration and distance only)
                String []result = new String[2];
                result[0] = distance.getString("text");
                result[1] = duration.getString("text");
                return result;

            }catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                return null;
            }
        }
    }
}
