package com.example.david.trimettrack;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by David on 7/24/2017.
 */


//Code based on https://www.androidhive.info/2012/01/android-json-parsing-tutorial/ by RAVI TAMADA

public class StopInfoJSONParser {

    private String TAG = "StopInfo_JSON_Parser";
    private String curLocation = null;
    private String direction = null ;
    private String errorContent = null;
    private String arrivalInfo = null;


    ArrayList<HashMap<String,String>> stopInfoList;

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

    public String getArrivalInfo() {
        return arrivalInfo;
    }

    public void setArrivalInfo(String arrivalInfo) {
        this.arrivalInfo = arrivalInfo;
    }


    public ArrayList<HashMap<String, String>> getStopInfoList() {
        return stopInfoList;
    }

    public void setStopInfoList(ArrayList<HashMap<String, String>> stopInfoList) {
        this.stopInfoList = stopInfoList;
    }

    public StopInfoJSONParser() {
        stopInfoList = new ArrayList<>();
    }

    public void getStopInfoResult(String url) throws Exception {

        String TimeLeft;
        String detourInfo;

        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);


        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject resultSet = jsonObj.getJSONObject("resultSet");

                if (resultSet.has("error")) {
                    JSONObject error = resultSet.getJSONObject("error");
                    errorContent = error.getString("content");
                } else {

                    HashMap<String, String> detourInfoList;
                    detourInfoList = new HashMap<>();
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
                    direction = "(" + temp.getString("dir") + ")";

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
                            String estimatedTime;
                            if (a.has("estimated")) {
                                estimatedTime = a.getString("estimated");

                                //Time Remaining
                                TimeLeft = MillisecondToTimeLeftFormat(estimatedTime) + " mins";

                                estimatedTime = MillisecondToTimeFormat(estimatedTime);

                            } else {
                                estimatedTime = "None";
                                TimeLeft = "-";
                            }

                            if (a.has("detour")) {
                                StringBuilder strBuilder = new StringBuilder();
                                JSONArray tmpDetour = a.getJSONArray("detour");
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
                            stopInfo.put("RemainingTime", TimeLeft);
                            stopInfo.put("detourInfo", detourInfo);

                            // adding contact to contact list
                            stopInfoList.add(stopInfo);
                        }
                    } else {
                        arrivalInfo = "No Arrivals Found!!!";
                    }
                }

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }

        } else {
            Log.e(TAG, "Couldn't get json from server.");}
    }

        //Methods for time format
    private String MillisecondToTimeLeftFormat(String millisecond){
        long currentTime = System.currentTimeMillis();
        long result = Long.valueOf(millisecond) - currentTime;
        if (result == 0)
            return "Due Now";
        else {
            return String.valueOf(MILLISECONDS.toMinutes(result));
        }
    }

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

