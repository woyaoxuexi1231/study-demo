package com.hundsun.demo.java.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.servlet
 * @className: ServletTest
 * @description:
 * @author: h1123
 * @createDate: 2022/11/13 16:26
 * @updateUser: h1123
 * @updateDate: 2022/11/13 16:26
 * @updateRemark:
 * @version: v1.0
 * @see :
 */
@WebServlet(name = "demo")
public class ServletTest extends HttpServlet implements Servlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("1111");
        PrintWriter writer = resp.getWriter();
        writer.println("1111");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("1111");
    }
}
