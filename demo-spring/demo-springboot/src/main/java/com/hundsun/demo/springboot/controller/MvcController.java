package com.hundsun.demo.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: MvcController
 * @description:
 * @author: h1123
 * @createDate: 2023/4/3 22:06
 */

@Controller
@Slf4j
public class MvcController {

    @RequestMapping("/index")
    public String test(){
        return "index";
    }
}
