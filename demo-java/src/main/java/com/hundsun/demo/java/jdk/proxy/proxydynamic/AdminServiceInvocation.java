package com.hundsun.demo.java.jdk.proxy.proxydynamic;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 调用处理类
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
public class AdminServiceInvocation implements InvocationHandler {

    /**
     * 被代理的对象
     */
    private Object target;

    public AdminServiceInvocation(Object target) {
        this.target = target;
    }

    /**
     * 代理对象的invoke
     *
     * @param proxy  被代理的对象
     * @param method 使用的被代理的对象的方法
     * @param args   代理对象方法传递的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("判断用户是否有权限进行操作");
        Object obj = method.invoke(target);
        log.info("记录用户执行操作的用户信息、更改内容和时间等");
        return obj;
    }
}
