package com.example.david.trimettrack;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class NearbyStopCheckActivity extends AppCompatActivity {

    private String TAG = NearbyStopCheckActivity.class.getSimpleName();

    // Important data variable for API request
    private String url;
    private String cur_lng;
    private String cur_lat;
    private String appId;
    private String range = "800";  //meters

    // Data
    protected ArrayList<HashMap<String,String>> nearbyStopResultList;
    protected ListView lv;
    protected SimpleAdapter adapter;
    protected NearbyStopJSONParser nearbyStopJsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_nearby_check);

        // Get Trimet Api Key
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            appId = bundle.getString("trimetAppID");
        } catch (Exception e) {
            Log.e(TAG, "You need to configure the meta-data first.");
        }

        // Find listView in which is going to display arrival information
        lv = (ListView) findViewById(R.id.nearbyStopListView);

        // Get data that are needed for the request from Main Activity
        Bundle locationData = getIntent().getExtras();
        if (locationData != null){
            cur_lat = locationData.getString("current_latitude");
            cur_lng = locationData.getString("current_longitude");
            // Define Request URL
            url = MessageFormat.format("https://developer.trimet.org/ws/V1/stops?ll" +
                    "={0},{1}&meters={2}&appID={3}&json=true",cur_lat,cur_lng,range,appId);

            new GetNearbyStop().execute(url);
        }
        else{
            Toast.makeText(NearbyStopCheckActivity.this,"No Current Location Receive !! \nPlease Enable Location Service !!!",Toast.LENGTH_LONG).show();
        }

    }

    private class GetNearbyStop extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "Nearby Stop Information is downloading");

            Toast.makeText(NearbyStopCheckActivity.this,"Loading ...",Toast.LENGTH_LONG).show();
        }

        // Retrieve Data from Trimet API
        @Override
        protected Void doInBackground(String... args) {

            String link = args[0];
            Log.e(TAG, "Response from url: " + link);

            try {
                nearbyStopJsonResult = new NearbyStopJSONParser(link,cur_lat,cur_lng);
                nearbyStopResultList = nearbyStopJsonResult.getNearbyStopResultList();

            } catch (Exception e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Error Encountered", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        // Set content to Views
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(nearbyStopResultList != null) {
                // Put information to listView
                adapter = new SimpleAdapter(NearbyStopCheckActivity.this, nearbyStopResultList,
                        R.layout.nearby_stop_list_item, new String[]{"sign", "locationId", "distance", "duration"},
                        new int[]{R.id.signTextView, R.id.stopIdTextView, R.id.distanceTextView, R.id.durationTextView});
                lv.setAdapter(adapter);

                // Perform listView item click event
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // Goto Check_Nearby_Stops_Activity
                        Intent i = new Intent(NearbyStopCheckActivity.this, StopArrivalCheckActivity.class);
                        String stopId = ((TextView) view.findViewById(R.id.stopIdTextView)).getText().toString();
                        i.putExtra("stopIdInput", stopId);
                        startActivity(i);
                        finish();
                    }
                });
            }
            else
                Toast.makeText(NearbyStopCheckActivity.this,"No data Found !!!",Toast.LENGTH_LONG).show();
        }
    }
}
