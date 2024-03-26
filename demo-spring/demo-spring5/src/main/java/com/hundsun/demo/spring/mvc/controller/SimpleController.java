package com.hundsun.demo.spring.mvc.controller;

import com.hundsun.demo.spring.mvc.springdao.UserDAOImpl;
import lombok.extern.slf4j.Slf4j;
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

    UserDAOImpl userDAO;

    public void setUserDAO(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mav = new ModelAndView(getViewName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hello, spring controller\n");
        stringBuilder.append(String.format("%s\n", userDAO.findAll()));
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
