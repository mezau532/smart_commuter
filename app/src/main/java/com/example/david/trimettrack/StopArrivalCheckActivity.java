package com.example.david.trimettrack;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class StopArrivalCheckActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private final static String appId = "1D72A2E7E55A71C6646AD061E";

    private EditText stop;
    private TextView stopDetail;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_arrival_check);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Get data that is being passed from Main Activity
        Bundle stopData = getIntent().getExtras();
        if(stopData == null)
            return;
        String stopId = stopData.getString("stopIdInput");
        stop = (EditText) findViewById(R.id.stopIdInputBox);
        stop.setText(stopId);

        String url = "https://developer.trimet.org/ws/V2/arrivals?locIDs="+stopId+"&appID="+appId+"&json=true";
        HttpHandler http = new HttpHandler();
        String result =  http.makeServiceCall(url);
        stopDetail = (TextView)findViewById(R.id.ArrivalDetailTextBox);
        stopDetail.setText(result);
/*

        //Send Request to obtain data
        stopDetail  = (TextView)findViewById(R.id.ArrivalDetailTextBox);
        String url = "https://developer.trimet.org/ws/V2/arrivals?locIDs="+stopId+"&appID="+appId+"&json=true";
        RequestQueue request = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        stopDetail.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        stopDetail.setText("No Information");
                    }
                });

        // Access the RequestQueue through your singleton class.
        request.add(jsObjRequest);

*/
    }

    public void onClick(View view){

        stop = (EditText) findViewById(R.id.stopIdInputBox);
        String stopId = stop.getText().toString();
        stopDetail  = (TextView)findViewById(R.id.ArrivalDetailTextBox);
        String url = "https://developer.trimet.org/ws/V2/arrivals?locIDs="+stopId+"&appID="+appId+"&json=true";
        RequestQueue request = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        stopDetail.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        stopDetail.setText("No Information");
                    }
                });

        // Access the RequestQueue through your singleton class.
        request.add(jsObjRequest);
    }

}
