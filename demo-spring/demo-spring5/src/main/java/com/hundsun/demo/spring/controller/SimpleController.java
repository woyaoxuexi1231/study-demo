package com.hundsun.demo.spring.controller;

import com.hundsun.demo.commom.core.model.CustomerDO;
import com.hundsun.demo.spring.service.YiibaidbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
     * yiibaidbService
     */
    private YiibaidbService yiibaidbService;

    /**
     * viewName
     */
    private String viewName;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        ModelAndView mav = new ModelAndView(getViewName());

        List<CustomerDO> students = yiibaidbService.jdbcTemplateQuery();

        mav.addObject("data", students);

        try {
            yiibaidbService.jdbcTemplateUpdate();
        } catch (Exception e) {
            log.error("更新出现异常! ", e);
        }

        yiibaidbService.handleTransaction();

        return mav;
    }

    public YiibaidbService getYiibaidbService() {
        return yiibaidbService;
    }

    public void setYiibaidbService(YiibaidbService yiibaidbService) {
        this.yiibaidbService = yiibaidbService;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
