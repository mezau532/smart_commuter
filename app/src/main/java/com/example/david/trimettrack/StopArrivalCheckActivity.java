package com.example.david.trimettrack;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class StopArrivalCheckActivity extends AppCompatActivity {

    private String TAG = StopArrivalCheckActivity.class.getSimpleName();

    private String stopId;
    private String appId = "1D72A2E7E55A71C6646AD061E";
    private String url = "https://developer.trimet.org/ws/V2/arrivals?locIDs=" + stopId + "&appID=" + appId + "&json=true";

    private StopInfoJSONParser result;

    //List of Stop Information
    private ListView lv;
    private ListAdapter adapter;
    ArrayList<HashMap<String, String>> stopInfoList;

    //Obtain Current stop ID
    private EditText currentStopId;

    //Variables
    private TextView currentDir;
    private TextView currentLoc;
    private TextView errorTextBox;
    private TextView noArrivalInfo;
    private String curLocation;
    private String direction;
    private String errorContent;
    private String arrivalInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_arrival_check);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        //Get data that is being passed from Main Activity
        Bundle stopData = getIntent().getExtras();
        if (stopData == null)
            return;
        String stopId = stopData.getString("stopIdInput");
        currentStopId = (EditText) findViewById(R.id.stopIdInputBox);
        currentStopId.setText(stopId);

        //Define the URL that going to be retrieve data from
        url = "https://developer.trimet.org/ws/V2/arrivals?locIDs=" + stopId + "&appID=" + appId + "&json=true";

        //Initialize a new Array List to store arrival information for a specific stop ID
        stopInfoList = new ArrayList<>();

        //Find listView box in which it is going to display arrival information
        lv = (ListView) findViewById(R.id.stopInfoListView);

        //Get the result and set content to ListView
        new GetStopInfo().execute(url);

    }

    public void onClick(View view) {
        hideKeyboard(view);
        currentStopId = (EditText) findViewById(R.id.stopIdInputBox);
        stopId = currentStopId.getText().toString();
        url = "https://developer.trimet.org/ws/V2/arrivals?locIDs=" + stopId + "&appID=" + appId + "&json=true";
        stopInfoList.clear();

        new GetStopInfo().execute(url);
        System.gc();
    }

    private class GetStopInfo extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "Stop Information is downloading");
        }

        //Retrieve Data from Trimet API
        @Override
        protected Void doInBackground(String... args) {

            String url = args[0];
            Log.e(TAG, "Response from url: " + url);

            try {
                //Parse the JSON response into Android Version
                result = new StopInfoJSONParser();
                result.getStopInfoResult(url);
                stopInfoList = result.getStopInfoList();
                curLocation = result.getCurLocation();
                direction = result.getDirection();
                errorContent = result.getErrorContent();
                arrivalInfo = result.getArrivalInfo();

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

        //Set content to Views
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //Set Text to error TextView if it has content
            if (errorContent == null) {
                currentLoc = (TextView) findViewById(R.id.currentStopLocation);
                currentLoc.setText(curLocation);
                currentDir = (TextView) findViewById(R.id.currentLocationDir);
                currentDir.setText(direction);

                if (stopInfoList.isEmpty()) {

                    noArrivalInfo = (TextView) findViewById(R.id.noArrivalInfo);
                    noArrivalInfo.setVisibility(View.VISIBLE);
                    noArrivalInfo.setText(arrivalInfo);

                    //Make TextView Invisible when they have no content
                    errorTextBox = (TextView) findViewById(R.id.errorContent);
                    errorTextBox.setVisibility(View.GONE);

                } else {
                    adapter = new SimpleAdapter(StopArrivalCheckActivity.this, stopInfoList,
                            R.layout.stop_info_list_item, new String[]{"shortStreetName",
                            "scheduledArrivalTime", "estimatedArrivalTime", "fullStreetName", "RemainingTime", "detourInfo"},
                            new int[]{R.id.shortStreetName, R.id.scheduledArrivalTime,
                                    R.id.estimatedArrivalTime, R.id.fullStreetName, R.id.TimeLeft, R.id.detourInfo});
                    lv.setAdapter(adapter);

                    //Make TextView Invisible when they have no content
                    noArrivalInfo = (TextView) findViewById(R.id.noArrivalInfo);
                    noArrivalInfo.setVisibility(View.GONE);
                    errorTextBox = (TextView) findViewById(R.id.errorContent);
                    errorTextBox.setVisibility(View.GONE);
                }

            } else {
                errorTextBox = (TextView) findViewById(R.id.errorContent);
                errorTextBox.setVisibility(View.VISIBLE);
                errorTextBox.setText(errorContent);

                //Make TextView Invisible when they have no content
                noArrivalInfo = (TextView) findViewById(R.id.noArrivalInfo);
                noArrivalInfo.setVisibility(View.GONE);
            }

        }
    }

    //Automatically hide keyboard after a input
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(StopArrivalCheckActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}