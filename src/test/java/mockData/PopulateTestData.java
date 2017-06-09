package mockData;

import domain.AllocationRequest;
import processor.AllocationSourceImpl;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by Sameer on 6/9/2017.
 */
public class PopulateTestData {
    public static void addMockDatatoQueue(AllocationSourceImpl allocationSource, int noOfMessages) {
        try {
            String[] subAcc = { "ABCD", "DEFR", "HEDGCAP1",
                    "JPMFUNDSME", "BARCAPFUNDA" };
            Random subAccRandom = new Random();
            Random qty = new Random();
            for (int i = 0; i < noOfMessages; i++) {
                allocationSource.allocRequestQueue.put(new AllocationRequest(
                        subAcc[Math.abs(subAccRandom.nextInt(5))],
                        (long) (Math.abs(qty.nextInt(1000)) + 100),
                        "HKG",
                        //new BigDecimal(Math.abs(qty.nextInt(100)) + 10)
                        new BigDecimal(100)
                        ));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
