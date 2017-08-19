package Sync.Info;

/**
 * Copyright (c) 2017 Ulysses Meza and Jinghui Han
 * This code is available under the "MIT License".
 * Please see the file LICENSE in this distribution for license terms
 */
/**
 * Created by umeza on 7/25/17.
 */

public class UberListOfCostEstimateDTOs {
    private UberCostEstimateDTO[] uberlistOfCostEstimates;

    public UberListOfCostEstimateDTOs(){
        this.uberlistOfCostEstimates = null;
    }

    public void setUberListOfCostEstimates(UberCostEstimateDTO[] theUberListOfCostEstimates) {
        if(theUberListOfCostEstimates == null){
            this.uberlistOfCostEstimates = null;
        }else {
            this.uberlistOfCostEstimates = theUberListOfCostEstimates.clone();
        }
    }

    public UberCostEstimateDTO getCheapest(){
        if(this.uberlistOfCostEstimates == null){
            return null;
        }

        int size = this.uberlistOfCostEstimates.length;
        int cheapest = 0;
        int i = 0;
        if(size == 0) {
            return null;
        }
        int current = 0;
        int tmp;
        int j = 0;
        while(j < size){
            if(this.uberlistOfCostEstimates[j].getHigh_estimate() > 0){
                current = this.uberlistOfCostEstimates[j].getHigh_estimate();
            }
            j++;
        }
        if(current == 0){
            return null;
        }
        while(i < size){
            tmp = this.uberlistOfCostEstimates[i].getHigh_estimate();
            if(tmp < current && tmp != 0){
                current = tmp;
                cheapest = i;
            }
            i++;
        }

        return this.uberlistOfCostEstimates[cheapest];

    }

    public UberCostEstimateDTO[] getListOfCostEstimates() {
        return this.uberlistOfCostEstimates;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
