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

    操作元素:
    hset key field value [field value ...] 设置值
    hdel key field [field ...] 删除filed
    hmset key field value [field value ...] 批量设置filed

    查询元素:
    hget key field
    hlen key 计算指定key的filed的数量
    hmget key field [field ...] 批量获取filed
    hexists key field 检测某个具体的filed是否存在
    hkeys key 获取指定key的所有filed 重量级操作
    hvals key 获取指定key的所有filed的值 重量级操作
    hgetall key 获取指定key的所有filed以及value 重量级操作

    redisson 分布式锁实现使用了哈希这种数据结构,主要涉及可重入的设计
    为什么使用hash这种数据结构呢? 不仅仅要存储锁的名字,还要保存当前持有锁的对象是谁以及重入的次数

     */

}
