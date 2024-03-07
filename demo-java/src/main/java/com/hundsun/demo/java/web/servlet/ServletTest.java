package com.hundsun.demo.java.web.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.servlet
 * @className: ServletTest
 * @description:
 * @author: h1123
 * @createDate: 2022/11/13 16:26
 * @see :
 */

// @WebServlet(urlPatterns = "/metadataSimple")
public class ServletTest extends HttpServlet implements Servlet {

    /*
    Java Web
    以 tomcat 为例, 在 tomcat的目录结构下, app为主目录, app下存在 src, WEB-INF(存在一个class目录, 存放 java的 class文件)

    这是一个简单的 java web应用
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        rsp.getWriter().println("A simple doGet method.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse rsp) throws IOException, ServletException {

        // 在 request对象中添加 name属性, 这里直接使用 req.getParameter("name")获取值, 那么前端传数据的时候就只能使用 Query Params方式传参
        req.setAttribute("name", req.getParameter("name"));

        // 把请求转发到 index.jsp
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/index.jsp");
        dispatcher.forward(req, rsp);
    }
}
