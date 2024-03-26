package com.hundsun.demo.spring.mvc.controller;

import com.hundsun.demo.spring.mvc.springdao.UserDAOHibernate;
import com.hundsun.demo.spring.mvc.springdao.UserDAOImpl;
import com.hundsun.demo.spring.mvc.springdao.UserDAOJdbcTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.controller
 * @className: TestController
 * @description:
 * @author: h1123
 * @createDate: 2022/11/16 23:00
 */

@Slf4j
public class SimpleController extends AbstractController {

    /**
     * viewName
     */
    private String viewName;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mav = new ModelAndView(getViewName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hello, spring controller\n");
        mav.addObject("data", stringBuilder);
        return mav;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
