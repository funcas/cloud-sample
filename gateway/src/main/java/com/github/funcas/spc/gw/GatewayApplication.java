package com.github.funcas.spc.gw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@LoadBalancerClients(defaultConfiguration = GrayLbConfiguration.class)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
