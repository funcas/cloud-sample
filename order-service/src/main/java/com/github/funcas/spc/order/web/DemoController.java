package com.github.funcas.spc.order.web;

import com.github.funcas.spc.order.feign.IUpmsService;
import jdk.internal.org.objectweb.asm.commons.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * TODO
 *
 * @author shane
 * @since 1.0
 */
@RestController
@RequestMapping("/api/order")
public class DemoController {

    @Autowired
    private IUpmsService upmsService;
    @Autowired
    private ApplicationContext ctx;

    @GetMapping("/sayHello")
    public String sayHello(String name) {
        Object dyBean = ctx.getBean("com.github.funcas.spc.lb.DyBean");
        ReflectionUtils.invokeMethod(Objects.requireNonNull(ReflectionUtils.findMethod(dyBean.getClass(), "init")), dyBean);
        return "Hello" + name;
    }

    @GetMapping("/remote/user")
    public Object getUser(String uid) {
        return upmsService.getUserInfo(uid);
    }

}

