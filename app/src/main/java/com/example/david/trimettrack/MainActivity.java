package com.example.david.trimettrack;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity { //implements View.OnClickListener {

    protected EditText stopIdInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Check Permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},10);
            }
        }

        //Old codes -- replaced by above code (can be removed)
        /**
        //this code is to handle both buttons on main page
        //got it from stack overflow: https://stackoverflow.com/questions/16265883/how-to-create-multiple-onclick-methods-in-android
        //stopIdSearchButton.setOnClickListener(this);

        //button2 = (Button) findViewById(R.id.RideShareButton);
        // button2.setOnClickListener(this);
         */


    }

    public void onClick(View view) {
        Intent i;

        switch (view.getId()){

            //Button for Trimet Service
            case R.id.stopIdSearchButton:

                CheckBox searchNearbyStopsCheckBox = (CheckBox) findViewById(R.id.searchNearbyStopsCheckBox);
                boolean checked = searchNearbyStopsCheckBox.isChecked();
                if(checked){

                    //Get current location data (latitude, longitude)
                    GetCurrentLocation location = new GetCurrentLocation(this);
                    location.getLocation();
                    String latitude = location.getLatitude();
                    String longitude = location.getLongitude();

                    //Goto Check_Nearby_Stops_Activity
                    i = new Intent(this, StopNearbyCheckActivity.class);
                    i.putExtra("current_latitude",latitude);
                    i.putExtra("current_longitude",longitude);
                    startActivity(i);
                }
                else{
                    //if checkbox is unchecked, goto Stop Arrivals Information activity
                    i = new Intent(this, StopArrivalCheckActivity.class);
                    stopIdInput = (EditText) findViewById(R.id.stopIdInputBox);
                    String stopId = stopIdInput.getText().toString();
                    i.putExtra("stopIdInput", stopId);
                    startActivity(i);
                }
                break;

            //Button for Lyft/Uber Service
            case R.id.RideShareButton:
                i = new Intent(this, FindRideActivity.class);
                //   Intent i = new Intent(this, UberLyftListActivity.class);
                startActivity(i);
                break;

            default:
                break;

        }

        //Old codes -- replaced by above code (can be removed)
        /**
        if(view.getId() == R.id.stopIdSearchButton) {
            Intent i = new Intent(this, StopArrivalCheckActivity.class);
            stopIdInput = (EditText) findViewById(R.id.stopIdInputBox);
            String stopId = stopIdInput.getText().toString();
            i.putExtra("stopIdInput", stopId);
            startActivity(i);
        }
        else if(view.getId() == R.id.RideShareButton){
            Intent i = new Intent(this, FindRideActivity.class);
         //   Intent i = new Intent(this, UberLyftListActivity.class);
            startActivity(i);
        }
         */
    }

    public void onCheckboxClicked(View view){

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.searchNearbyStopsCheckBox:
                if(checked){
                    stopIdInput = (EditText) findViewById(R.id.stopIdInputBox);
                    stopIdInput.setEnabled(false);
                }
                else{
                    stopIdInput = (EditText) findViewById(R.id.stopIdInputBox);
                    stopIdInput.setEnabled(true);
                }
                break;
            default:
                break;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2]==PackageManager.PERMISSION_GRANTED)) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this,"You need enable Location Service to use this feature",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

}
