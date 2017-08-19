package Sync.Info;
/**
 * Copyright (c) 2017 Ulysses Meza and Jinghui Han
 * This code is available under the "MIT License".
 * Please see the file LICENSE in this distribution for license terms
 */
import static okhttp3.internal.Internal.instance;

/**
 * Created by umeza on 7/22/17.
 */

public class LocationDTO {
    private double lat;
    private double lng;

    @Override
    public int hashCode() {
        return (int) this.lat * (int) this.lng;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LocationDTO) || obj == null){
            return false;
        }

        LocationDTO other = (LocationDTO) obj;
        if(this.lat != other.getLat()){
            return false;
        }
        if(this.lng != other.getLng()){
            return false;
        }
        return true;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
