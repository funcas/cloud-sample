package com.github.funcas.spc.gw;

import com.alibaba.cloud.nacos.NacosServiceInstance;
import com.alibaba.nacos.api.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
public class GrayRoundRobinLoadBalancer extends RoundRobinLoadBalancer {

    private static final Logger log = LoggerFactory.getLogger(GrayRoundRobinLoadBalancer.class);
    public GrayRoundRobinLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        super(serviceInstanceListSupplierProvider, serviceId);
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    private final String serviceId;

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(serviceInstances -> getInstanceResponse(serviceInstances, request));
    }

    Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, Request request) {

        // 注册中心无可用实例 抛出异常
        if (CollectionUtils.isEmpty(instances)) {
            log.warn("No instance available {}", serviceId);
            return new EmptyResponse();
        }

        DefaultRequestContext requestContext = (DefaultRequestContext) request.getContext();
        RequestData clientRequest = (RequestData) requestContext.getClientRequest();
        HttpHeaders headers = clientRequest.getHeaders();

        String reqVersion = headers.getFirst("x-version");
        log.error(reqVersion);
        if (StringUtils.isEmpty(reqVersion)) {
            return super.choose(request).block();
        }

        // 遍历可以实例元数据，若匹配则返回此实例
        for (ServiceInstance instance : instances) {
            NacosServiceInstance nacosInstance = (NacosServiceInstance) instance;
            Map<String, String> metadata = nacosInstance.getMetadata();
            String targetVersion = metadata.get("version");
            if (reqVersion.equalsIgnoreCase(targetVersion)) {
                log.debug("gray requst match success :{} {}", reqVersion, nacosInstance);
                return new DefaultResponse(nacosInstance);
            }
        }
        // 降级策略，使用轮询策略
        return super.choose(request).block();
    }
}
