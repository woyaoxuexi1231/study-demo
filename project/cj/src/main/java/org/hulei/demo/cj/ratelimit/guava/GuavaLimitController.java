package org.hulei.demo.cj.ratelimit.guava;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limit")
public class GuavaLimitController {

    @GetMapping("/limitByGuava")
    @GuavaLimitRateAnnotation(limitType = "测试限流", limitCount = 1)
    public String limitByGuava() {
        return "limitByGuava";
    }

}
