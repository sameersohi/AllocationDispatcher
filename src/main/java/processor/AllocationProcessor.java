package processor;

import domain.AllocationRequest;
import domain.AllocationResponse;
import services.CalculateFeesChargesService;

import java.util.concurrent.Callable;

/**
 * Created by Sameer on 6/8/2017.
 */
public class AllocationProcessor implements Callable<AllocationResponse> {

    private CalculateFeesChargesService calculateFeesChargesService;
    private AllocationRequest allocationRequest;

    public AllocationProcessor(CalculateFeesChargesService calculateFeesChargesService, AllocationRequest allocationRequest) {
        this.calculateFeesChargesService = calculateFeesChargesService;
        this.allocationRequest = allocationRequest;
    }

    public AllocationResponse call() throws Exception {
        return this.calculateFeesChargesService.processRequest(allocationRequest);
    }

}
