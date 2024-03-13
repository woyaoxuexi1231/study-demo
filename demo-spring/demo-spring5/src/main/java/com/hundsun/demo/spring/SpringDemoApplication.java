package com.hundsun.demo.spring;

import com.hundsun.demo.spring.init.listener.MybatisEvent;
import com.hundsun.demo.spring.init.listener.SimpleEvent;
import com.hundsun.demo.spring.jdbc.DynamicDataSourceType;
import com.hundsun.demo.spring.mybatis.MyBatisOperationType;
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
public class SpringDemoApplication {

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
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml", "simple.xml");
        // 这里通过 applicationContext 容器来发布一个简单的事件
        applicationContext.publishEvent(new SimpleEvent("Application Started"));

        // 测试多数据源切换
        // try {
        //     applicationContext.publishEvent(new MultipleDataSourceTestEvent(DynamicDataSourceType.MASTER));
        //     applicationContext.publishEvent(new MultipleDataSourceTestEvent(DynamicDataSourceType.SECOND));
        // } catch (Exception e) {
        //     log.error("多数据源更新异常, 执行回滚操作. ", e);
        // }

        // mybatis 事务小测试
        try {
            // applicationContext.publishEvent(new MybatisEvent(MyBatisOperationType.UPDATE, DynamicDataSourceType.MASTER));
            applicationContext.publishEvent(new MybatisEvent(MyBatisOperationType.UPDATE, DynamicDataSourceType.SECOND));
        } catch (Exception e) {
            log.error("Mybatis更新数据异常, 已执行回滚. ", e);
        }
        // 销毁容器
        ((AbstractApplicationContext) applicationContext).close();
    }

}
