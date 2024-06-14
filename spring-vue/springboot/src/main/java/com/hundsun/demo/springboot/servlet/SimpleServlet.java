// package com.hundsun.demo.springboot.servlet;
//
// import javax.servlet.ServletException;
// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;
// import java.io.IOException;
//
// /**
//  * @projectName: study-demo
//  * @package: com.hundsun.demo.springboot.servlet
//  * @className: SimpleServlet
//  * @description:
//  * @author: hulei42031
//  * @createDate: 2023-04-02 16:30
//  */
//
// // @Component
// @WebServlet(name = "simpleServlet", urlPatterns = "/srv")
// public class SimpleServlet extends HttpServlet {
//
//
//     @Override
//     protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//         HttpSession session = req.getSession(true);
//         System.out.println(111);
//         // super.doGet(req, resp);
//         System.out.println(SimpleHttpSessionListener.online);
//
//     }
// }
