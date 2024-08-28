package org.hulei.demo.spring.circle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author hulei
 * @since 2024/8/28 10:37
 */

@SpringBootApplication
public class SpringCircleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringCircleApplication.class, args);

        applicationContext.getBean(ClassA.class).test();
        System.out.println(1111);
    }

}
