package com.hundsun.demo.spring;

import com.hundsun.demo.spring.listener.SimpleEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-29 14:23
 */

public class SpringDemoApplication {

    /**
     * spring容器
     */
    public static final ApplicationContext applicationContext;

    static {
        // 创建容器
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml", "simple.xml");
        // 这里通过 applicationContext 容器来发布一个简单的事件
        applicationContext.publishEvent(new SimpleEvent("Application Started"));
    }

    public static void main(String[] args) {
        // 销毁容器
        ((AbstractApplicationContext) applicationContext).close();
    }

}
