package org.hulei.demo.cj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hulei
 * @since 2025/8/8 17:49
 */

@Controller
@RequestMapping
@SpringBootApplication
public class CJApplication {

    public static void main(String[] args) {
        SpringApplication.run(CJApplication.class, args);
    }

}
