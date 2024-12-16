package com.hundsun.demo.dubbo.consumer;

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
 */

@SpringBootApplication
@Slf4j
@MapperScan(basePackages = {"com.hundsun.demo.dubbo.consumer.mapper"})
public class ConsumerApplication {

    public static void main(String[] args) {

        log.info("consumer开始启动");
        ApplicationContext applicationContext = SpringApplication.run(ConsumerApplication.class, args);
        log.info("consumer启动完成");
    }

}
