package services;

import domain.AllocationRequest;
import domain.AllocationResponse;


/**
 * Created by Sameer on 6/8/2017.
 */
public interface CalculateFeesChargesService {

    AllocationResponse processRequest(AllocationRequest allocationRequest);
}
