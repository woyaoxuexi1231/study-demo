package com.hundsun.demo.springboot.redis.basic;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonExample {

    public static void main(String[] args) {
        // 创建 Redisson 配置对象
        Config config = new Config();
        config.useSingleServer()
              .setAddress("redis://192.168.80.128:6379")
              .setPassword("123456");

        // 创建 Redisson 客户端
        RedissonClient redisson = Redisson.create(config);

        // 进行数据操作
        try {
            // 获取字符串对象
            RBucket<Object> key2 = redisson.getBucket("key2");
            System.out.println("Value of key2: " + key2.get());

            // 设置字符串对象
            redisson.getBucket("key2").set("value2");

            // 获取 Map 对象
            redisson.getMap("map1").put("field1", "value1");

            // 获取 List 对象
            redisson.getList("list1").add("item1");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭 Redisson 客户端
            redisson.shutdown();
        }
    }
}
