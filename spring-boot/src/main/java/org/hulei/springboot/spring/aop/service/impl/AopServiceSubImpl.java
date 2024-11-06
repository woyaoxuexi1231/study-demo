package org.hulei.springboot.spring.aop.service.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.aop.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-05-16 19:42
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2024 Hundsun Technologies Inc. All Rights Reserved
 */

@Component
public class AopServiceSubImpl extends AopServiceImpl {

    /**
     * 这个方法虽然没有显示的被指定任何 advice,但是他的父类被明确的 advice 包裹
     * spring aop对于这种行为是直接继承
     */
    @Override
    public void print() {
        // super.print();
        System.out.println(111);
        throw new DataAccessException("error") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }
}
