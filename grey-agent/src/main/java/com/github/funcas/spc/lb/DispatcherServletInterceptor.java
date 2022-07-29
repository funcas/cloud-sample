package com.github.funcas.spc.lb;

import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
public class DispatcherServletInterceptor {
    @RuntimeType
    public static void intercept(@Argument(0) Object o1, @Origin Method method, @SuperCall Callable<?> callable) throws Exception {

        try {
            HttpServletRequest request = (HttpServletRequest) o1;
            System.out.println("intercepter dispatcher ==>" + request.getHeader("x-version"));
            // TODO: 2022/7/26 set ttl
            RibbonRequestContextHolder.getCurrentContext().put(GrayRule.KEY_VERSION, request.getHeader("x-version"));
            callable.call();
        } catch (Exception e) {
            // 进行异常信息上报

            throw e;
        } finally {
            // todo remove ttl
            RibbonRequestContextHolder.clearContext();
        }
    }
}
