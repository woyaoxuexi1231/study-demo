package com.hundsun.demo.springboot.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.util
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-11-30 11:26
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class StringUtilTest {

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
