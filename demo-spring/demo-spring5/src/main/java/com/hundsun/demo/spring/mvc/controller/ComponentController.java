package com.hundsun.demo.spring.mvc.controller;

import com.alibaba.fastjson.JSONObject;
import com.hundsun.demo.spring.mvc.springdao.UserDAOHibernate;
import com.hundsun.demo.spring.mvc.springdao.UserDAOImpl;
import com.hundsun.demo.spring.mvc.springdao.UserDAOJdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Autowired
    UserDAOImpl userDAO;

    @Autowired
    UserDAOJdbcTemplate userDAOJdbcTemplate;

    @Autowired
    UserDAOHibernate userDAOHibernate;

    @RequestMapping("/mvc")
    public ModelAndView mvc(String name) {
        ModelAndView mv = new ModelAndView();
        // mv.addObject("data", name);
        mv.addObject("data", "students");
        mv.setViewName("SimpleMVC");
        return mv;
    }

    @RequestMapping(value = "/simpleJS")
    @ResponseBody
    public String simpleJS() {

        Map<String, String> map = new HashMap<>();
        map.put("hello", ", world");
        map.put("userdao", userDAO.findAll().toString());
        map.put("userDAOJdbcTemplate", userDAOJdbcTemplate.findAll().toString());
        map.put("userDAOHibernate", userDAOJdbcTemplate.findAll().toString());

        return JSONObject.toJSONString(map);
    }
}
