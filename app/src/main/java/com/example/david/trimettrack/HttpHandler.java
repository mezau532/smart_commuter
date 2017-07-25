package com.example.david.trimettrack;

/**
 * Created by David on 7/18/2017.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * This class is copied from the TutorialsPoint site. I found out it is a pretty
 * solution to solve my http request problem so I will be using this class
 * throughout my app as the httphandler.
 * If you want to read more detail about the codes below, please
 * visit https://www.tutorialspoint.com/android/android_json_parser.html.
 */

public class HttpHandler {

    public static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } /*catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        */
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}


/**
 * Volley request
 *
 *
 * RequestQueue request = Volley.newRequestQueue(this);
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
 *
 *
 */
