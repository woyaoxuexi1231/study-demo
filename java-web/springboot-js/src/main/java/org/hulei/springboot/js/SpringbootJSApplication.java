package org.hulei.springboot.js;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.js.controller
 * @className: SpringbootJSApplication
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 13:38
 */

@RestController
@MapperScan(basePackages = {
        "org.hulei.common.mapper.mapper",
})
@Slf4j
@SpringBootApplication
public class SpringbootJSApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SpringbootJSApplication.class);
        log.info("启动完成");
    }

    @GetMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response) {
        log.info("远程地址: {}", request.getRemoteHost());
    }
}
