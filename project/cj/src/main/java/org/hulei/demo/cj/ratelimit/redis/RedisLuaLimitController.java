package org.hulei.demo.cj.ratelimit.redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limit")
public class RedisLuaLimitController {

    @GetMapping("/limitByRedis")
    @RedisLimitAnnotation(key = "limitByRedis", period = 1, count = 1, limitType = LimitTypeEnum.IP)
    public String limitByRedis() {
        return "limitByRedis";
    }

}
