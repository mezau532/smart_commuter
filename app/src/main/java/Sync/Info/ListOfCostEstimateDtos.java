package Sync.Info;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
/**
 * Copyright (c) 2017 Ulysses Meza and Jinghui Han
 * This code is available under the "MIT License".
 * Please see the file LICENSE in this distribution for license terms
 */
/**
 * Created by umeza on 7/20/17.
 */

public class ListOfCostEstimateDtos
{
    private CostEstimateDTO[] listOfCostEstimates;

    public ListOfCostEstimateDtos(){
        this.listOfCostEstimates = null;
    }

    public void setListOfCostEstimates(CostEstimateDTO[] listOfCostEstimates) {
        this.listOfCostEstimates = listOfCostEstimates.clone();
    }

    public CostEstimateDTO getCheapest(){
        /*
        if(this.listOfCostEstimates == null){
            return null;
        }

        int size = this.listOfCostEstimates.length;
        int cheapest = 0;
        int i = 0;
        if(size == 0){
            return null;
        }

        int current = this.listOfCostEstimates[0].getEstimated_cost_cents_max();
        int tmp;
        while(i < size){
            tmp = this.listOfCostEstimates[i].getEstimated_cost_cents_max();
            if(tmp < current){
                current = tmp;
                cheapest = i;
            }
            i++;
        }

        return this.listOfCostEstimates[cheapest];
        */
        if(this.listOfCostEstimates == null){
            return null;
        }

        int size = this.listOfCostEstimates.length;
        int cheapest = 0;
        int i = 0;
        if(size == 0) {
            return null;
        }
        int current = 0;
        int tmp;
        int j = 0;
        while(j < size){
            if(this.listOfCostEstimates[j].getEstimated_cost_cents_max() > 0){
                current = this.listOfCostEstimates[j].getEstimated_cost_cents_max();
            }
            j++;
        }
        if(current == 0){
            return null;
        }
        while(i < size){
            tmp = this.listOfCostEstimates[i].getEstimated_cost_cents_min();
            if(tmp < current && tmp != 0){
                current = tmp;
                cheapest = i;
            }
            i++;
        }

        return this.listOfCostEstimates[cheapest];

    }

    public CostEstimateDTO[] getListOfCostEstimates() {
        return listOfCostEstimates;
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
