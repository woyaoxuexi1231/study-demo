package com.hundsun.demo.springcloud.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebController {

    /**
     * 声明一个简单的根路径，在一个非常纯净的security中，默认登录后会访问这个地址。
     * 如果没有，会报错。
     *
     * @return 简单的一串字符串
     */
    @GetMapping("/")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, null, auth);
        }
        return "redirect:/login?logout";
    }

    // @GetMapping({"/", "/index"})
    // public String redirectBasedOnAuthentication() {
    //     // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //     // if (auth != null && !auth.getName().equals("anonymousUser")) {
    //     //     // 该用户已登录，可以添加更多的条件检查比如基于角色的重定向
    //     //     return "redirect:/user/index";
    //     // }
    //     return "myLogin"; // 如果用户未登录，显示首页
    // }

    @GetMapping("/login-form")
    public String showLoginForm() {
        return "login-form";
    }

}
