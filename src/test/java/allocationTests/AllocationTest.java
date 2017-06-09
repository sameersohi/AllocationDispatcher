package allocationTests;

import domain.AllocationRequest;
import domain.AllocationResponse;
import exceptions.AllocationProcessingException;
import mockData.PopulateTestData;
import org.junit.Before;
import org.junit.Test;
import processor.AllocationDispatcher;
import processor.AllocationSourceImpl;
import services.AllocationResponseServiceImpl;
import services.CalculateFeesChargesService;
import services.CalculateFeesChargesServiceImpl;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sameer on 6/9/2017.
 */
public class AllocationTest {

    public AllocationSourceImpl allocationSource;
    public CalculateFeesChargesServiceImpl feesChargesService;
    public AllocationResponseServiceImpl allocationResponseService;
    AllocationDispatcher allocationDispatcher;

    @Before
    public void setup() {
        allocationSource = new AllocationSourceImpl(new ArrayBlockingQueue<AllocationRequest>(10));
        feesChargesService = new CalculateFeesChargesServiceImpl();
        allocationResponseService = new AllocationResponseServiceImpl(new ArrayBlockingQueue<AllocationResponse>(10));
        allocationDispatcher = new AllocationDispatcher(allocationSource, feesChargesService,
                allocationResponseService);
    }

    @Test
    public void validateBactchRead() {
        PopulateTestData.addMockDatatoQueue(allocationSource, 10);
        assertEquals(10, allocationSource.getAllocationBatch(10).size());
    }

    @Test
    public void lessNoofMessageinQueueReadBach() {
        PopulateTestData.addMockDatatoQueue(allocationSource, 5);
        assertEquals(5, allocationSource.getAllocationBatch(10).size());
    }

    @Test
    public void validateReturnTypeOfDecisionService() {
        assertTrue(feesChargesService.processRequest(new AllocationRequest(
                "TESTFUND",  1500L, "SG", new BigDecimal(100))) instanceof AllocationResponse);

    }

    @Test(expected = AllocationProcessingException.class)
    public void validateExceptionforProcessing()
            throws AllocationProcessingException {
        feesChargesService.getFeesAndChargers(new AllocationRequest(
                "TESTFUND",  0L, "SG", new BigDecimal(100)));

    }

}
