package org.hulei.springboot.redis.redisson.basic;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

@Slf4j
public class RedissonExample {

    public static void main(String[] args) {

        RedissonClient redissonClient = RedissonConnectionFactory.getRedissonClient();

        // 进行数据操作
        try {
            // 获取字符串对象
            RBucket<Object> key2 = redissonClient.getBucket("key2");
            System.out.println("Value of key2: " + key2.get());

            // 设置字符串对象
            redissonClient.getBucket("key2").set("value2");

            // 获取 Map 对象
            redissonClient.getMap("map1").put("field1", "value1");

            // 获取 List 对象
            redissonClient.getList("list1").add("item1");

        } catch (Exception e) {
            log.error("redisson exception", e);
        } finally {
            // 关闭 Redisson 客户端
            redissonClient.shutdown();
        }
    }
}
