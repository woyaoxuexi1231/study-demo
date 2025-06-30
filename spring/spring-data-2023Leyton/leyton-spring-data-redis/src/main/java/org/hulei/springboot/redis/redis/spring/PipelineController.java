package org.hulei.springboot.redis.redis.spring;

import com.github.jsonzou.jmockdata.JMockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hulei
 * @since 2025/6/29 11:12
 */

@RequestMapping("/pipeline")
@RestController
public class PipelineController {


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/pipeline")
    public void test() {

        /*
        如果需要一次性插入大量的数据，那么使用单个命令进行 set/lpush/sadd/hset 网络开销大，性能开销大
        所以redis提供了 pipeline 进行命令打包发送，这样可以减少网络开销和性能开销
        当然 mset 这个命令也是可以的
         */
        stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            // !!! 这里需要注意一点是，如果是redis cluster集群，需要
            connection.set(("{pipeline}:" + JMockData.mock(String.class)).getBytes(StandardCharsets.UTF_8),
                    JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));

            connection.set(("{pipeline}:" + "k1").getBytes(StandardCharsets.UTF_8), JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));

            connection.incr(("{pipeline}:" + "k1").getBytes(StandardCharsets.UTF_8));

            connection.set(("{pipeline}:" + JMockData.mock(String.class)).getBytes(StandardCharsets.UTF_8),
                    JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));

            return null;
        });
    }


    // @Autowired
    // RedissonClient redisson;

    @RequestMapping("/mset")
    public void mset() {

        // ============================================== 使用 stringRedisTemplate multiSet 进行 mset ===============================================
        // 在 io.lettuce.core.cluster.RedisAdvancedClusterAsyncCommandsImpl#mset 这个方法中也可以看到会根据 key 进行 slot 分类
        // 最后相同的 slot 执行一个 mset，所以这里也不会报错 CROSSSLOT
        Map<String, String> map = new HashMap<>();
        map.put("{mset}:" + JMockData.mock(String.class), JMockData.mock(String.class));
        map.put("{mset}:" + JMockData.mock(String.class), JMockData.mock(String.class));
        map.put("{mset}:" + JMockData.mock(String.class), JMockData.mock(String.class));
        map.put(JMockData.mock(String.class), JMockData.mock(String.class));
        stringRedisTemplate.opsForValue().multiSet(map);

        Map<byte[], byte[]> tuple = new HashMap<>();
        tuple.put(JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8), JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));
        tuple.put(JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8), JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));
        tuple.put(JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8), JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));
        tuple.put(JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8), JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));

        // RedisClusterConnection clusterConnection = stringRedisTemplate.getConnectionFactory().getClusterConnection();
        // clusterConnection.stringCommands().mSet(tuple); // 这里会执行原生 MSET


        // stringRedisTemplate.execute(new RedisCallback<Object>() {
        //     @Override
        //     public Object doInRedis(RedisConnection connection) throws DataAccessException {
        //         connection.mSet(tuple);
        //         return null;
        //     }
        // });


        // ============================================== 使用 redisson buckets 进行 mset ===============================================
        // 这里使用了 slot 不同的 key，但是却并没有触发 CROSSSLOT 报错
        // 在源码 org.redisson.command.CommandAsyncService.executeBatchedAsync 这里可以看到 redisson 客户端会把键按照 slot 分类，相同的slot的一批 key 会使用 mset 执行
        // redisson.getBuckets().set(map);
    }
}
