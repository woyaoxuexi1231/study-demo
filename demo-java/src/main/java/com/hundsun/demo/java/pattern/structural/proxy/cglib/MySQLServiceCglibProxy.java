package com.hundsun.demo.java.pattern.structural.proxy.cglib;

import com.hundsun.demo.java.pattern.structural.proxy.MySQLService;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * cglib-代理对象类
 * cglib在Spring AOC中被广泛使用(Enhancer这个增强类来生成代理)
 *
 * @ProductName: Java
 * @Package:
 * @Description:
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */

public class MySQLServiceCglibProxy implements MethodInterceptor {

    /*
    Cglib代理方式相比JDK动态代理更加强大
    1. Cglib不仅仅可以代理接口, 它也可以直接代理对象, 这个对象可以没有实现任何接口, 更神奇的是cglib可以直接代理一个没有实现类的接口
    2. 我们通过 System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, path), 可以把生成的代理对象保存到本地
        生成的代理对象将继承这个接口的实现类(如果这个接口没有实现类, 那么代理对象会实现这个接口), 实现 Factory接口
        调用的方式和jdk动态代理类似
     */

    /**
     * 目标对象
     */
    private final MySQLService target;

    public MySQLServiceCglibProxy(MySQLService target) {
        this.target = target;
    }

    public MySQLService getProxyInstance() {

        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, Objects.requireNonNull(this.getClass().getResource("")).getPath());
        // 工具类 - 字节码增强器, 可以用来为无接口的类创建代理, 功能与 Proxy 类似
        Enhancer en = new Enhancer();
        // 设置目标类的 class
        en.setSuperclass(target.getClass());
        // 设置回调函数 - 参数是一个实现 MethodInterceptor接口的实现类
        en.setCallback(this);

        // cglib还提供 callback过滤器, 可以根据不同的过滤规则, 来选择代理的方法具体被哪一个 callback代理
        CallbackFilter callbackFilter = new MySQLServiceCallbackFilter();
        en.setCallbackFilter(callbackFilter);

        // 什么也不做的 Callback
        Callback noopCb = NoOp.INSTANCE;
        // 这里把自定义的MySQLServiceCglibProxy和NoOp.INSTANCE两个callback放入Enhancer, 以便根据 CallbackFilter来过滤
        Callback[] callbacks = new Callback[]{new MySQLServiceCglibProxy(this.target), noopCb};
        en.setCallbacks(callbacks);

        // 创建 Cglib代理对象
        return (MySQLService) en.create();

    }


    public Object intercept(Object object, Method method, Object[] arg, MethodProxy proxy) {

        System.out.println("Mysql connected, starting the transaction...");
        Object obj;
        try {
            obj = method.invoke(target, arg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Transaction has been commit.");
        return obj;
    }


}
