package org.hulei.springboot.thymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * home目录
     * spring.thymeleaf.prefix=classpath:/templates/
     * 此配置指定 Thymeleaf 模板引擎在解析模板时应搜索模板文件的路径前缀。在这种情况下，模板引擎将在 classpath 下的 /templates/ 目录中搜索模板文件。
     * 如果不配置, spring在整合thymeleaf的情况下会自动在 classpath:/templates/ 下找模板
     * 在源码的ThymeleafPropertiesw
     * <p>
     * spring.thymeleaf.suffix=.html
     * 此配置项指定 Thymeleaf 模板引擎在解析模板时应使用的文件后缀。在这种情况下，模板引擎将模板文件的后缀定义为 .html。
     *
     * @param model 模型
     * @return 视图名
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to the Spring Boot Thymeleaf example!");
        return "home";
    }
}