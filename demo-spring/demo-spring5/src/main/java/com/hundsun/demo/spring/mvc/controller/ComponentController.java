package com.hundsun.demo.spring.mvc.controller;

import com.alibaba.fastjson.JSONObject;
import com.hundsun.demo.spring.model.pojo.CustomerDO;
import com.hundsun.demo.spring.service.YiibaidbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private YiibaidbService yiibaidbService;

    @RequestMapping("/mvc")
    public ModelAndView mvc(String name) {
        ModelAndView mv = new ModelAndView();
        // mv.addObject("data", name);
        List<CustomerDO> students = yiibaidbService.jdbcTemplateQuery();
        mv.addObject("data", students);
        mv.setViewName("index.jsp");
        return mv;
    }

    @RequestMapping(value = "/simpleJS")
    @ResponseBody
    public String simpleJS() {
        Map<String, String> map = new HashMap<>();
        map.put("hello", ", world");
        return JSONObject.toJSONString(map);
    }
}
