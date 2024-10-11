package org.hulei.springboot.thymeleaf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.com.hundsun.demo.springboot.thymeleaf.controller
 * @className: SpringbootJSApplication
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 13:38
 */

@MapperScan(basePackages = {
        "org.hulei.springboot.thymeleaf.mapper",
})
@Slf4j
@SpringBootApplication
public class SpringbootThymeleafApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SpringbootThymeleafApplication.class);
        log.info("启动完成");
    }
}
