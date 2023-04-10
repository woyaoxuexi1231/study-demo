package com.hundsun.demo.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.controller
 * @className: ComponentController
 * @description:
 * @author: h1123
 * @createDate: 2023/4/10 22:35
 */

@Controller
public class ComponentController {

    @RequestMapping("/mvc")
    public ModelAndView mvc(String name) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("data", name);
        mv.setViewName("index.jsp");
        return mv;
    }

    @RequestMapping("/simpleJS")
    public Map<String, String> simpleJS() {
        Map<String, String> map = new HashMap<>();
        map.put("hello", ", world");
        return map;
    }
}
