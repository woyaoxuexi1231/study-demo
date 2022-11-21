package com.hundsun.demo.spring.model;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.model
 * @className: IgnoreBean
 * @description:
 * @author: h1123
 * @createDate: 2022/11/17 20:57
 * @updateUser: h1123
 * @updateDate: 2022/11/17 20:57
 * @updateRemark:
 * @version: v1.0
 * @see :
 */
public class IgnoreBean implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

    }
}
