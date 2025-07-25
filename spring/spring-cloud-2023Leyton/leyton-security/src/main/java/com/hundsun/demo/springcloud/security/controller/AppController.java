package com.hundsun.demo.springcloud.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/7/24 20:38
 */

@RestController
@RequestMapping("/app")
public class AppController {

    @GetMapping("/hello")
    public String hello(){
        return "hello, app";
    }
}
