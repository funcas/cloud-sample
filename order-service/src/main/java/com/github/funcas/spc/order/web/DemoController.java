package com.github.funcas.spc.order.web;

import com.github.funcas.spc.order.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
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

    public static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private DemoService demoService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/sayHello")
    public String sayHello(String name) {
        Object dyBean = ctx.getBean("dyBean");
        ReflectionUtils.invokeMethod(Objects.requireNonNull(ReflectionUtils.findMethod(dyBean.getClass(), "init")), dyBean);
        return "Hello" + name;
    }

    @GetMapping("/test/spring-task-pool")
    public Object testRunInSpringTaskPool() {
        demoService.doInSpringTaskPool();
        return "success";
    }

    @GetMapping("/test/parallel")
    public Object testParallel() {
        demoService.doInParallelStream();
        return "success";
    }

    @GetMapping("/test/juc-thread-pool")
    public Object getUser(String uid, HttpServletRequest request) {
        demoService.doInExecutorService(uid);
        return "success";//upmsService.getUserInfo(uid);
    }


    @GetMapping("/test/anno-with-async")
    public Object annotationWithAsync() {
        demoService.doAsync("anno-with-async");
        return "success";
    }

    @GetMapping("/test/resttemplate")
    public Object resttemplate() {
        return restTemplate.getForObject("http://upms-service/api/upms/userinfo?uid=121", Map.class);
    }
}

