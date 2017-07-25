package Sync;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import Sync.Info.CostEstimateDTO;
import Sync.Info.ListOfCostEstimateDtos;
import Sync.Info.UberCostEstimateDTO;
import Sync.Info.UberListOfCostEstimateDTOs;

/**
 * Created by umeza on 7/25/17.
 */

public class UberSync {
    private String baseUrl = "https://api.uber.com/v1.2/";
    private String results;
    private UberCostEstimateDTO[] costEstimates;
    private String serverToken = "XtW6q7Yu7QSHxAfUhcAQTtbkVemZoHAH7XTeIDqi";

    public UberCostEstimateDTO[] getCostEstimates(double startLat, double startLng, double endLng, double endlat){
        try{
            String url = MessageFormat.format(
                    "https://api.uber.com/v1.2/estimates/price?start_latitude={0}&start_longitude={1}&end_latitude={2}&end_longitude={3}",
                    startLat, startLng, endlat, endLng);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
//            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Authorization", "Bearer " + serverToken);
            con.setRequestProperty("Accept-Language", "en_US");

            int responseCode = con.getResponseCode();

            if(responseCode != 200){
                return null;
            }
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            this.results = response.toString();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
//            in.close();

            //print result
           System.out.println(response.toString());
//            this.results = response.toString();
           return parseEstimates(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private UberCostEstimateDTO[] parseEstimates(String json){
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = (JsonObject) parser.parse(json);
        JsonElement jsonElement = jsonObj.get("prices");
        String jsonString = jsonElement.toString();
        UberCostEstimateDTO[] uberCostEstimates = new GsonBuilder().create().fromJson(jsonString, UberCostEstimateDTO[].class);
/*
        int size = uberCostEstimates.length;
        UberListOfCostEstimateDTOs uberList = new UberListOfCostEstimateDTOs();
        uberList.setUberListOfCostEstimates(uberCostEstimates);
        UberCostEstimateDTO cheapestUber = uberList.getCheapest();
*/
        return uberCostEstimates;
    }
}
