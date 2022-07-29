package com.github.funcas.spc.lb;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

/**
 * TODO
 *
 * @author shane
 * @since 1.0
 */
public class GreyLbAgent {
    /**
     * jvm 参数形式启动，运行此方法
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        new AgentBuilder.Default()
                .type(ElementMatchers.named("org.springframework.cloud.client.discovery.EnableDiscoveryClientImportSelector"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("selectImports"))
                        .intercept(MethodDelegation.to(DiscoveryImportInterceptor.class))
                )
                .installOn(inst);

        new AgentBuilder.Default()
                .type(ElementMatchers.named("org.springframework.web.servlet.DispatcherServlet"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("doDispatch"))
                        .intercept(MethodDelegation.to(DispatcherServletInterceptor.class))
                )
                .installOn(inst);

    }

    /**
     * 动态 attach 方式启动，运行此方法
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("agentmain");
    }
}
