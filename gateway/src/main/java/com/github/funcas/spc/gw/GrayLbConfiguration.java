package com.github.funcas.spc.gw;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
public class GrayLbConfiguration {

    @Bean
    public ReactorServiceInstanceLoadBalancer reactorServiceInstanceLoadBalancer(Environment environment,
                                                                                 ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider){
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new GrayRoundRobinLoadBalancer(serviceInstanceListSupplierProvider, name);
    }
}
