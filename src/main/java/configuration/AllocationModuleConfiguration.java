package configuration;

import domain.AllocationRequest;
import domain.AllocationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import processor.AllocationDispatcher;
import processor.AllocationSource;
import processor.AllocationSourceImpl;
import services.AllocationResponseService;
import services.AllocationResponseServiceImpl;
import services.CalculateFeesChargesService;
import services.CalculateFeesChargesServiceImpl;

import javax.inject.Named;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Sameer on 6/8/2017.
 */

@Configuration
@PropertySource("classpath:app.conf")
public class AllocationModuleConfiguration {

    @Value("${request.blockingqueue.size}")
    private int requestQueueSize;

    @Value("${response.blockingqueue.size}")
    private int responseQueueSize;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "AllocationSource")
    public AllocationSource getAllocationSource(){
        return new AllocationSourceImpl(new ArrayBlockingQueue<AllocationRequest>(requestQueueSize));
    }

    @Bean(name = "CalculateFeesChargesService")
    public CalculateFeesChargesService getChargesSerives(){
        return new CalculateFeesChargesServiceImpl();
    }

    @Bean(name = "AllocationResponseService")
    public AllocationResponseService getAllocationResponseService(){
        return new AllocationResponseServiceImpl(new ArrayBlockingQueue<AllocationResponse>(this.responseQueueSize));
    }

    @Bean(name = "AllocationDispatcher")
    public AllocationDispatcher getAllocationDispatcher(@Named("AllocationSource") AllocationSource allocationSource,
                                                        @Named("CalculateFeesChargesService") CalculateFeesChargesService feesChargesService,
                                                        @Named("AllocationResponseService") AllocationResponseService allocationResponseService){
        return new AllocationDispatcher(allocationSource,feesChargesService,allocationResponseService);
    }


}
