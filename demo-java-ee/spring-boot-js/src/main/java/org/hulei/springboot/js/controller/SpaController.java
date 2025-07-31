package org.hulei.springboot.js.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hulei
 * @since 2024/3/10 13:45
 */

@Controller
public class SpaController {

    // 将所有非静态和非API请求重定向到 index.html
    // @RequestMapping(value = "/**/{[path:[^\\.]*}")
    public String redirect() {
        // 转发到 index.html 而无需更改 URL
        return "forward:/index.html";
    }
}