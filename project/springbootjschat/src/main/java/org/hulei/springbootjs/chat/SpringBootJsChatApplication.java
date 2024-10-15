package org.hulei.springbootjs.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;

/**
 * @author hulei
 * @since 2024/10/14 14:33
 */

@RestController
@Slf4j
@SpringBootApplication
public class SpringBootJsChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJsChatApplication.class, args);
        log.info("项目启动完成!");
    }

    @GetMapping("/user/test")
    public void test() {
        log.info("test");
    }
}
