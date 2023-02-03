package com.hundsun.demo.java.jdk.proxy.service;

/**
 * target对象接口
 *
 * @ProductName: Java
 * @Package:
 * @Description:
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */

public interface AdminService {

    /**
     * 更新
     */
    void update();

    /**
     * 查询
     *
     * @return
     */
    Object find();
}
