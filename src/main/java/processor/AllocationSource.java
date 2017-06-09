package processor;

import domain.AllocationRequest;

import java.util.List;

/**
 * Created by Sameer on 6/8/2017.
 */
public interface AllocationSource {
    /**
     * Get the next allocation request. This will block until an allocation request arrives.
     */
    AllocationRequest getNextAllocation();

    /**
     * Get any queued allocation requests maxSize.This will
     * return immediately even if there are no waiting allocation requests.
     *
     * @param maxSize
     * maximum number of allocation requests to receive.
     */

    List<AllocationRequest> getAllocationBatch(int maxSize);

}
