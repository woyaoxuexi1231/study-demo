package com.hundsun.demo.java.pattern.structural.proxy.jdk;


import com.hundsun.demo.java.pattern.structural.proxy.MySQLService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ProductName: Java
 * @Package: com.hundsun.demo.java.jdk.proxy.jdk
 * @Description: 这是一个动态代理类, 这个类可以返回一个动态代理对象
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */

@Slf4j
public class MySQLServiceInvocation implements InvocationHandler {

    /*
    1. Proxy.newProxyInstance - getProxyClass0(ClassLoader loader, Class<?>... interfaces)
        这个方法会帮我们生成一个代理类(一个继承Proxy的类), 在 newProxyInstance 方法返回前会帮我们把我们自定义的InvocationHandler对象塞进去
        Proxy$ProxyClassFactory 是最终帮我们生成Proxy的工具类
        源码中如果我们对一个没有实现类的接口进行代理, 那么生成的代理类不会实现任何方法, 他只是一个 Object类的代理对象, 没有任何意义
        也就是说jdk动态代理无法对一个没有实现类的接口进行代理
    2. 在获取代理对象的前面加入 System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true"), 我们把生成的代理对象保存到本地
        代理对象会继承 Proxy类, 实现代理的接口
        在代理的接口中 super.h.invoke(this, m3, new Object[]{var1})), 这里会调用我们自定义的 InvocationHandler的 invoke方法
        this是就是生成的代理对象
        m3在代理对象的初始化static方法里可以看到 m3 = Class.forName("com.hundsun.demo.java.jdk.proxy.MySqlService").getMethod("update"), 就是我们代理的接口的对应的方法
        new Object[]{var1})就是这个对应的方法传递的参数
     */

    /**
     * 被代理的对象
     */
    private final MySQLService target;

    public MySQLServiceInvocation(MySQLService target) {
        this.target = target;
    }

    /**
     * 根据 target 获取一个代理对象
     *
     * @return 代理对象
     */
    public MySQLService getProxy() {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        return (MySQLService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new MySQLServiceInvocation(target)
        );
    }

    /**
     * 代理对象的invoke
     *
     * @param proxy  生成的代理对象本身
     * @param method 被代理的对象的对应的方法
     * @param args   被代理的方法的参数
     * @return 被代理的方法返回的结果
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        System.out.println("Mysql connected, starting the transaction...");
        Object obj;
        try {
            obj = method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Transaction has been commit.");
        return obj;
    }
}
