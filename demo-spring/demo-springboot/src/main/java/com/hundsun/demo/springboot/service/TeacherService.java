package com.hundsun.demo.springboot.service;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.service
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-09-23 15:47
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public interface TeacherService {

    /**
     * NEVER
     * 必须在一个没有的事务中执行,否则抛出异常(与Propagation.MANDATORY相反)
     */
    void insertNever();

    /**
     * MANDATORY
     * 必须在一个已有的事务中执行,否则抛出异常
     */
    void insertMandatory();

    /**
     * SUPPORTS
     * 如果其他bean调用这个方法,在其他bean中声明事务,那就用事务.如果其他bean没有声明事务,那就不用事务.
     */
    void insertSupports();

    /**
     * NOT_SUPPORTED
     * 容器不为这个方法开启事务
     */
    void insertNotSupports();

    /**
     * REQUIRES_NEW
     * 不管是否存在事务,都创建一个新的事务,原来的挂起,新的执行完毕,继续执行老的事务
     */
    void insertRequiresNew();


}
