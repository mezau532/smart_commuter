package Sync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;

import Sync.Info.LocationDTO;

/**
 * Created by umeza on 7/20/17.
 */

public class GoogleGeocodeSync {
    private LocationDTO location;
    String add;
    public String getAddress(String lat, String lng, String key){
        try {
            String googleUrl = MessageFormat.format(
                    "https://maps.googleapis.com/maps/api/geocode/json?latlng={0},{1}&key={2}",
                    lat, lng, key);
            URL url;
            url = new URL(googleUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if(responseCode != 200){
                return null;
            }

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }

            return parseAddress(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parseAddress(String result){
        JsonParser parser = new JsonParser();
        JsonObject json = (JsonObject) parser.parse(result);

        JsonElement jsonCode = json.get("status");
        String code = jsonCode.getAsString();
        if(! (code.equals("OK"))){
            return null;
        }

        JsonArray jsonArray = json.getAsJsonArray("results");
        JsonElement elm = jsonArray.get(0);
        JsonObject obj = elm.getAsJsonObject();
        JsonElement ob2 = obj.get("formatted_address");
        return ob2.getAsString();
    }
    public LocationDTO getCoordinates(String address, String key){
        try {
            String encodedAdd = URLEncoder.encode(address, "UTF-8");
            String googleUrl = MessageFormat.format(
                    "https://maps.googleapis.com/maps/api/geocode/json?address={0}&key={1}",
                    encodedAdd, key);
            URL url = null;
            url = new URL(googleUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
          if(responseCode != 200){
                return null;
           }

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String results = response.toString();
            while ((inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }

            return parseLocation(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public LocationDTO parseLocation(String result){
        JsonParser parser = new JsonParser();
        JsonObject json = (JsonObject) parser.parse(result);

        JsonElement jsonCode = json.get("status");
        String code = jsonCode.getAsString();
        if(! (code.equals("OK"))){
            return null;
        }

        JsonArray jsonArray = json.getAsJsonArray("results");
        JsonElement elm = jsonArray.get(0);
        JsonObject obj = elm.getAsJsonObject();
        JsonObject ob2 = obj.getAsJsonObject("geometry");
        JsonObject obj3 = ob2.getAsJsonObject("location");
        String jsonString = obj3.toString();

        Gson gson = new GsonBuilder().create();
        LocationDTO loc = gson.fromJson(jsonString, LocationDTO.class);

        return loc;
    }
}
