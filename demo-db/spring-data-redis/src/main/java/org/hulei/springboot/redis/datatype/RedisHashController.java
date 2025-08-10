package org.hulei.springboot.redis.datatype;

import org.hulei.entity.jpa.pojo.BigDataUser;
import org.hulei.entity.jpa.starter.dao.BigDataUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hulei
 * @since 2024/9/20 16:51
 */

@RequestMapping("/redis-hash")
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


    Redis 的 Hash 类型是基于压缩列表（ziplist）或哈希表（hashtable）实现的键值对集合（类似Java的Map）
    核心特性是支持对对象的部分字段原子操作，且内存效率优于String类型的序列化存储。
    以下是其核心使用场景及实战中需注意的细节，结合具体案例说明：

    1. 存储结构化对象（最常用）
        场景：缓存数据库中的行记录（如用户信息、商品详情、订单基础信息），避免将整个对象序列化为JSON/Protobuf存储（减少反序列化开销，支持字段级更新）。
        实现：
          Key 设计：业务模块:唯一标识（如 user:1001表示用户ID为1001的信息）；
          Field 设计：对象的属性名（如 name、age、email）；
          Value 设计：属性值（字符串类型）。
    2. 统计类计数（字段级增量更新）
        场景：需要按维度统计的指标（如商品的日销量、用户的评论数、标签的出现次数），Hash的字段可独立增减，避免覆盖全量数据。
        实现：
          Key 设计：统计维度:标识（如 product:sales:1001表示商品ID为1001的销量）；
          Field 设计：统计维度（如 daily、weekly）；
          Value 设计：统计值（数值型字符串）。
        示例：
          # 统计商品1001的日销量（字段为日期）
          HINCRBY product:sales:1001 20250807 1  # 2025年8月7日销量+1
          HINCRBY product:sales:1001 20250807 1  # 再次+1（当日销量变为2）

          # 统计用户1001的评论数（字段为固定key）
          HINCRBY user:1001:comments 20250807 1  # 当日评论数+1
          HGET user:1001:comments 20250807  # 返回"1"（当前评论数）
    3. 动态配置管理（灵活扩展字段）
        场景：需要动态调整的配置项（如用户的主题模式、应用的开关配置），Hash的字段可灵活增删，无需修改Key结构。
        实现：
          Key 设计：配置类型:标识（如 user:theme:1001表示用户1001的主题配置）；
          Field 设计：配置项（如 dark_mode、font_size）；
          Value 设计：配置值（字符串/数值）。
    4. 批量操作（减少网络IO）
     */

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    BigDataUserRepository bigDataUserRepository;

    @GetMapping("/sav-user-info-to-redis")
    public void savUserInfoToRedis(@RequestParam(value = "id") Long id) {
        BigDataUser user = bigDataUserRepository.findById(id).orElse(null);
        if (Objects.nonNull(user)) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("name", user.getName());
            userMap.put("email", user.getEmail());
            userMap.put("create_at", user.getCreatedAt());
            // 通过 map 结构化存储用户信息
            redisTemplate.opsForHash()
                    .putAll(
                            "springdataredis:hash:user:" + user.getId(),
                            userMap
                    );
        }
    }

    @GetMapping("/get-user-info-from-redis")
    public void getUserInfoFromRedis(@PathParam("id") Long id) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey("springdataredis:hash:user:" + id))) {

            // 单独拉去用户的每个属性
            // System.out.println("id: " + redisTemplate.opsForHash().get("springdataredis:hash:user:" + id, "id"));
            // System.out.println("name: " + redisTemplate.opsForHash().get("springdataredis:hash:user:" + id, "name"));
            // System.out.println("email: " + redisTemplate.opsForHash().get("springdataredis:hash:user:" + id, "email"));
            // System.out.println("create_at: " + redisTemplate.opsForHash().get("springdataredis:hash:user:" + id, "create_at"));



            // 一次性拉取
            Collection<Object> hashKeys = new ArrayList<>();
            hashKeys.add("id");
            hashKeys.add("name");
            hashKeys.add("email");
            hashKeys.add("create_at");
            List<Object> objects = redisTemplate.opsForHash().multiGet("springdataredis:hash:user:" + id, hashKeys);
            objects.forEach(System.out::println);
        }
    }

}
