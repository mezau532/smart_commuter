package Sync.Info;

import java.text.MessageFormat;

/**
 * Created by umeza on 7/20/17.
 */

public class CostEstimateDTO {
    private String ride_type;
    private int estimated_duration_seconds;
    private double estimated_distance_miles;
    private int estimated_cost_cents_max;
    private String primetime_percentage;
    private boolean is_valid_estimate;
    private String currency;
    private String cost_token;
    private int estimated_cost_cents_min;
    private String dysplay_name;
    private String primetime_confirmation_token;
    private boolean can_request_ride;

    CostEstimateDTO(){
        this.ride_type = null;
        this.estimated_duration_seconds = 0;
        this.estimated_distance_miles = 0;
        this.estimated_cost_cents_max = 0;
        this.primetime_percentage = null;
        this.is_valid_estimate = false;
        this.currency = null;
        this.cost_token = null;
        this.estimated_cost_cents_min = 0;
        this.dysplay_name = null;
        this.primetime_confirmation_token = null;
        this.can_request_ride = false;
    }

    public boolean equals(Object o){
        if(o == null || !(o instanceof CostEstimateDTO)){
            return false;
        }

        CostEstimateDTO other = (CostEstimateDTO) o;
        if(this.estimated_duration_seconds != other.estimated_duration_seconds){
            return false;
        }
        if(! this.ride_type.equals(other.ride_type)){
            return false;
        }

        return true;
    }

    public int hashCode(){
        return this.estimated_cost_cents_max * this.estimated_duration_seconds;
    }


    public void setRide_type(String ride_type){
        this.ride_type = ride_type;
    }
    public String getRide_type(){
        return this.ride_type;
    }
    public void setEstimated_duration_seconds(int estimated_duration_seconds){
        this.estimated_duration_seconds = estimated_duration_seconds;
    }
    public int getEstimated_duration_seconds(){
        return this.estimated_duration_seconds;
    }
    public void setEstimated_distance_miles(double estimated_distance_miles){
        this.estimated_distance_miles = estimated_distance_miles;
    }
    public double getEstimated_distance_miles(){
        return this.estimated_distance_miles;
    }
    public void setEstimated_cost_cents_max(int estimated_cost_cents_max){
        this.estimated_cost_cents_max = estimated_cost_cents_max;
    }
    public int getEstimated_cost_cents_max(){
        return this.estimated_cost_cents_max;
    }
    public void setPrimetime_percentage(String primetime_percentage){
        this.primetime_percentage = primetime_percentage;
    }
    public String getPrimetime_percentage(){
        return this.primetime_percentage;
    }
    public void setIs_valid_estimate(boolean is_valid_estimate){
        this.is_valid_estimate = is_valid_estimate;
    }
    public boolean isIs_valid_estimate(){
        return this.is_valid_estimate;
    }
    public void setCurrency(String currency){
        this.currency = currency;
    }
    public String getCurrency(){
        return this.currency;
    }
    public void setCost_token(String cost_token){
        this.cost_token = cost_token;
    }
    public String getCost_token(){
        return this.cost_token;
    }
    public void setEstimated_cost_cents_min(int estimated_cost_cents_min){
        this.estimated_cost_cents_min = estimated_cost_cents_min;
    }
    public int getEstimated_cost_cents_min(){
        return  this.estimated_cost_cents_min;
    }
    public void setDysplay_name(String dysplay_name){
        this.dysplay_name = dysplay_name;
    }
    public String getDysplay_name(){
        return this.dysplay_name;
    }
    public void setPrimetime_confirmation_token(String primetime_confirmation_token){
        this.primetime_confirmation_token = primetime_confirmation_token;
    }
    public String getPrimetime_confirmation_token(){
        return this.primetime_confirmation_token;
    }
    public void setCan_request_ride(boolean can_request_ride){
        this.can_request_ride = can_request_ride;
    }
    public boolean isCan_request_ride() {
        return this.can_request_ride;
    }
}
