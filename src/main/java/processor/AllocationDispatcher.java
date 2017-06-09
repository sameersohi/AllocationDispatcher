package processor;

import domain.AllocationRequest;
import domain.AllocationResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import services.AllocationResponseService;
import services.AllocationResponseServiceImpl;
import services.CalculateFeesChargesService;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Sameer on 6/8/2017.
 */
public class AllocationDispatcher implements Runnable{

    private static Log logger = LogFactory.getLog(AllocationDispatcher.class);

    volatile boolean running = true;

    private AllocationSource allocationSource;
    private CalculateFeesChargesService feesChargesService;
    private AllocationResponseService allocationResponseService;

    @Value("${allocationsource.batch.size}")
    private int maxsize;

    @Value("${allocationprocessor.thread}")
    private int allocationProcessingThreadCount;

    @Inject
    public AllocationDispatcher( final AllocationSource allocationSource, final CalculateFeesChargesService feesChargesService,
                                 final AllocationResponseService allocationResponseService) {
        this.allocationSource = allocationSource;
        this.feesChargesService = feesChargesService;
        this.allocationResponseService = allocationResponseService;
    }

    public void shutDown(){
        this.running = false;
    }

    public void run() {

        ExecutorService executorService = Executors.newFixedThreadPool(this.allocationProcessingThreadCount);
        BlockingQueue<AllocationRequest> allocationProcessingQueue = new ArrayBlockingQueue<AllocationRequest>(this.maxsize);
        CompletionService completionService = new ExecutorCompletionService(executorService,allocationProcessingQueue);
        logger.info("All Initiated Starting the process");

        while(running) {

            List<AllocationRequest> allocationRequests = allocationSource.getAllocationBatch(this.maxsize);
            if(allocationRequests.size() == 0) {
                allocationRequests.add(allocationSource.getNextAllocation());
            }

            for(AllocationRequest allocationRequest: allocationRequests){
                completionService.submit(new AllocationProcessor( feesChargesService , allocationRequest));
            }

            for(int taskcounter = 0; taskcounter < allocationRequests.size();taskcounter ++){
                try {
                    this.allocationResponseService.sendResponse((AllocationResponse) completionService.take().get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            logger.debug("BatchSize [" + allocationRequests.size() + "] Processed");
        }
        executorService.shutdown();
        logger.info("Allocation Service Stopped");
    }


    public AllocationSource getAllocationSource() {
        return allocationSource;
    }

    public void setAllocationSource(AllocationSource allocationSource) {
        this.allocationSource = allocationSource;
    }

    public CalculateFeesChargesService getFeesChargesService() {
        return feesChargesService;
    }

    public void setFeesChargesService(CalculateFeesChargesService feesChargesService) {
        this.feesChargesService = feesChargesService;
    }

    public AllocationResponseService getAllocationResponseService() {
        return allocationResponseService;
    }

    public void setAllocationResponseService(AllocationResponseService allocationResponseService) {
        this.allocationResponseService = allocationResponseService;
    }

    public int getMaxsize() {
        return maxsize;
    }

    public void setMaxsize(int maxsize) {
        this.maxsize = maxsize;
    }

    public int getAllocationProcessingThreadCount() {
        return allocationProcessingThreadCount;
    }

    public void setAllocationProcessingThreadCount(int allocationProcessingThreadCount) {
        this.allocationProcessingThreadCount = allocationProcessingThreadCount;
    }
}
