package com.hundsun.demo.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-29 14:23
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class SpringDemoApplication {


    public static final ApplicationContext applicationContext;

    public static void main(String[] args) {

        String[] beans = applicationContext.getBeanDefinitionNames();

        for (String bean : beans) {
            System.out.println("bean : " + applicationContext.getBean(bean));
        }

    }

    static {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml", "demo-1.xml");
    }
}
