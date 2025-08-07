package org.hulei.springboot.redis.redis.spring.advanced;

import com.github.jsonzou.jmockdata.JMockData;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            /*
            Redis Cluster 的 Key 分布在不同的节点上（通过 Slot 分配），而 Pipeline 的所有命令必须发送到同一个节点，否则会触发 MOVED 错误。
            但是这个在 stringRedisTemplate 不会触发，原因是因为 stringRedisTemplate 封装了在发送前自动分类的逻辑
             */
            connection.set(
                    ("{pipeline}:" + JMockData.mock(String.class)).getBytes(StandardCharsets.UTF_8),
                    JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8)
            );

            connection.set(
                    ("{pipeline}:" + "k1").getBytes(StandardCharsets.UTF_8),
                    JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8)
            );

            connection.set(
                    ("{pipeline}:" + JMockData.mock(String.class)).getBytes(StandardCharsets.UTF_8),
                    JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8)
            );

            return null;
        });
    }

    @Autowired
    RedissonClient redisson;

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

        RedisClusterConnection clusterConnection = stringRedisTemplate.getConnectionFactory().getClusterConnection();
        Map<byte[], byte[]> tuple = new HashMap<>();
        tuple.put(JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8), JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));
        tuple.put(JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8), JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));
        tuple.put(JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8), JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));
        tuple.put(JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8), JMockData.mock(String.class).getBytes(StandardCharsets.UTF_8));
        clusterConnection.stringCommands().mSet(tuple); // 这里会执行原生 MSET

        stringRedisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.mSet(tuple);
            return null;
        });

        // ============================================== 使用 redisson buckets 进行 mset ===============================================
        // 这里使用了 slot 不同的 key，但是却并没有触发 CROSSSLOT 报错
        // 在源码 org.redisson.command.CommandAsyncService.executeBatchedAsync 这里可以看到 redisson 客户端会把键按照 slot 分类，相同的slot的一批 key 会使用 mset 执行
        redisson.getBuckets().set(map);
    }
}
