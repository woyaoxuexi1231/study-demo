package com.hundsun.demo.java.pattern.proxy;

import com.hundsun.demo.java.pattern.proxy.cglib.MySQLServiceCglibProxy;
import com.hundsun.demo.java.pattern.proxy.jdk.MySQLServiceInvocation;
import com.hundsun.demo.java.pattern.proxy.normal.MySQLServiceProxy;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.proxy
 * @className: ProxyTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 14:43
 */

public class ProxyTest {

    /*
    代理模式 - 由于某些原因需要给某对象提供一个代理以控制对该对象的访问。这时，访问对象不适合或者不能直接引用目标对象，代理对象作为访问对象和目标对象之间的中介。

    Java中有三种方式来供我们创建代理对象, 自定义的静态代理, JDK提供的动态代理, CGLIB代理

    代理模式的思想在很多地方都有体现, 日志系统、SpringAop、事务
     */

    public static void main(String[] args) {

        // 这是一个需要被代理的对象
        MySQLService mySqlService = new MySQLServiceImpl();

        // 1. 使用静态代理的方式代理该对象
        MySQLService staticProxy = new MySQLServiceProxy(mySqlService);

        // 2.使用jdk动态代理的方式代理该对象
        MySQLService jdkProxy = new MySQLServiceInvocation(mySqlService).getProxy();

        // 3. 通过cglib的方式代理该对象
        MySQLService cglibProxy = new MySQLServiceCglibProxy(mySqlService).getProxyInstance();

        staticProxy.update("static");
        jdkProxy.update("jdk");
        cglibProxy.update("cglib");
    }
}
