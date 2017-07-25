package Sync.Info;

/**
 * Created by umeza on 7/24/17.
 */

public class UberCostEstimateDTO {
    String localized_display_name;
    double distance;
    String display_name;
    String product_id;
    int high_estimate;
    int low_estimate;
    int duration;
    String estimate;
    String currency_code;

    public UberCostEstimateDTO(){
        this.localized_display_name = null;
        this.distance = 0;
        this.display_name = null;
        this.product_id = null;
        this.high_estimate = 0;
        this.low_estimate = 0;
        this.duration = 0;
        this.estimate = null;
        this.currency_code = null;
    }

    public boolean equals(Object o){
        if(o == null || !(o instanceof CostEstimateDTO)){
            return false;
        }

        UberCostEstimateDTO other = (UberCostEstimateDTO) o;
        if(this.duration != other.duration){
            return false;
        }
        if(! this.display_name.equals(other.display_name)){
            return false;
        }

        return true;
    }

    public int hashCode(){
        return this.high_estimate * this.duration;
    }


    public void setLocalized_display_name(String localized_display_name) {
        this.localized_display_name = localized_display_name;
    }

    public String getLocalized_display_name() {
        return this.localized_display_name;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getDisplay_name() {
        return this.display_name;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_id() {
        return this.product_id;
    }

    public void setHigh_estimate(int high_estimate) {
        this.high_estimate = high_estimate;
    }

    public int getHigh_estimate() {
        return this.high_estimate;
    }

    public void setLow_estimate(int low_estimate) {
        this.low_estimate = low_estimate;
    }

    public int getLow_estimate() {
        return this.low_estimate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public String getEstimate() {
        return this.estimate;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getCurrency_code() {
        return this.currency_code;
    }
}