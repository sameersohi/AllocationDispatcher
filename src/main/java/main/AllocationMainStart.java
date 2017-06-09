package main;

import configuration.AllocationModuleConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import processor.AllocationDispatcher;

/**
 * Created by Sameer on 6/8/2017.
 */
public class AllocationMainStart {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AllocationModuleConfiguration.class);
        AllocationDispatcher allocationDispatcher = (AllocationDispatcher)applicationContext.getBean("AllocationDispatcher");
        allocationDispatcher.run();
        //Can create Prallel threads for allocationDispatcher to run parallel
    }
}
