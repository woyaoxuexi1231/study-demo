package com.hundsun.demo.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: SimpleController
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:30
 */
@RestController
@Slf4j
public class SimpleController implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public static void main(String[] args) {
        String blank = "";
        String space = "                ";
        String nullStr = null;
        String marble = " marble";
        System.out.println("-------isNotBlank--------");
        // StringUtils.isNotBlank 1. null 2.空字符串, 或字符串全部由空格组成 这两种情况军返回 false
        System.out.println(StringUtils.isNotBlank(blank));
        System.out.println(StringUtils.isNotBlank(space));
        System.out.println(StringUtils.isNotBlank(nullStr));
        System.out.println(StringUtils.isNotBlank(marble));
        System.out.println("-------isNotBlank--------");
        System.out.println("-------------------------");


        System.out.println("-------isNoneBlank--------");
        // StringUtils.isNoneBlank 此方法可以同时判断多个字符串, 判断的逻辑是 isNotBlank, 只要有一个不符合逻辑就 false
        System.out.println(StringUtils.isNoneBlank(blank));
        System.out.println(StringUtils.isNoneBlank(space));
        System.out.println(StringUtils.isNoneBlank(nullStr));
        System.out.println(StringUtils.isNoneBlank(marble));
        System.out.println("-------isNoneBlank--------");
        System.out.println("-------------------------");

        System.out.println("-------isNotEmpty--------");
        // StringUtils.isNotEmpty 1. 字符串为 null 2. 字符串的长度为 0 那么返回 true
        System.out.println(StringUtils.isNotEmpty(blank));
        System.out.println(StringUtils.isNotEmpty(space));
        System.out.println(StringUtils.isNotEmpty(nullStr));
        System.out.println(StringUtils.isNotEmpty(marble));
        System.out.println("-------isNotEmpty--------");
        System.out.println("-------------------------");
    }
}
