package com.hundsun.demo.spring.mvc.controller;

import com.alibaba.fastjson.JSONObject;
import com.hundsun.demo.spring.mvc.springdao.UserDAO;
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

    // @Autowired
    // UserDAOImpl userDAO;

    @Autowired
    UserDAO userDAO;

    // @Autowired
    // UserDAOHibernate userDAOHibernate;

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
        // map.put("userdao", userDAO.findAll().toString());
        map.put("userDAOJdbcTemplate", userDAO.findAll().toString());
        // 集成hibernate遇到一些bug,花费两天时间依旧没解决,暂时不搞这个了 2024年3月27日
        // map.put("userDAOHibernate", userDAOHibernate.findAll().toString());

        return JSONObject.toJSONString(map);
    }
}
