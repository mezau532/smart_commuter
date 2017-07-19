package com.example.david.trimettrack;

import LyftUber.LyftCalls;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

public class FindRideActivity extends AppCompatActivity {
    //don't know if I want these private or not?
    //TODO remove this comment once I decide what I want
    //TODO there will be no need for this if i implement location services
    public String StartLong;
    public String StartLat;
    public String DestinationLong;
    public String DestinationLat;
    public String BaseUrl = "http://api.lyft.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride);

    }
    public void onClick(View view){
        LyftCalls lyftCalls;
        TextView RideOutput;

        final EditText StartLatET = (EditText) findViewById(R.id.StartLatInputBox);
        final EditText StartLongET = (EditText) findViewById(R.id.StartlongInputBox);
        final EditText DestinationLatET = (EditText) findViewById(R.id.DestinationLatInputBox);
        final EditText DestinationLongET = (EditText) findViewById(R.id.DestinationLongInputBox);
        StartLat = StartLatET.getText().toString();
        StartLong = StartLongET.getText().toString();
        DestinationLat = DestinationLatET.getText().toString();
        DestinationLong = DestinationLongET.getText().toString();
        RideOutput = (TextView)findViewById(R.id.RideOutputBox);
        RideOutput.setText("comming soon");
    }
}
