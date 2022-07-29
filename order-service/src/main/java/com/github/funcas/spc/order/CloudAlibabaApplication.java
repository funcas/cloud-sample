package com.github.funcas.spc.order;

import com.github.funcas.spc.order.conf.webmvc.GrayContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringCloudApplication
@EnableDiscoveryClient
@EnableAsync
@EnableFeignClients(basePackages = "com.github.funcas.spc.order.feign")
public class CloudAlibabaApplication {


    public static void main(String[] args) {
        SpringApplication.run(CloudAlibabaApplication.class, args);
    }

    @Bean
    WebMvcConfigurer createWebMvcConfigurer(@Autowired GrayContextInterceptor interceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(interceptor);
            }
        };
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}


//-Xmx128m
//-javaagent:C:\Users\funcas\Desktop\cloud-sample\grey-agent\target\grey-agent-0.0.1-SNAPSHOT-jar-with-dependencies.jar
//-javaagent:C:\Users\funcas\.m2\repository\com\alibaba\transmittable-thread-local\2.13.2\transmittable-thread-local-2.13.2.jar