package com.hundsun.demo.java.proxy.proxycglib;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;

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

@Slf4j
public class AdminServiceCglibProxy implements MethodInterceptor {

    /**
     * 目标对象
     */
    private Object target;

    public AdminServiceCglibProxy(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {

        // System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\java\\java_workapace");

        // 工具类(字节码增强器，可以用来为无接口的类创建代理，功能与proxy类似)
        Enhancer en = new Enhancer();

        // 设置父类(设置增强类的类型)
        en.setSuperclass(target.getClass());

        // CallbackFilter callbackFilter = new TargetMethodCallbackFilter();
        //
        // Callback noopCb= NoOp.INSTANCE;
        // Callback callback1=new AdminServiceCglibProxy(this.target);
        // Callback fixedValue=new TargetResultFixed();
        // Callback[] cbarray=new Callback[]{callback1,noopCb,fixedValue};

        // 设置回调函数(设置代理方法--参数是一个实现MethodInterceptor接口的实现类)
        en.setCallback(this);
        //
        // en.setCallbacks(cbarray);
        // en.setCallbackFilter(callbackFilter);

        // 创建子类代理对象(创建cglib代理对象)
        return en.create();
    }


    public Object intercept(Object object, Method method, Object[] arg2, MethodProxy proxy) throws Throwable {

        log.info("判断用户是否有权限进行操作");
        Object obj = method.invoke(target);
        log.info("记录用户执行操作的用户信息、更改内容和时间等");
        return obj;
    }


}
