package com.github.funcas.spc.lb;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes());
        if(requestAttributes != null) {
            Map<String, String> headers = getHeaders(requestAttributes.getRequest());
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                template.header(entry.getKey(), entry.getValue());
            }
        }

    }

    /**
     * 获取原请求头
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                String value = request.getHeader(key);
                System.out.println("key=>" + key + " value=>" + value);
                //将灰度标记的请求头透传给下个服务
                if(GrayRule.KEY_HTTP_HEADER_VERSION.equalsIgnoreCase(key)) {
                    RibbonRequestContextHolder.getCurrentContext().put(GrayRule.KEY_VERSION, value);
                    map.put(key, value);
                }
            }
        }
        return map;
    }
}
