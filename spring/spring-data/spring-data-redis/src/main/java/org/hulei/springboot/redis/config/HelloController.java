package org.hulei.springboot.redis.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping("/")
    public String hello(){
        return "forward:index-v2.html";
    }
}
