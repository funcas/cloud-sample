package com.github.funcas.spc.order.service;

import com.github.funcas.spc.order.feign.IUpmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * TODO
 *
 * @author Shane Fang
 * @since 1.0
 */
@Service
public class DemoService {

    public static final Logger logger = LoggerFactory.getLogger(DemoService.class);
    @Autowired
    private IUpmsService upmsService;

    ExecutorService executorService = Executors.newFixedThreadPool(3);
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;


    public Map<String,String> getRemoteUser(String uid) {
        return upmsService.getUserInfo(uid);
    }

    @Async
    public void doAsync(String uid) {
        Map ret = upmsService.getUserInfo(uid);
        logger.info(ret.toString());
    }

    public void doInSpringTaskPool() {
        threadPoolTaskExecutor.execute(() -> {
            upmsService.getUserInfo("spring-task-pool");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            upmsService.getUserInfo("spring-task-pool-again");
        });
    }

    public void doInParallelStream() {
        IntStream.range(0, 15).parallel().forEach(i -> {
            Map ret = upmsService.getUserInfo(i + "");
            logger.info(ret.toString());
        });
    }

    public void doInExecutorService(String uid) {
        executorService.submit(() -> {
            upmsService.getUserInfo(uid);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            upmsService.getUserInfo(uid);
        });

    }

}
