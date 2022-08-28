package com.hundsun.demo.java.proxy.proxydynamic;


import com.hundsun.demo.java.proxy.service.AdminService;
import com.hundsun.demo.java.proxy.service.serviceimpl.AdminServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;


/**
 * 动态代理测试类
 *
 * @ProductName: Java
 * @Package:
 * @Description:
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */

@Slf4j
public class DynamicProxyTest {

    public static void main(String[] args) {

        // 一个拥有更新和查找两个功能的服务(这是一个需要被代理的对象)
        AdminService adminService = new AdminServiceImpl();
        // 这是一个包含被代理的对象的调用处理类
        AdminServiceInvocation adminServiceInvocation = new AdminServiceInvocation(adminService);
        // 这个类封装了一个需要被代理的对象，和一个调用处理类的类
        AdminServiceDynamicProxy adminServiceDynamicProxy = new AdminServiceDynamicProxy(adminService, adminServiceInvocation);
        // **获取代理对象的三种方法：方法1.通过这个封装类的这个方法获得一个代理对象
        AdminService proxy = (AdminService) adminServiceDynamicProxy.getPersonProxy();
        // ***方法2.直接使用newProxyInstance方法来获取一个代理对象。三个参数的意义是：1.被代理的对象的类加载器、2.被代理的对象实现的接口、3.一个调用处理类
        AdminService proxy2 = (AdminService) Proxy.newProxyInstance(adminService.getClass().getClassLoader(),
                adminService.getClass().getInterfaces(), adminServiceInvocation);
        // *方法3.直接原地写一个调用处理类
        AdminService proxy3 = (AdminService) Proxy.newProxyInstance(adminService.getClass().getClassLoader(),
                adminService.getClass().getInterfaces(), (proxy1, method, args1) -> {
            log.info("判断用户是否有权限进行操作");
            Object obj = method.invoke(adminService, args1);
            log.info("记录用户执行操作的用户信息、更改内容和时间等");
            return obj;
        });

        // 通过代理对象来调用方法
        proxy.find();
        proxy.update();

        // proxy2.find();
        // proxy3.find();

    }
}

