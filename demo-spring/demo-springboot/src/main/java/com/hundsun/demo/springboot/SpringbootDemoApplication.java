package com.hundsun.demo.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ApplicationContext;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-25 20:20
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

@SpringBootApplication
@Slf4j
public class SpringbootDemoApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SpringbootDemoApplication.class);
        log.info("启动完成");
    }
}
