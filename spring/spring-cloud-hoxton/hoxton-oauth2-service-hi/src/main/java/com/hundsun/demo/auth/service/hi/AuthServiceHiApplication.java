package com.hundsun.demo.auth.service.hi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.auth.service.hi
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-04-25 15:52
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@MapperScan(basePackages = {"com.hundsun.demo.auth.service.hi.mapper"})
@SpringBootApplication
public class AuthServiceHiApplication {

    public static void main(String[] args) {
        /*
        获取 token
        curl -d "username=miya&password=123456" "localhost:8762/user/registry"
         */
        SpringApplication.run(AuthServiceHiApplication.class);
    }
}
