package com.github.funcas.spc.order.conf.ribbon;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.github.funcas.spc.order.conf.trace.TraceContext;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
@Configuration
public class GrayRule extends ZoneAvoidanceRule {

    public static final String KEY_VERSION = "version";

    public static final String KEY_VERSION_PROD = "prod";

    public static final String KEY_HTTP_HEADER_VERSION = "x-version";

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }

    @Override
    public Server choose(Object key) {

        //从ThreadLocal中获取灰度标记
        String grayVersion = TraceContext.getCurrentContext().get(KEY_VERSION);
        System.out.println("version===>" + grayVersion);
        if(grayVersion == null || "".equals(grayVersion)) {
            grayVersion = KEY_VERSION_PROD;
        }
        //获取所有可用服务
        List<Server> serverList = this.getLoadBalancer().getReachableServers();

        Map<String, List<Server>> serverMap = serverList.stream().collect(Collectors.groupingBy(item -> {
            NacosServer server = (NacosServer) item;
            if(!server.getMetadata().containsKey(KEY_VERSION)) {
                return KEY_VERSION_PROD;
            }
            return server.getMetadata().get(KEY_VERSION);
        }));
        System.out.println(serverMap);
        return originChoose(serverMap.get(grayVersion), key);


    }

    private Server originChoose(List<Server> noMetaServerList, Object key) {
        if(noMetaServerList == null) {
            return null;
        }
        return getPredicate().chooseRoundRobinAfterFiltering(noMetaServerList, key).orNull();
    }
}
