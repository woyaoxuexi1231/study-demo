package com.hundsun.demo.springcloud.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/7/24 20:37
 */

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/hello")
    public String hello(){
        return "hello, admin";
    }
}
