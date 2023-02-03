package com.hundsun.demo.java.jdk.proxy.service.serviceimpl;

import com.hundsun.demo.java.jdk.proxy.service.AdminService;
import lombok.extern.slf4j.Slf4j;

/**
 * target对象实例
 *
 * @ProductName: Java
 * @Package:
 * @Description:
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */

@Slf4j
public class AdminServiceImpl implements AdminService {

    public void update() {
        log.info("修改管理系统数据");
    }

    public Object find() {
        log.info("查看管理系统数据");
        return new Object();
    }
}
