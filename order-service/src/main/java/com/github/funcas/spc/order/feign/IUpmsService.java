package com.github.funcas.spc.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * TODO
 *
 * @author shane
 * @since 1.0
 */
@FeignClient(value = "upms-service")
public interface IUpmsService {

    @GetMapping("/api/upms/userinfo")
    public Map<String, String> getUserInfo(@RequestParam("uid") String uid);
}
