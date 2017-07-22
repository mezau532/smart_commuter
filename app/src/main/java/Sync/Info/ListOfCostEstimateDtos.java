package Sync.Info;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by umeza on 7/20/17.
 */

public class ListOfCostEstimateDtos
{
    private List<CostEstimateDTO> listOfCostEstimates;

    public void setListOfCostEstimates(List<CostEstimateDTO> listOfCostEstimates) {
        Collections.copy(this.listOfCostEstimates, listOfCostEstimates);
    }

    public List<CostEstimateDTO> getListOfCostEstimates() {
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
