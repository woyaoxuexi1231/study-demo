package com.hundsun.demo.java.jdk.proxy.proxystatic;


import lombok.extern.slf4j.Slf4j;
import com.hundsun.demo.java.jdk.proxy.service.AdminService;

/**
 * 这是一个静态代理对象
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
public class AdminServiceProxyImpl implements AdminService {


    /**
     * 被代理的对象
     */
    private AdminService adminService;

    public AdminServiceProxyImpl(AdminService adminService) {
        this.adminService = adminService;
    }

    // 通过静态代理对象来调用被代理的对象的方法

    public void update() {
        log.info("判断用户是否有权限进行update操作");
        adminService.update();
        log.info("记录用户执行update操作的用户信息、更改内容和时间等");
    }

    public Object find() {
        log.info("判断用户是否有权限进行find操作");
        log.info("记录用户执行find操作的用户信息、查看内容和时间等");
        return adminService.find();
    }
}
