package com.github.funcas.spc.upms.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author shane
 * @since 1.0
 */
@RequestMapping("/api/upms")
@RestController
public class UpmsController {

    @GetMapping("/userinfo")
    public Map<String, String> getUserInfo(String uid) {
        ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        System.out.println(attrs.getRequest().getHeader("x-version"));
        System.out.println("invoke");
        Map<String, String> uInfo = new HashMap<>();
        uInfo.put("uid", uid);
        uInfo.put("name", "funcas");
        return uInfo;
    }

}
