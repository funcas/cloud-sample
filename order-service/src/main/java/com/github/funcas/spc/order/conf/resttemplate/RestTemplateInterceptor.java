package com.github.funcas.spc.order.conf.resttemplate;

import com.github.funcas.spc.order.conf.trace.TraceContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        addRequestHeader(request);
        return execution.execute(request, body);
    }

    // 向头部中加入一些信息
    private void addRequestHeader(HttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String version = TraceContext.getCurrentContext().get("version");
        headers.add("x-version",version);
    }
}
