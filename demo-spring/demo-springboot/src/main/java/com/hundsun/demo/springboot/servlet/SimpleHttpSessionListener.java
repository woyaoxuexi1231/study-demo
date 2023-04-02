package com.hundsun.demo.springboot.servlet;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.servlet
 * @className: SimpleHttpSessionListener
 * @description:
 * @author: hulei42031
 * @createDate: 2023-04-02 16:47
 */

@Component
public class SimpleHttpSessionListener implements HttpSessionListener {


    public static int online=0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("创建session");
        online++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("销毁session");
    }
}
