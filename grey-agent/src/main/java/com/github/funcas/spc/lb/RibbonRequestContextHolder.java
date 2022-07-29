package com.github.funcas.spc.lb;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
public class RibbonRequestContextHolder {

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
