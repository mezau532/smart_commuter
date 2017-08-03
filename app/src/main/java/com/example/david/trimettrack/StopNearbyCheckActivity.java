package com.example.david.trimettrack;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.text.MessageFormat;

public class StopNearbyCheckActivity extends AppCompatActivity {

    private String TAG = StopNearbyCheckActivity.class.getSimpleName();

    //Important data variable for API request
    private String longitude;
    private String latitude;
    private String checkRange = "500";
    private String appId;
    private String url;

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


    }
}
