package org.hulei.keeping.server.redis.basic.advanced;

import redis.clients.jedis.Jedis;

import java.util.Arrays;

public class RedisLuaScript {

    public static void main(String[] args) {
        // 创建 Jedis 客户端连接
        Jedis jedis = new Jedis("192.168.80.128", 6379);
        jedis.auth("123456");

        // 定义 Lua 脚本
        String luaScript =
                // redis.call('get', KEYS[1])：这部分代码使用 Redis 的 GET 命令从 Redis 中获取键 KEYS[1] 对应的值。KEYS 是 Lua 脚本中预定义的一个数组，用于存储传递给 Lua 脚本的键。在这里，KEYS[1] 表示第一个键，也就是 Lua 脚本中的第一个参数。这个命令会返回一个字符串，表示键对应的值。
                // tonumber(...)：Lua 中的 tonumber 函数用于将字符串转换为数字。在这里，我们将 Redis 返回的字符串转换为数字。如果 Redis 返回的字符串不能转换为数字（比如键不存在或者对应的值不是数字字符串），那么 tonumber 函数会返回 nil。
                // or 0：这部分代码使用 Lua 中的逻辑或运算符 or，如果 tonumber 函数返回了 nil，则返回 0，否则返回 tonumber 函数的返回值。这样就保证了即使 Redis 中的键不存在或者对应的值不是数字字符串，我们也可以得到一个默认值 0。
                "local current_count = tonumber(redis.call('get', KEYS[1])) or 0\n" +
                        // current_count：这是一个变量，表示之前获取到的 Redis 键 KEYS[1] 对应的值，即当前的计数器值。这个值是一个数字。
                        // tonumber(ARGV[1])：这部分代码使用 Lua 的 tonumber 函数将 ARGV[1] 转换为数字。ARGV 是 Lua 脚本中预定义的一个数组，用于存储传递给 Lua 脚本的参数。在这里，ARGV[1] 表示第一个参数，也就是 Lua 脚本中的第一个参数。这个参数会在脚本执行时由外部传入。这个命令会返回一个数字，表示参数转换后的值。
                        // new_count = current_count + tonumber(ARGV[1])：这部分代码将当前的计数器值 current_count 和外部传入的参数值相加，得到一个新的计数器值 new_count。这个新的计数器值将用于更新 Redis 中的计数器。
                        "local new_count = current_count + tonumber(ARGV[1])\n" +
                        // redis.call('set', KEYS[1], new_count)：这部分代码调用了 Redis 的 SET 命令，用于设置 Redis 中键 KEYS[1] 对应的值为 new_count。KEYS[1] 是 Lua 脚本中预定义的一个数组，用于存储传递给 Lua 脚本的键。在这里，KEYS[1] 表示第一个键，也就是 Lua 脚本中的第一个参数。new_count 是在前面计算出来的新的计数器值，是一个数字。
                        "redis.call('set', KEYS[1], new_count)\n" +
                        "return new_count";

        // 执行 Lua 脚本
        String key = "counter";
        String[] keys = {key};
        String[] values = {"1"};
        Long result = (Long) jedis.eval(luaScript, Arrays.asList(keys), Arrays.asList(values));

        System.out.println("Counter value after Lua script execution: " + result);

        // 关闭 Jedis 连接
        jedis.close();
    }
}
