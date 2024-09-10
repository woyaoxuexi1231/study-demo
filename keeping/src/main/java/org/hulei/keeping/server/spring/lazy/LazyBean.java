package org.hulei.keeping.server.spring.lazy;

import org.hulei.keeping.server.redis.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author woaixuexi
 * @since 2024/3/25 0:31
 */

@Lazy
@Component
public class LazyBean {

    @Autowired
    RedisConfig redisConfig;

    public LazyBean() {
        System.out.println("LazyBean此时开始实例化");
    }

    @PostConstruct
    public void init() {
        System.out.println("LazyBean此时开始初始化");
        System.out.printf("redisConfig: %s%n", redisConfig);
    }

    public void print() {
        System.out.println("this is lazy bean");
    }
}
