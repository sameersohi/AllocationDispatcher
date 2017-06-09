package services;

import domain.AllocationResponse;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Sameer on 6/8/2017.
 */
public class AllocationResponseServiceImpl implements AllocationResponseService {

    public final BlockingQueue<AllocationResponse> responseBlockingQueue;

    public AllocationResponseServiceImpl(BlockingQueue<AllocationResponse> responseBlockingQueue) {
        this.responseBlockingQueue = responseBlockingQueue;
    }

    public void sendResponse(AllocationResponse response) {
        try{
            this.responseBlockingQueue.put(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
