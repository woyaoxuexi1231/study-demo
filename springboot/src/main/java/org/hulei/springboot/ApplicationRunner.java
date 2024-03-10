package org.hulei.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @projectName: study-demo
 * @package: org.hulei.springboot
 * @className: ApplicationRunner
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 19:05
 */

@MapperScan(basePackages = {
        "org.hulei.springboot.mapper",
})
@Slf4j
@SpringBootApplication
public class ApplicationRunner implements ApplicationContextAware {

    public static ApplicationContext CONTEXT;

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ApplicationRunner.class);
        log.info("启动完成");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }
}
