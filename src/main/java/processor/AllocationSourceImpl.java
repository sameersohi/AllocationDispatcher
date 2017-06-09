package processor;

import domain.AllocationRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Sameer on 6/8/2017.
 */
public class AllocationSourceImpl implements AllocationSource {

    private static final Log logger = LogFactory.getLog(AllocationSourceImpl.class);

    public final BlockingQueue<AllocationRequest> allocRequestQueue;

    public AllocationSourceImpl(BlockingQueue<AllocationRequest> allocRequestQueue) {
        this.allocRequestQueue = allocRequestQueue;
    }

    public AllocationRequest getNextAllocation() {
        AllocationRequest allocationRequest = null;
        try{
            logger.info("Waiting on message");

            allocationRequest = allocRequestQueue.take();

            logger.debug("Message Received wait Completed");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return allocationRequest;
    }

    public List<AllocationRequest> getAllocationBatch(int maxSize) {

        List<AllocationRequest> allocationRequests = new ArrayList<AllocationRequest>();

        if (this.allocRequestQueue.size() < maxSize){
            this.allocRequestQueue.drainTo(allocationRequests);
        } else {
            this.allocRequestQueue.drainTo(allocationRequests,maxSize);
        }
        return allocationRequests;
    }
}
