package com.hundsun.demo.spring.mvc.controller;

import com.hundsun.demo.spring.mvc.springdao.UserDAO;
import com.hundsun.demo.spring.mvc.springdao.UserDAOImpl;
import lombok.Getter;
import lombok.Setter;
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

@Setter
@Slf4j
public class SimpleController extends AbstractController {

    /**
     * viewName
     */
    @Getter
    private String viewName;

    UserDAO userDAO;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mav = new ModelAndView(getViewName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hello, spring controller\n");
        stringBuilder.append(String.format("%s\n", userDAO.findAll()));
        mav.addObject("data", stringBuilder);
        return mav;
    }

}
