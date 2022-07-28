package com.hundsun.demo.spring.controller;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-22 16:21
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class HiController {

    public static void main(String[] args) {
        String s = "123";
        change(s);
        System.out.println(s);
    }

    public static String change(String s){
        s = "sss";
        return s;
    }
}
