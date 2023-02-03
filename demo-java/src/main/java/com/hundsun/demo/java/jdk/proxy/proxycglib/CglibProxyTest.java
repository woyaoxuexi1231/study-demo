package com.hundsun.demo.java.jdk.proxy.proxycglib;


import com.hundsun.demo.java.jdk.proxy.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

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

        // log.info("代理对象：" + proxy.getClass());
        //
        // Object obj = proxy.find();
        // log.info("find 返回对象：" + obj.getClass());
        // log.info("----------------------------------");
        proxy.update();

        Enhancer en = new Enhancer();
        // jvm参数-设置此项可以在指定位置生成cglib生成的字节码文件 -Dcglib.debugLocation=C:\Project\study-demo\demo-java\target\classes
        en.setSuperclass(AdminService.class);
        en.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
            System.out.println("只代理了一个接口...");
            return null;
        });
        // return en.create();
        AdminService proxy2 = (AdminService)en.create();
        proxy2.update();
    }
}

