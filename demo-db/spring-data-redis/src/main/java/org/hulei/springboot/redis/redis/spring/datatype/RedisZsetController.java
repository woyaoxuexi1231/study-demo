package org.hulei.springboot.redis.redis.spring.datatype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.DefaultTuple;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hulei
 * @since 2024/9/20 17:02
 */

@RequiredArgsConstructor
@RequestMapping("/redisZset")
@RestController
public class RedisZsetController {

    /*
    有序集合
        集合内:

        操作元素:
            zadd key [NX|XX] [GT|LT] [CH] [INCR] score member [score member ...]
                nx/xx 和 set的类似
                GT/LT 控制更新分数的条件, 仅当新分数大于/小于当前成员的分数的时候才允许更新成员分数
                CH 返回此次操作后,有序集合元素和分数变化的个数
                INCR 对score做增加
            zrem key member [member ...] 移除元素
            zincrby key increment member 增加成员的分数
            zremrangebyrank key start stop 删除指定排名内的元素(从低到高)
            zremrangebyscore key min max 删除指定分数范围内的元素

        查询单个元素
            zscore key member 得到某个成员的分数
            zrank key member 计算成员的排名,分数从低到高排名,第一名是0
            zrevrank key member 计算成员的排名,分数从高到低

        查询元素(操作主要都针对范围操作,获得指定的最大最小值之间的元素):
            zrange key min max [BYSCORE|BYLEX] [REV] [LIMIT offset count] [WITHSCORES]
                BYSCORE 按照分数排名,如果加了这个选项,那么min和max就代表分数的范围,而不是排名的范围
                bylex 按照字典排序, ZRANGE myset [a [z BYLEX
                rev不能与上面两个命令同时使用
                withscore 返回带上元素分数
            zrangebyscore key min max [WITHSCORES] [LIMIT offset count] 指定分数范围内的所有元素排名
            zrevrangebyscore key max min [WITHSCORES] [LIMIT offset count] 指定分数范围内的所有元素排名,从高到低
            zcount zkey1 min max 指定范围内的元素个数

            zcard key 计算成员个数

        集合键操作:
            zinter zunion zdiff
            zinterstore zunionstore zdiffstore



    1. 排行榜（最常用）
        场景：需要按分数/热度排序的场景（如游戏积分榜、商品销量榜、热搜榜），ZSet的分数排序特性可天然支持动态排名。
        实现：
          Key 设计：业务模块:排行榜类型（如 game:score:rank表示游戏积分排行榜）；
          Score 设计：排序依据（如游戏积分、商品销量、热搜指数）；
          Member 设计：唯一标识（如用户ID、商品ID）。
    2. 时间线/动态流（按时间排序）
        场景：需要按时间顺序展示的动态（如微博热搜、朋友圈动态、新闻列表），ZSet的分数可存储时间戳（精确到毫秒），实现按时间排序。
        实现：
          Key 设计：业务模块:动态类型（如 weibo:hot:feed表示微博热搜动态）；
          Score 设计：时间戳（如 1717651200000表示2025年8月7日0点）；
          Member 设计：动态内容ID（如 feed:1001）。
    3. 优先级任务队列（按优先级处理）
        场景：需要按优先级执行的任务（如订单超时处理、消息重试、资源分配），ZSet的分数可表示任务优先级（数值越小优先级越高）。
        实现：
          Key 设计：任务类型:队列（如 order:timeout:queue表示订单超时任务队列）；
          Score 设计：优先级（如 1表示最高优先级，10表示最低）；
          Member 设计：任务ID（如 task:1001）。
    4. 范围查询（如统计某段时间内的数据）
        场景：需要按分数范围筛选数据的场景（如统计近30天的活跃用户、查询价格区间内的商品），ZSet的 ZRANGEBYSCORE命令可高效实现。
        实现：
          Key 设计：业务模块:范围统计（如 user:active:30d表示近30天活跃用户）；
          Score 设计：时间戳（如用户最后活跃时间）；
          Member 设计：用户ID（如 user:1001）。
    5. 延迟队列（按延迟时间触发任务）
        场景：需要延迟执行的任务（如30分钟后自动取消未支付订单、定时提醒），ZSet的分数存储任务的延迟截止时间（时间戳），通过轮询取出到期任务。
        实现：
          Key 设计：延迟任务:类型（如 delay:order:cancel表示订单取消延迟任务）；
          Score 设计：延迟截止时间戳（如当前时间+30分钟）；
          Member 设计：任务详情（如 order:1001:cancel）。
     */

    private final RedisTemplate<String,Object> redisTemplate;
    private static final String LEADERBOARD_KEY = "springdataredis:zset:game:leaderboard";

    // 更新玩家分数s
    public void updateScore(String playerId, double score) {
        redisTemplate.opsForZSet().add(LEADERBOARD_KEY, playerId, score);
    }

    // 获取前N名玩家（降序）
    public List<PlayerScore> getTopPlayers(int topN) {

        // zrevrangebyscore
        Set<ZSetOperations.TypedTuple<Object>> tuples =
                redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, topN - 1);

        assert tuples != null;
        return tuples.stream()
                .map(tuple -> new PlayerScore(
                        (String) tuple.getValue(),
                        tuple.getScore()))
                .collect(Collectors.toList());
    }

    // 获取玩家排名（降序，从0开始）
    public Long getPlayerRank(String playerId) {
        return redisTemplate.opsForZSet().reverseRank(LEADERBOARD_KEY, playerId);
    }

    // 获取玩家分数
    public Double getPlayerScore(String playerId) {
        return redisTemplate.opsForZSet().score(LEADERBOARD_KEY, playerId);
    }

    // DTO类
    @Data
    @AllArgsConstructor
    public static class PlayerScore {
        private String playerId;
        private Double score;
    }

    // 初始化排行榜数据
    @PostConstruct
    public void initLeaderboardData() {
        // 清空现有数据
        redisTemplate.delete(LEADERBOARD_KEY);

        // 添加预设玩家数据
        updateScore("player1", 2500);
        updateScore("player2", 3200);
        updateScore("player3", 1800);
        updateScore("player4", 2950);
        updateScore("player5", 3100);

        System.out.println("排行榜初始化完成，预设了5个玩家数据");
    }

    @GetMapping("/demo")
    public void demo() {
        System.out.println("=== 排行榜演示 ===");
        System.out.println("Top 3玩家:");
        getTopPlayers(3).forEach(p ->
                System.out.println(p.getPlayerId() + ": " + p.getScore()));

        System.out.println("\nplayer2的排名: " + (getPlayerRank("player2") + 1));
        System.out.println("player1的分数: " + getPlayerScore("player1"));
    }
}
