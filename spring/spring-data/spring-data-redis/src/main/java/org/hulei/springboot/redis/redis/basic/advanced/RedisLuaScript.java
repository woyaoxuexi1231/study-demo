package org.hulei.springboot.redis.redis.basic.advanced;

import redis.clients.jedis.Jedis;

import java.util.Arrays;

public class RedisLuaScript {

    public static void main(String[] args) {
        // 创建 Jedis 客户端连接
        Jedis jedis = new Jedis("192.168.80.128", 6379);
        jedis.auth("123456");

        // 定义 Lua 脚本,使用 lua脚本的好处是即使脚本内有大量的命令操作, redis可以保证整个脚本的执行是原子性的(但不支持回滚,redis本身是不支持回滚这种操作的,已经执行的命令不会撤回)
        String luaScript =
                /*
                redis.call是lua脚本来实现对redis访问的api
                redis.call('get', KEYS[1]):
                    1.get指这里使用redis的get命令
                    2.key[1]是在lua脚本中预定义一个参数数组,1表示参数数组的第一个值
                    3,这个命令返回redis的实际结果
                tonumber(...): 用于将字符串转换为数字,如果字符串不能转换为数字那么这个函数将返回nil
                or 0: lua脚本的逻辑或运算,如果前面部分返回的是nil,那么整个语句返回0
                 */
                "local current_count = tonumber(redis.call('get', KEYS[1])) or 0\n" +
                        /*
                        current_count: 上一句中定义的变量,这里获取到上一个命令的结果
                        tonumber(ARGV[1]): 尝试将数值列表的第一个值转换为数字
                        这条命令通过将上述得到的结果加上我们预定义的数值列表的第一个值相加
                         */
                        "local new_count = current_count + tonumber(ARGV[1])\n" +
                        // 执行redis的set命令,设置KEYS[1]的值为新的值
                        "redis.call('set', KEYS[1], new_count)\n" +
                        // 返回我们设置的新的值
                        "return new_count";

        // 执行 Lua 脚本
        String key = "lua::counter";
        String[] keys = {key};
        String[] values = {"1"};

        /*
        要在redis中运行lua脚本,redis提供了两个命令
        1. eval lua脚本内容 key个数 key列表 参数列表
        2. evalsha 脚本的SHA1值 key个数 key列表 参数列表, lua脚本可以预先加载到redis服务器,得到该脚本的SHA1校验和, evalsha仅通过发送SHA1校验和作为参数来直接执行, 避免了每次发送脚本到客户端
         */
        Long result = (Long) jedis.eval(luaScript, Arrays.asList(keys), Arrays.asList(values));

        System.out.println("Counter value after Lua script execution: " + result);

        // 关闭 Jedis 连接
        jedis.close();
    }
}
