package com.hundsun.demo.java.jdk.proxy.proxydynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;


/**
 * 动态代理
 *
 * @ProductName: Java
 * @Package:
 * @Description:
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */
public class AdminServiceDynamicProxy {

    /**
     * 目标对象
     */
    private Object target;

    /**
     * 代理对象调用处理
     */
    private InvocationHandler invocationHandler;

    public AdminServiceDynamicProxy(Object target, InvocationHandler invocationHandler) {
        this.target = target;
        this.invocationHandler = invocationHandler;
    }

    public Object getPersonProxy() {

        // 通过Proxy.newProxyInstance得到一个代理对象
        Object obj = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), invocationHandler);
        return obj;
    }
}
