package com.hundsun.demo.spring;

import com.hundsun.demo.spring.aop.annotation.MyService;
import com.hundsun.demo.spring.aop.xml.PrintService;
import com.hundsun.demo.spring.db.transaction.SpringTransactionService;
import com.hundsun.demo.spring.init.PrototypeBean;
import com.hundsun.demo.spring.init.listener.SimpleEvent;
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
public class SpringApplication {

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
        applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml", "spring/simple.xml");

        prototypeBean();
        springListener();
        springAop();
        springDao();
        transaction();

        // 销毁容器
        ((AbstractApplicationContext) applicationContext).close();
    }

    /**
     * spring多例bean的使用
     */
    private static void prototypeBean(){
        System.out.println(applicationContext.getBean(PrototypeBean.class));
        System.out.println(applicationContext.getBean(PrototypeBean.class));
    }

    /**
     * spring 订阅发布功能
     */
    private static void springListener() {
        // spring自带的监听机制测试.
        applicationContext.publishEvent(new SimpleEvent("Application Started"));
    }

    /**
     * spring aop
     */
    private static void springAop() {
        PrintService bean = applicationContext.getBean(PrintService.class);
        bean.print();
        MyService myService = applicationContext.getBean(MyService.class);
        myService.doSomething();
    }

    private static void springDao() {
        // UserDAOImpl bean = applicationContext.getBean(UserDAOImpl.class);
        UserDAO template = applicationContext.getBean(UserDAO.class);
        // log.info("{}", bean.findAll());
        log.info("{}", template.findAll());
    }

    private static void transaction() {
        SpringTransactionService bean = applicationContext.getBean(SpringTransactionService.class);
        bean.handleTransaction();
    }
}
