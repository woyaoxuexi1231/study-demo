package org.hulei.springboot.redis.redisson.spring;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/1/8 14:29
 */

@RequestMapping("/redisson")
@RestController
public class RedissonSpringController {

    @Autowired
    RedissonClient redisson;

    @RequestMapping("/test")
    public void test() {

    }
}
