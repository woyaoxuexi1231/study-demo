package com.hundsun.demo.spring.controller;

import com.hundsun.demo.spring.service.HiService;
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
 * @updateUser: h1123
 * @updateDate: 2022/11/16 23:00
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class TestController extends AbstractController {


    private HiService hiService;

    private String viewName;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        getHiService().sayHi();

        ModelAndView mav = new ModelAndView(getViewName());
        mav.addObject("hello","hello");

        return mav;
    }

    public HiService getHiService() {
        return hiService;
    }

    public void setHiService(HiService hiService) {
        this.hiService = hiService;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
