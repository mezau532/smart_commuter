package com.example.david.trimettrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view){
        Intent i = new Intent(this, StopArrivalCheckActivity.class);

        final EditText stopIdInput = (EditText)findViewById(R.id.stopIdInputBox);
        String stopId = stopIdInput.getText().toString();
        i.putExtra("stopIdInput",stopId);

        startActivity(i);

    }

}
