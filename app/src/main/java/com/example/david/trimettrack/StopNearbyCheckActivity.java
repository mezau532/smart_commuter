package com.example.david.trimettrack;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.MessageFormat;

public class StopNearbyCheckActivity extends AppCompatActivity {

    private String TAG = StopNearbyCheckActivity.class.getSimpleName();

    //Important data variable for API request
    private String longitude;
    private String latitude;
    private String checkRange = "500";
    private String appId;
    private String url;

    //Data
    protected String r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_nearby_check);

        //Get Trimet APPID
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            appId = bundle.getString("trimetAppID");
        } catch (Exception e) {
            Log.e(TAG, "You need to configure the meta-data first.");
        }

        //Get data that are needed for the request from Main Activity
        Bundle locationData = getIntent().getExtras();
        if (locationData == null)
            return;
        latitude = locationData.getString("current_latitude");
        longitude = locationData.getString("current_longitude");


        //Define Request URL
        url = MessageFormat.format("https://developer.trimet.org/ws/V1/stops?ll" +
                "={0},{1}&meters={2}&appID={3}&json=true",latitude,longitude,checkRange,appId);

        new GetNearbyStop().execute(url);

    }

    private class GetNearbyStop extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "Nearby Stop Information is downloading");
        }

        //Retrieve Data from Trimet API
        @Override
        protected Void doInBackground(String... args) {

            String link = args[0];
            Log.e(TAG, "Response from url: " + link);

            try {
                //Parse the JSON response into Android Version
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                r = sh.makeServiceCall(url);

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
            TextView t = (TextView) findViewById(R.id.textView);
            t.setText(r);
        }
    }
}
