package com.github.funcas.spc.order.conf.resttemplate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * 对RestTemplate Bean做增强处理，在bean实例化阶段动态添加过滤器，添加头信息
 *
 * @author Shane Fang
 * @since 1.0
 */
@Component
public class RestTemplateBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RestTemplate) {
            RestTemplate restTemplate = (RestTemplate) bean;

            restTemplate.setInterceptors(Collections.singletonList(new RestTemplateInterceptor()));
        }
        return bean;
    }

}
