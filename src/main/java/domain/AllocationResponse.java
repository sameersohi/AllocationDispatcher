package domain;

import java.math.BigDecimal;

/**
 * Created by Sameer on 6/8/2017.
 */
public class AllocationResponse {

    private final AllocationRequest allocationRequest;
    private BigDecimal appliedFeesandCharges;

    public AllocationResponse(AllocationRequest allocationRequest, BigDecimal appliedFeesandCharges) {
        this.allocationRequest = allocationRequest;
        this.appliedFeesandCharges = appliedFeesandCharges;
    }

    public AllocationRequest getAllocationRequest() {
        return allocationRequest;
    }

    public BigDecimal getAppliedFeesandCharges() {
        return appliedFeesandCharges;
    }

    public void setAppliedFeesandCharges(BigDecimal appliedFeesandCharges) {
        this.appliedFeesandCharges = appliedFeesandCharges;
    }
}
