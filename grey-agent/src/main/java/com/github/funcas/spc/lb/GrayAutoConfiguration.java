package com.github.funcas.spc.lb;

import org.springframework.context.annotation.Bean;

/**
 * TODO 添加Conditional
 *
 * @author Shane Fang
 * @since 1.0
 */
public class GrayAutoConfiguration {

    @Bean
    public GrayRule grayRule () {
        return new GrayRule();
    }

    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
