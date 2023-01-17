package com.hundsun.demo.spring.controller;

import com.hundsun.demo.spring.event.SimpleEvent;
import com.hundsun.demo.spring.model.pojo.Dept;
import com.hundsun.demo.spring.model.pojo.Student;
import com.hundsun.demo.spring.service.HiService;
import com.hundsun.demo.spring.service.StudentService;
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
 * @updateUser: h1123
 * @updateDate: 2022/11/16 23:00
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class TestController extends AbstractController {


    private HiService hiService;

    private String viewName;

    private StudentService studentService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        getHiService().sayHi();

        ModelAndView mav = new ModelAndView(getViewName());
        mav.addObject("hello", "hello");

        List<Student> students = studentService.getAllStudent();
        mav.addObject("student", students);

        List<Dept> depts = studentService.getAllDept();
        mav.addObject("depts", depts);

        this.getApplicationContext().publishEvent(new SimpleEvent(new Object()));
        // SimpleEvent simpleEvent = new SimpleEvent();

        return mav;
    }


    public StudentService getStudentService() {
        return studentService;
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
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
