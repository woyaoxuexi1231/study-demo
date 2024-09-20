package org.hulei.springboot.redis.redis.spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/9/20 16:51
 */

@RequestMapping("/redisHash")
@RestController
public class RedisHashController {

    /*
    hset key field value [field value ...] 设置值
    hget key field
    hdel key field [field ...] 删除filed
    hlen key 计算指定key的filed的数量
    hmset key field value [field value ...] 批量设置filed
    hmget key field [field ...] 批量获取filed
    hexists key field 检测某个具体的filed是否存在
    hkeys key 获取指定key的所有filed
    hvals key 获取指定key的所有filed的值
    hgetall key 获取指定key的所有filed以及value
    redisson分布式锁实现使用了哈希这种数据结构,主要涉及可重入的设计
     */

}
