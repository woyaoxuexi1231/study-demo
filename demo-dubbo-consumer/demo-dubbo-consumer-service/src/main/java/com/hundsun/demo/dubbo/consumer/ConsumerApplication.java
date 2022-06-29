package com.hundsun.demo.dubbo.consumer;

import com.hundsun.demo.dubbo.common.api.config.ResultAspectConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

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

@SpringBootApplication
@Slf4j
public class ConsumerApplication {

    public static void main(String[] args) {

        log.info("consumer开始启动");
        ApplicationContext applicationContext = SpringApplication.run(ConsumerApplication.class, args);
        log.info("consumer启动完成");
    }

}
