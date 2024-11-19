package org.hulei.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/11/19 19:31
 */


@ServletComponentScan(basePackages = "org.hulei.test.filter")
@RestController
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/get-ip")
    @ResponseBody
    public void getIp() {
    }
}
