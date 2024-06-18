package com.hundsun.demo.spring;

import com.hundsun.demo.spring.aop.annotation.MyService;
import com.hundsun.demo.spring.aop.xml.PrintService;
import com.hundsun.demo.spring.db.dynamicdb.DynamicDataSourceType;
import com.hundsun.demo.spring.db.dynamicdb.MultipleDataSourceTestEvent;
import com.hundsun.demo.spring.db.transaction.AnnotationTransaction;
import com.hundsun.demo.spring.init.PrototypeBean;
import com.hundsun.demo.spring.mvc.springdao.UserDAOImpl;
import com.hundsun.demo.spring.mvc.springdao.UserDAOJdbcTemplate;
import com.hundsun.demo.spring.init.listener.SimpleEvent;
import com.hundsun.demo.spring.mybatis.MyBatisOperationType;
import com.hundsun.demo.spring.mybatis.MybatisEvent;
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
        applicationContext = new ClassPathXmlApplicationContext("springmvc/applicationContext.xml", "springmvc/simple.xml");

        prototypeBean();
        // springListener();
        // dynamicDB();
        // mybatisDynamicDB();
        // springDao();
        // hibernate();
        // transaction();
        // springaop();



        // 销毁容器
        ((AbstractApplicationContext) applicationContext).close();
    }

    private static void springListener() {
        // spring自带的监听机制测试.
        applicationContext.publishEvent(new SimpleEvent("Application Started"));
    }

    private static void springaop() {
        PrintService bean = applicationContext.getBean(PrintService.class);
        bean.print();
        MyService myService = applicationContext.getBean(MyService.class);
        myService.doSomething();
    }

    private static void transaction() {
        // SpringTransactionService bean = applicationContext.getBean(SpringTransactionService.class);
        // bean.handleTransaction();

        // ProxyTransaction bean = applicationContext.getBean(ProxyTransaction.class);
        // bean.handleTransaction();

        // AopTransaction bean = applicationContext.getBean(AopTransaction.class);
        // bean.handleTransaction();

        AnnotationTransaction bean = applicationContext.getBean(AnnotationTransaction.class);
        bean.handleTransaction();
    }

    private static void dynamicDB() {
        // 测试多数据源切换
        try {
            applicationContext.publishEvent(new MultipleDataSourceTestEvent(DynamicDataSourceType.MASTER));
            applicationContext.publishEvent(new MultipleDataSourceTestEvent(DynamicDataSourceType.SECOND));
        } catch (Exception e) {
            log.error("多数据源更新异常, 执行回滚操作. ", e);
        }
    }

    private static void mybatisDynamicDB() {
        // mybatis 事务小测试
        try {
            applicationContext.publishEvent(new MybatisEvent(MyBatisOperationType.UPDATE, DynamicDataSourceType.SECOND));
            // applicationContext.publishEvent(new MybatisEvent(MyBatisOperationType.SELECT, DynamicDataSourceType.SECOND));
        } catch (Exception e) {
            log.error("Mybatis更新数据异常, 已执行回滚. ", e);
        }
    }


    private static void springDao() {
        UserDAOImpl bean = applicationContext.getBean(UserDAOImpl.class);
        UserDAOJdbcTemplate template = applicationContext.getBean(UserDAOJdbcTemplate.class);
        log.info("{}", bean.findAll());
        log.info("{}", template.findAll());
    }

    private static void hibernate() {
        // UserDAO userDAO = applicationContext.getBean(com.hundsun.demo.spring.hibernate.UserDAOImpl.class);
        //
        // // 创建用户
        // User user = new User();
        // user.setName("test");
        // userDAO.save(user);
        //
        // // 查询所有用户
        // List<User> users = userDAO.findAll();
        // for (User u : users) {
        //     System.out.println(u.getName());
        // }
    }

    private static void prototypeBean(){
        System.out.println(applicationContext.getBean(PrototypeBean.class));
        System.out.println(applicationContext.getBean(PrototypeBean.class));
    }
}
