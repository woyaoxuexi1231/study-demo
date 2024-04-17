package com.hundsun.demo.springboot.actuate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woaixuexi
 * @since 2024/4/17 23:41
 */

@RestController
public class ActuateController {

    @Value("${refresh}")
    String refresh;

    @GetMapping("/refresh")
    public String getRefresh() {
        return refresh;
    }
}
