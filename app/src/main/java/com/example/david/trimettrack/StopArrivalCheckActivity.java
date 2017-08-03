package com.example.david.trimettrack;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class StopArrivalCheckActivity extends AppCompatActivity {

    private String TAG = StopArrivalCheckActivity.class.getSimpleName();

    //Important data variable for API request
    private String stopId;
    private String appId;
    private String url;

    //JSON Parser Variable
    protected StopInfoJSONParser arrivalsResult;

    //List of Stop Information
    protected ListView lv;
    protected ListAdapter adapter;
    ArrayList<HashMap<String, String>> stopInfoList;

    //Obtain Current stop ID
    protected EditText currentStopId;

    //Variables
    protected TextView currentDir;
    protected TextView currentLoc;
    protected TextView errorTextBox;
    protected TextView noArrivalInfo;
    protected String curLocation;
    protected String direction;
    protected String errorContent;
    protected String arrivalInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_arrival_check);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            appId = bundle.getString("trimetAppID");
        } catch (Exception e) {
            Log.e(TAG, "You need to configure the meta-data first.");
        }


        //Get data that is being passed from Main Activity
        Bundle stopData = getIntent().getExtras();
        if (stopData == null)
            return;
        stopId = stopData.getString("stopIdInput");
        currentStopId = (EditText) findViewById(R.id.stopIdInputBox);
        currentStopId.setText(stopId);
        //Define the URL that going to be retrieve data from
        url = MessageFormat.format("https://developer.trimet.org/ws/V2/arrivals?locIDs={0}&appID={1}&json=true",
                                    stopId,appId);

        //Initialize a new Array List to store arrival information for a specific stop ID
        stopInfoList = new ArrayList<>();

        //Find listView box in which it is going to display arrival information
        lv = (ListView) findViewById(R.id.stopInfoListView);

        //Get the result and set content to ListView
        new GetStopInfo().execute(url);

    }

    public void onClick(View view) {
        hideKeyboard(view);

        //Define URL and the request data
        currentStopId = (EditText) findViewById(R.id.stopIdInputBox);
        stopId = currentStopId.getText().toString();
        url = MessageFormat.format("https://developer.trimet.org/ws/V2/arrivals?locIDs={0}&appID={1}&json=true",
                                    stopId,appId);

        //Clean the list before receive new information
        stopInfoList.clear();

        //Call method to retrieve a stop Arrivals result
        new GetStopInfo().execute(url);

        //Avoid stack too much unused memory
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

            String link = args[0];
            Log.e(TAG, "Response from url: " + link);

            try {
                //Parse the JSON response into Android Version
                arrivalsResult = new StopInfoJSONParser(link);
                stopInfoList = arrivalsResult.getStopInfoList();
                curLocation = arrivalsResult.getCurLocation();
                direction = arrivalsResult.getDirection();
                errorContent = arrivalsResult.getErrorContent();
                arrivalInfo = arrivalsResult.getArrivalInfo();

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

            //Display arrival information if it is being requested successfully.
            if (errorContent == null) {
                currentLoc = (TextView) findViewById(R.id.currentStopLocation);
                currentLoc.setText(curLocation);
                currentDir = (TextView) findViewById(R.id.currentLocationDir);
                currentDir.setText(direction);

                //Check arrivals information whether is empty or not
                if (stopInfoList.isEmpty()) {
                    noArrivalInfo = (TextView) findViewById(R.id.noArrivalInfo);
                    noArrivalInfo.setVisibility(View.VISIBLE);
                    noArrivalInfo.setText(arrivalInfo);

                    //Make TextView Invisible when no arrival found
                    errorTextBox = (TextView) findViewById(R.id.errorContent);
                    errorTextBox.setVisibility(View.GONE);

                } else {
                    //Put information to listView
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
                //Display Error message when request is not processed successfully.
                errorTextBox = (TextView) findViewById(R.id.errorContent);
                errorTextBox.setVisibility(View.VISIBLE);
                errorTextBox.setText(errorContent);

                //Make noArrivalFound Message TextView Invisible
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