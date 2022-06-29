package com.hundsun.demo.dubbo.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: dubbo-demo
 * @Package: com.hundsun.amust.dubbodemo
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-21 15:10
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@MapperScan("com.hundsun.demo.dubbo.provider.mapper")
@SpringBootApplication
@Slf4j
public class ProviderApplication {

    public static void main(String[] args) {

        log.info("provider开始启动");
        ApplicationContext applicationContext = SpringApplication.run(ProviderApplication.class, args);
        log.info("provider启动完成");
    }

}
