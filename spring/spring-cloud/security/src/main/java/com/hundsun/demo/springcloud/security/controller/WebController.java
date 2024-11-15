package com.hundsun.demo.springcloud.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class WebController {

    // @GetMapping("/")
    // public String showHome() {
    //     return "index";
    // }
    //
    // @GetMapping("/index")
    // public String index() {
    //     return "index";
    // }

    @GetMapping({"/", "/index"})
    public String redirectBasedOnAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getName().equals("anonymousUser")) {
            // 该用户已登录，可以添加更多的条件检查比如基于角色的重定向
            return "redirect:/user/index";
        }
        return "index"; // 如果用户未登录，显示首页
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @RequestMapping("/user/index")
    public String showUserPage(Model model, Principal principal) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        model.addAttribute("username", principal.getName());
        return "user/index";
    }

    @RequestMapping("/401")
    public String accessDenied() {
        return "401";
    }
}
