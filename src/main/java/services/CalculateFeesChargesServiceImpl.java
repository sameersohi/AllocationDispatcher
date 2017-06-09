package services;

import domain.AllocationRequest;
import domain.AllocationResponse;
import exceptions.AllocationProcessingException;

import java.math.BigDecimal;

/**
 * Created by Sameer on 6/8/2017.
 */
public class CalculateFeesChargesServiceImpl implements CalculateFeesChargesService {

    public AllocationResponse processRequest(AllocationRequest allocationRequest) {

        try{
            return new AllocationResponse(allocationRequest, getFeesAndChargers(allocationRequest));
        } catch (AllocationProcessingException e) {
            e.printStackTrace();
            return new AllocationResponse(allocationRequest, new BigDecimal("0"));
        }
    }

    /**
     * Dummy Implementation of Calculating fees and charges . Added sleep with default timing
     * to mock expensive operation
     */

    public BigDecimal getFeesAndChargers(AllocationRequest allocationRequest) throws AllocationProcessingException{
        try {
            BigDecimal sampleCharges = new BigDecimal("234.456");
            Thread.sleep(2);
            if (allocationRequest.getAllocatedQty() < 1) {
                throw new AllocationProcessingException("Quantity 0 Cannot process");
            }
            return sampleCharges;
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        return new BigDecimal("0");

    }

}
