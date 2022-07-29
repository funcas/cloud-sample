package com.github.funcas.spc.gw;

import com.alibaba.cloud.circuitbreaker.sentinel.SentinelConfigBuilder;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.DispatcherHandler;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
@Component
public class SpringCloudCircuitBreakerSentinelFilterFactory extends SpringCloudCircuitBreakerFilterFactory {
    public static final Logger log = LoggerFactory.getLogger(SpringCloudCircuitBreakerSentinelFilterFactory.class);
    @Autowired(required = false)
    public SpringCloudCircuitBreakerSentinelFilterFactory (
            ReactiveCircuitBreakerFactory<SentinelConfigBuilder.SentinelCircuitBreakerConfiguration, SentinelConfigBuilder>
                    reactiveCircuitBreakerFactory,
            ObjectProvider<DispatcherHandler> dispatcherHandlerProvider) {
        super(reactiveCircuitBreakerFactory, dispatcherHandlerProvider);
        if (reactiveCircuitBreakerFactory == null) {
            log.warn("ReactiveCircuitBreakerFactory must be required!");
            return;
        }
        List<DegradeRule> degradeRules = new ArrayList<>();
        degradeRules.add(new DegradeRule()
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                .setCount(30000.0d)
                .setMinRequestAmount(30)
                .setTimeWindow(10)
                .setStatIntervalMs(1000)
                .setSlowRatioThreshold(0.8d));
        Function<String, SentinelConfigBuilder.SentinelCircuitBreakerConfiguration> defaultConfiguration =
                id -> new SentinelConfigBuilder().resourceName(id).rules(degradeRules).build();
        reactiveCircuitBreakerFactory.configureDefault(defaultConfiguration);
    }

    @Override
    protected Mono<Void> handleErrorWithoutFallback(Throwable t) {
        if (t instanceof DegradeException) {
            return Mono.error(new RuntimeException("500"));
        }
        return Mono.error(t);
    }
}
