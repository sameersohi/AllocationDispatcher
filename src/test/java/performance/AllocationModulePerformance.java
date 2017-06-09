package performance;

import domain.AllocationRequest;
import domain.AllocationResponse;
import mockData.PopulateTestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import processor.AllocationDispatcher;
import processor.AllocationSourceImpl;
import services.AllocationResponseServiceImpl;
import services.CalculateFeesChargesService;
import services.CalculateFeesChargesServiceImpl;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Sameer on 6/9/2017.
 */
public class AllocationModulePerformance {

    private static final int TotalRuns = 1;

    // Keep this size equal to the number of messages that needs to be tested
    private final int RECEIVER_QUEUE_SIZE = 100000;

    // Keep it to optimum size to prevent blocking
    private final int REQUEST_QUEUE = 10000;

    // Batch Size
    private final int BATCH_SIZE = 100;

    // Count for Parallel works
    final private int NO_THREADS = 4;

    AllocationSourceImpl allocationSource= new AllocationSourceImpl(
            new ArrayBlockingQueue<AllocationRequest>(REQUEST_QUEUE));
    CalculateFeesChargesService calculateFeesChargesService= new CalculateFeesChargesServiceImpl();
    AllocationResponseServiceImpl allocationResponseService= new AllocationResponseServiceImpl(
            new ArrayBlockingQueue<AllocationResponse>(RECEIVER_QUEUE_SIZE));

    AllocationDispatcher allocationDispatcher;

    @Before
    public void setUp() throws Exception {
        allocationDispatcher = new AllocationDispatcher(allocationSource,
                calculateFeesChargesService, allocationResponseService);
        allocationDispatcher.setMaxsize(BATCH_SIZE);
        allocationDispatcher.setAllocationProcessingThreadCount(NO_THREADS);
        new Thread(allocationDispatcher).start();
    }

    @Test
    public void testProcessor() {
        try {
            // For JVM warmup
            long totaltime = 0;
            PopulateTestData.addMockDatatoQueue(allocationSource, 1000);
            Thread.sleep(2000);
            while (allocationResponseService.responseBlockingQueue.size() != 1000) {
            }
            allocationResponseService.responseBlockingQueue.clear();

            System.out.println("Starting Performance - "
                    + allocationResponseService.responseBlockingQueue.size());
            for (int noRun = 1; noRun <= TotalRuns; noRun++) {
                long starttime = System.nanoTime();
                PopulateTestData.addMockDatatoQueue(allocationSource,
                        RECEIVER_QUEUE_SIZE);
                while (allocationResponseService.responseBlockingQueue.size() != RECEIVER_QUEUE_SIZE) {
                }
                long processing = System.nanoTime() - starttime;
                totaltime += processing;
                System.out.println("Total Processing Time Batch -> " + noRun
                        + ", ProcessingTime -> " + processing / (1000000)
                        + "ms");
                allocationResponseService.responseBlockingQueue.clear();
            }
            Thread.sleep(5000);
            System.out.println("Input - Message per Run -> "
                    + RECEIVER_QUEUE_SIZE + ", Batch Size -> " + BATCH_SIZE
                    + ", TotalAvgTime -> " + totaltime / (TotalRuns * 1000000) + "ms, TotalRuns -> " + TotalRuns);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        allocationDispatcher.shutDown();
    }
}
