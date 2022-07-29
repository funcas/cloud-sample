package com.github.funcas.spc.order.conf.feign;


import com.github.funcas.spc.order.conf.ribbon.GrayRule;
import com.github.funcas.spc.order.conf.trace.TraceContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        System.out.println("feign interceptor => " + TraceContext.getCurrentContext().get(GrayRule.KEY_VERSION));
        template.header(GrayRule.KEY_HTTP_HEADER_VERSION, TraceContext.getCurrentContext().get(GrayRule.KEY_VERSION));
    }
}
