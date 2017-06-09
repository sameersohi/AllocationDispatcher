package domain;

import java.math.BigDecimal;

/**
 * Created by Sameer on 6/8/2017.
 */
public class AllocationRequest {

    private final String subAccount;
    private final Long allocatedQty;
    private final String ctry;
    private BigDecimal avgPrice;

    public AllocationRequest(String subAccount, Long allocatedQty, String ctry, BigDecimal avgPrice) {
        this.subAccount = subAccount;
        this.allocatedQty = allocatedQty;
        this.ctry = ctry;
        this.avgPrice = avgPrice;
    }


    public String getSubAccount() {
        return subAccount;
    }

    public Long getAllocatedQty() {
        return allocatedQty;
    }

    public String getCtry() {
        return ctry;
    }

    public BigDecimal getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(BigDecimal avgPrice) {
        this.avgPrice = avgPrice;
    }
}
