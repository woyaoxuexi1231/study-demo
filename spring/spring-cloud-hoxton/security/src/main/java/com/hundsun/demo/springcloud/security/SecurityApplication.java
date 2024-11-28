package com.hundsun.demo.springcloud.security;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

import java.awt.*;
import java.net.URI;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

@MapperScan("com.hundsun.demo.springcloud.security.mapper")
@SpringBootApplication
public class SecurityApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //

        // Open the homepage URL in default browser
        String homepageURL = "http://localhost:9170"; // Update with your homepage URL
        System.out.println("Unable to open browser automatically. Please navigate to: " + homepageURL);
        // if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        //     Desktop.getDesktop().browse(URI.create(homepageURL));
        // } else {
        //     System.out.println("Unable to open browser automatically. Please navigate to: " + homepageURL);
        // }
    }
}
