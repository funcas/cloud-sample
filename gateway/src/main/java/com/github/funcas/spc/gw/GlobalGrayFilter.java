//package com.github.funcas.spc.gw;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * TODO
// *
// * @author Shane Fang
// * @since 1.0
// */
//@Component
//public class GlobalGrayFilter implements GlobalFilter {
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        HttpHeaders headers = exchange.getRequest().getHeaders();
//        if (headers.containsKey("x-version")){
//            String gray = headers.getFirst("x-version");
//            if (StringUtils.isNotEmpty(gray)){
//                //②设置灰度标记
//                TraceContext.getCurrentContext().put("version", gray);
//            }
//        }
//        //③ 将灰度标记放入请求头中
//        ServerHttpRequest tokenRequest = exchange.getRequest().mutate()
//                // 将灰度标记传递过去
//                .header("x-version", TraceContext.getCurrentContext().get("version"))
//                .build();
//        ServerWebExchange build = exchange.mutate().request(tokenRequest).build();
//        return chain.filter(build);
//
//
//    }
//}
