package com.example.david.trimettrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            //this code is to handle both buttons on main page
            //got it from stack overflow: https://stackoverflow.com/questions/16265883/how-to-create-multiple-onclick-methods-in-android
            Button button1 = (Button) findViewById(R.id.stopIdSearchButton);
            button1.setOnClickListener(this);

            Button button2 = (Button) findViewById(R.id.RideShareButton);
            button2.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.stopIdSearchButton) {
            Intent i = new Intent(this, StopArrivalCheckActivity.class);
            final EditText stopIdInput = (EditText) findViewById(R.id.stopIdInputBox);
            String stopId = stopIdInput.getText().toString();
            i.putExtra("stopIdInput", stopId);
            startActivity(i);
        }
        else if(view.getId() == R.id.RideShareButton){
            Intent i = new Intent(this, FindRideActivity.class);
            startActivity(i);
        }
    }

}
