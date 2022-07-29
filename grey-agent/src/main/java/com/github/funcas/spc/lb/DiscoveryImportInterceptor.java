package com.github.funcas.spc.lb;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
public class DiscoveryImportInterceptor {

    @RuntimeType
    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {

        try {
            String[] imports = (String[])callable.call();
            List<String> tmp =  new ArrayList<>(Arrays.asList(imports));
            tmp.add("com.github.funcas.spc.lb.GrayAutoConfiguration");
            return tmp.toArray(new String[0]);
        } catch (Exception e) {
            // 进行异常信息上报

            throw e;
        }
    }
}
