package com.hundsun.demo.spring.redis.advanced;

import redis.clients.jedis.Jedis;

import java.util.Arrays;

public class RedisEvalShaExample {
    public static void main(String[] args) {
        // 创建 Jedis 客户端连接
        Jedis jedis = new Jedis("192.168.80.128", 6379);
        jedis.auth("123456");
        // 定义 Lua 脚本
        String luaScript =
                "local current_count = tonumber(redis.call('get', KEYS[1])) or 0\n" +
                "local new_count = current_count + tonumber(ARGV[1])\n" +
                "redis.call('set', KEYS[1], new_count)\n" +
                "return new_count";

        // 计算 Lua 脚本的 SHA1 校验和
        String scriptSha1 = jedis.scriptLoad(luaScript);

        // 执行 Lua 脚本
        String key = "counter";
        String[] keys = {key};
        String[] values = {"1"};
        Object result = jedis.evalsha(scriptSha1, Arrays.asList(keys), Arrays.asList(values));

        System.out.println("Counter value after Lua script execution: " + result);

        // 关闭 Jedis 连接
        jedis.close();
    }
}
