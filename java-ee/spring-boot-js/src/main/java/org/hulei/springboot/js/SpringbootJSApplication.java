package org.hulei.springboot.js;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/3/10 13:38
 */

@ServletComponentScan(basePackages = {"org.hulei.springboot.js.filter"})
@RestController
@Slf4j
@SpringBootApplication
public class SpringbootJSApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SpringbootJSApplication.class);
        log.info("启动完成");
    }

    @GetMapping("/getRemoteAddress")
    public void getRemoteAddress(HttpServletRequest request, HttpServletResponse response) {
        log.info("远程地址: {}", request.getRemoteHost());
    }
}
