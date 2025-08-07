package org.hulei.springboot.redis.redis.spring.datatype;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author hulei
 * @since 2024/9/20 16:58
 */

@Slf4j
@RequestMapping("/redisSet")
@RestController
public class RedisSetController {

    /*
     集合:
     集合内操作:
     操作元素:
        sadd key member [member ...] 添加元素,可以添加多个
        srem key member [member ...] 删除元素,可以删除多个
        spop key 随机弹出一个元素

    得到元素:
        scard key 查看元素个数
        srandmember key [count] 随机得到元素,不指定count默认弹出一个,这个操作不会删除元素
        sismember key member 判断指定的元素是否在集合内
        smembers key 得到所有元素,这是一个重量级操作,如果集合内元素过多,那么将耗费大量时间和cpu资源

     集合间操作:
        sinter key [key ...] 多个集合的交集
        sunion key [key ...] 多个集合的并集
        sdiff key [key ...] 返回第一个集合与其他集合的不同的值
        每个命令加store就是把对应的结果保存到指定的key
        sdiffstore destination key [key ...] 把多个集合的差集保存到新的 destination 集合


    Redis 的 Set 类型基于哈希表或整数集合（Intset）实现，核心特性是元素唯一、无序，并支持丰富的集合运算（交集、并集、差集）。
    以下是其核心使用场景及实战中需注意的细节，结合具体案例说明：

    1. 数据去重（唯一性约束）
        场景：需要保证数据唯一性的场景（如用户签到记录、商品收藏、活动参与资格），Set的「元素唯一」特性可天然去重。
        实现：
          添加元素：用 SADD key member（自动去重，重复添加不生效）；
          校验存在：用 SISMEMBER key member（O(1)时间复杂度判断是否存在）；
          统计数量：用 SCARD key（获取集合大小）。
    2. 标签/分类管理
        场景：需要为对象打标签（如用户兴趣标签、商品分类标签），Set可快速实现标签的添加、删除、查询。
        实现：
          添加标签：SADD key tag1 tag2；
          删除标签：SREM key tag1；
          查询所有标签：SMEMBERS key；
          交集标签（共同兴趣）：SINTER key1 key2（如找同时喜欢“篮球”和“电影”的用户）。
    3. 社交关系（好友/粉丝/共同关注）
        场景：社交平台中管理好友关系（如互相关注、共同好友、黑名单），Set的无序性和集合运算能高效处理这类需求。
        实现：
          关注/取关：SADD follow:user1001 user2002（关注）、SREM follow:user1001 user2002（取关）；
          共同好友：SINTER follow:user1001 follow:user2002（两人共同关注的人）；
          粉丝列表：SMEMBERS followers:user2002（关注user2002的用户集合）；
          黑名单：SADD blacklist:user1001 user3003（禁止user3003互动）。
    4. 抽奖/抢购去重（防重复参与）
        场景：抽奖活动中，避免同一用户多次参与；或抢购时防止重复下单，Set可快速校验用户是否已参与。
        实现：
          参与标记：SADD lottery:1001:user user1001（活动ID为1001，用户ID为1001）；
          校验参与：SISMEMBER lottery:1001:user user1001；
          统计参与人数：SCARD lottery:1001:user。
    5. 随机抽取（如随机推荐、随机任务）
        场景：需要从集合中随机选取元素（如随机推荐商品、随机分配任务），Set的 SRANDMEMBER命令可高效实现。
        实现：
          随机取1个：SRANDMEMBER key；
          随机取N个（不重复）：SRANDMEMBER key N；
          随机删除并返回：SPOP key（Redis 2.6+支持）。

    🚨 集合运算性能：大Set交集/并集耗时
    🚨 元素类型：仅支持字符串
    🚨 过期策略：仅Key级过期
     */

    @Qualifier(value = "strObjRedisTemplate")
    @Autowired
    RedisTemplate<String, Object> strObjRedisTemplate;


    @RequestMapping("/fans")
    public void fans() {

        // 对于用户 Aurelia 来说,这算是他的关注列表,他一共关注了四个人 sadd key
        strObjRedisTemplate.opsForSet().add("Aurelia", "Nou", "Tyhesha", "Darrion", "Saroeun");

        // 对于用户 Nou 来说,这算是他的关注列表,他也关注了四个人 sadd key
        strObjRedisTemplate.opsForSet().add("Nou", "Sulaiman", "Tyhesha", "Darrion", "Crystle");

        // 这两个人的共同关注，这其实redis有提供这相关api可以使用 sinter key1 key2
        Set<Object> intersect = strObjRedisTemplate.opsForSet().intersect("Aurelia", "Nou");

        // 1.求并集  sunion key1 key2
        Set<Object> union = strObjRedisTemplate.opsForSet().union("Aurelia", "Nou");
        // 2.求差集 sdiff key1 key2
        Set<Object> difference = strObjRedisTemplate.opsForSet().difference("Aurelia", "Nou");

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("Aurelia 和 Nou 的共同关注(inter操作)", CollectionUtils.isEmpty(intersect) ? "null" : intersect.toString());
        resultMap.put("Aurelia 和 Nou 的所有关注(union操作)", CollectionUtils.isEmpty(union) ? "null" : union.toString());
        resultMap.put("Aurelia 和 Nou 两个关注的不同的人(diff操作)", CollectionUtils.isEmpty(difference) ? "null" : difference.toString());

        resultMap.forEach((k, v) -> System.out.printf("%s-%s\n", k, v));

        // 随机取值
        System.out.println(strObjRedisTemplate.opsForSet().randomMember("Aurelia"));
        System.out.println(strObjRedisTemplate.opsForSet().randomMember("Aurelia"));
    }
}
