package Sync.Info;

/**
 * Created by umeza on 7/25/17.
 */

public class UberListOfCostEstimateDTOs {
    private UberCostEstimateDTO[] uberlistOfCostEstimates;

    public UberListOfCostEstimateDTOs(){
        this.uberlistOfCostEstimates = null;
    }

    public void setUberListOfCostEstimates(UberCostEstimateDTO[] uberListOfCostEstimates) {
        this.uberlistOfCostEstimates = uberlistOfCostEstimates.clone();
    }

    public UberCostEstimateDTO getCheapest(){
        if(this.uberlistOfCostEstimates == null){
            return null;
        }

        int size = this.uberlistOfCostEstimates.length;
        int cheapest = 0;
        int i = 0;
        if(size == 0){
            return null;
        }

        int current = this.uberlistOfCostEstimates[0].getHigh_estimate();
        int tmp;
        while(i < size){
            tmp = this.uberlistOfCostEstimates[i].getHigh_estimate();
            if(tmp < current){
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
