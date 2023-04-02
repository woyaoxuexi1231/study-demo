package com.hundsun.demo.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-25 20:20
 */

@SpringBootApplication
@MapperScan(basePackages = {"com.hundsun.demo.springboot.mapper"})
@Slf4j
@ServletComponentScan
public class SpringbootDemoApplication {

    public static void main(String[] args) {
        // System.setProperty("cglib.debugLocation","C:\\Project\\study-demo\\demo-spring\\demo-springboot\\target\\classes");
        ApplicationContext applicationContext = SpringApplication.run(SpringbootDemoApplication.class);
        log.info("启动完成");
    }
}
