package com.hundsun.demo.spring;

import com.hundsun.demo.spring.mvc.springdao.UserDAO;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class SpringMvcApplication {

    /*
    spring 读取配置文件的顺序如下:
    –file:./config/
    –file:./
    –classpath:/config/
    –classpath:/
     */

    /**
     * spring容器
     */
    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        // 创建容器,以读取配置文件的方式开启容器
        applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        // 销毁容器
        ((AbstractApplicationContext) applicationContext).close();
    }
}
