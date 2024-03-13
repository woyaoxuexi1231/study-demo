package com.hundsun.demo.springboot.service;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.springboot.common.model.EmployeeDO;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.service
 * @className: SimpleService
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:32
 */

public interface SimpleService {



    /**
     * mybatis 的两种填值方式
     */
    void mybatis();





    void pageHelper();
}
