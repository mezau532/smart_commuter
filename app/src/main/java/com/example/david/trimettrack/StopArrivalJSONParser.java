package com.example.david.trimettrack;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by David on 7/24/2017.
 * This class based on https://www.androidhive.info/2012/01/android-json-parsing-tutorial/ by RAVI TAMADA
 */


public class StopArrivalJSONParser {

    private String TAG = "StopInfo_JSON_Parser";

    //Variables
    private String curLocation = null;
    private String direction = null ;
    private String errorContent = null;
    private String noArrivalInfo = null;
    private ArrayList<HashMap<String,String>> stopInfoResultList;

    //Constructor
    public StopArrivalJSONParser(String url) {
        stopInfoResultList = new ArrayList<>();
        try {
            getStopInfoResult(url);
        } catch (Exception e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
    }

    //Default Methods
    public String getCurLocation() {
        return curLocation;
    }

    public void setCurLocation(String curLocation) {
        this.curLocation = curLocation;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getErrorContent() {
        return errorContent;
    }

    public void setErrorContent(String errorContent) {
        this.errorContent = errorContent;
    }

    public String getNoArrivalInfo() {
        return noArrivalInfo;
    }

    public void setNoArrivalInfo(String noArrivalInfo) {
        this.noArrivalInfo = noArrivalInfo;
    }


    public ArrayList<HashMap<String, String>> getStopInfoResultList() {
        return stopInfoResultList;
    }

    public void setStopInfoResultList(ArrayList<HashMap<String, String>> stopInfoResultList) {
        this.stopInfoResultList = stopInfoResultList;
    }


    //Get JSON response and parse result method
    public void getStopInfoResult(String url) throws Exception {

        // Make a request to the URL and get response from it
        HttpHandler request = new HttpHandler();
        String jsonStr = request.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);

        //Check response content whether is empty
        if (jsonStr != null) {

            //If it is no empty, start parse the JSON response
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject resultSet = jsonObj.getJSONObject("resultSet");

                if (resultSet.has("error")) {
                    JSONObject error = resultSet.getJSONObject("error");
                    errorContent = error.getString("content");
                } else {

                    HashMap<String, String> detourInfoList = new HashMap<>();
                    if (resultSet.has("detour")) {

                        JSONArray detour = resultSet.getJSONArray("detour");
                        for (int i = 0; i < detour.length(); i++) {

                            JSONObject tmp = detour.getJSONObject(i);
                            String detourId = tmp.getString("id");
                            String desc = tmp.getString("desc");

                            detourInfoList.put(detourId, desc);
                        }
                    }

                    //Get Current Location
                    JSONArray loc = resultSet.getJSONArray("location");
                    JSONObject temp = loc.getJSONObject(0);
                    curLocation = temp.getString("desc");
                    direction = MessageFormat.format("({0})",temp.getString("dir"));

                    //Get Arrival Time Detail
                    //Arrival is an JSON Array Node

                    if (resultSet.has("arrival")) {

                        JSONArray arrival = resultSet.getJSONArray("arrival");
                        for (int i = 0; i < arrival.length(); i++) {
                            //Get JSON object node
                            JSONObject a = arrival.getJSONObject(i);

                            //Get short street Sign
                            String shortSign = a.getString("shortSign");

                            //Get full street Sign
                            String fullSign = a.getString("fullSign");

                            //Get scheduled Time
                            String scheduledTime = a.getString("scheduled");

                            //Get estimated Time
                            String timeLeft;
                            String estimatedTime;
                            if (a.has("estimated")) {
                                estimatedTime = a.getString("estimated");

                                //Time Remaining
                                timeLeft = MillisecondToTimeLeftFormat(estimatedTime);

                                estimatedTime = MillisecondToTimeFormat(estimatedTime);

                            } else {
                                estimatedTime = "None";
                                timeLeft = "-";
                            }

                            String detourInfo;
                            if (a.has("detour")) {
                                //Get detour Information
                                JSONArray tmpDetour = a.getJSONArray("detour");

                                //Combine detour information into one string
                                StringBuilder strBuilder = new StringBuilder();
                                String[] detourDetail = new String[tmpDetour.length()];
                                for (int n = 0; n < tmpDetour.length(); n++) {
                                    detourDetail[n] = detourInfoList.get(tmpDetour.getString(n));
                                    strBuilder.append("Detour: " + detourDetail[n] + "\n");
                                }
                                detourInfo = strBuilder.toString().trim();

                            } else {
                                detourInfo = "No Detour Information";
                            }


                            // temporary hash map for single stop info item
                            HashMap<String, String> stopInfo = new HashMap<>();
                            stopInfo.put("shortStreetName", shortSign);
                            stopInfo.put("scheduledArrivalTime", "Scheduled: " + MillisecondToTimeFormat(scheduledTime));
                            stopInfo.put("estimatedArrivalTime", "Estimated: " + estimatedTime);
                            stopInfo.put("fullStreetName", "(" + fullSign + ")");
                            stopInfo.put("RemainingTime", timeLeft);
                            stopInfo.put("detourInfo", detourInfo);

                            // adding contact to contact list
                            stopInfoResultList.add(stopInfo);
                        }
                    } else {
                        noArrivalInfo = "No Arrivals Found!!!";
                    }
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }

        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
    }


    //Method, calculate when the bus/max arrive in minute format
    private String MillisecondToTimeLeftFormat(String millisecond){
        long currentTime = System.currentTimeMillis();
        long result = MILLISECONDS.toMinutes(Long.valueOf(millisecond) - currentTime);
        if (result == 0) {
            return "Due Now";
        }
        else {
            return String.valueOf(result) + " mins";
        }
    }

    //Method, transform millisecond to date format
    private String MillisecondToTimeFormat(String millisecond){

        //Change Millisecond to hh:mm am/pm format
        long current = Long.parseLong(millisecond);
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current);

        //Return the time as string back to Caller
        return formatter.format(calendar.getTime());
    }
}

