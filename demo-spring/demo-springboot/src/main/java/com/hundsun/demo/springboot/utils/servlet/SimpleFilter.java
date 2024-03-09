// package com.hundsun.demo.springboot.servlet;
//
// import javax.servlet.Filter;
// import javax.servlet.FilterChain;
// import javax.servlet.FilterConfig;
// import javax.servlet.ServletException;
// import javax.servlet.ServletRequest;
// import javax.servlet.ServletResponse;
// import javax.servlet.annotation.WebFilter;
// import java.io.IOException;
//
// /**
//  * @projectName: study-demo
//  * @package: com.hundsun.demo.springboot.servlet
//  * @className: SimpleFilter
//  * @description:
//  * @author: hulei42031
//  * @createDate: 2023-04-02 16:45
//  */
//
// @WebFilter(filterName = "MyFilter", urlPatterns = "/*")
// public class SimpleFilter implements Filter {
//
//     @Override
//     public void init(FilterConfig filterConfig) throws ServletException {
//         System.out.println("init");
//     }
//
//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//         System.out.println("filter");
//         chain.doFilter(request, response);
//     }
//
//     @Override
//     public void destroy() {
//         System.out.println("destory");
//     }
// }
