package com.hundsun.demo.java.proxy.proxycglib;


import lombok.extern.slf4j.Slf4j;

/**
 * cglib代理测试
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
public class CglibProxyTest {
    public static void main(String[] args) {

        // 这是一个需要被代理的对象
        AdminCglibService target = new AdminCglibService();

        // 创建一个cglib代理对象类
        AdminServiceCglibProxy proxyFactory = new AdminServiceCglibProxy(target);

        // 通过cglib代理对象类得到一个代理对象
        AdminCglibService proxy = (AdminCglibService) proxyFactory.getProxyInstance();

        log.info("代理对象：" + proxy.getClass());

        Object obj = proxy.find();
        log.info("find 返回对象：" + obj.getClass());
        log.info("----------------------------------");
        proxy.update();
    }
}

