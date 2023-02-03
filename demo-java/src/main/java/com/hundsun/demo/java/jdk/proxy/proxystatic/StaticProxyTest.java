package com.hundsun.demo.java.jdk.proxy.proxystatic;

import com.hundsun.demo.java.jdk.proxy.service.serviceimpl.AdminServiceImpl;
import com.hundsun.demo.java.jdk.proxy.service.AdminService;
import lombok.extern.slf4j.Slf4j;

/**
 * 静态代理测试类
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
public class StaticProxyTest {

    public static void main(String[] args) {
        // 一个拥有更新和查找两个功能的服务(这是一个需要被代理的对象)
        AdminService adminService = new AdminServiceImpl();
        // 这是一个静态代理对象（最原始，最简单的代理对象）
        AdminServiceProxyImpl proxy = new AdminServiceProxyImpl(adminService);
        // 通过代理对象来实现对被代理的对象的方法的访问
        proxy.update();
        proxy.find();
    }
}
