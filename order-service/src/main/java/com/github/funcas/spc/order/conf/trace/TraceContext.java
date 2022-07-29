package com.github.funcas.spc.order.conf.trace;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
public class TraceContext {

    private static final TransmittableThreadLocal<RibbonRequestContext> holder = TransmittableThreadLocal.withInitial(RibbonRequestContext::new);

    public static RibbonRequestContext getCurrentContext() {
        return holder.get();
    }

    public static void setCurrentContext(RibbonRequestContext context) {
        holder.set(context);
    }

    public static void clearContext() {
        holder.remove();
    }
}
