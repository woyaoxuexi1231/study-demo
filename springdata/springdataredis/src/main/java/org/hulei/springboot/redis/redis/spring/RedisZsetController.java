package org.hulei.springboot.redis.redis.spring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/9/20 17:02
 */

@RequestMapping("/redisZset")
@RestController
public class RedisZsetController {

    /*
    有序集合
        集合内:
            zadd key [NX|XX] [GT|LT] [CH] [INCR] score member [score member ...]
                nx/xx 和 set的类似
                GT/LT 控制更新分数的条件, 仅当新分数大于/小于当前成员的分数的时候才允许更新成员分数
                CH 返回此次操作后,有序集合元素和分数变化的个数
                INCR 对score做增加
            zcard key 计算成员个数
            zscore key member 得到某个成员的分数
            zrank key member 计算成员的排名,分数从低到高排名,第一名是0
            zrevrank key member 计算成员的排名,分数从高到低
            zrem key member [member ...] 移除元素
            zincrby key increment member 增加成员的分数
            zrange key min max [BYSCORE|BYLEX] [REV] [LIMIT offset count] [WITHSCORES]
                BYSCORE 按照分数排名,如果加了这个选项,那么min和max就代表分数的范围,而不是排名的范围
                bylex 按照字典排序, ZRANGE myset [a [z BYLEX
                rev不能与上面两个命令同时使用
                withscore 返回带上元素分数
            zrangebyscore key min max [WITHSCORES] [LIMIT offset count] 指定分数范围内的所有元素排名
            zrevrangebyscore key max min [WITHSCORES] [LIMIT offset count] 指定分数范围内的所有元素排名,从高到低
            zcount zkey1 min max 指定范围内的元素个数
            zremrangebyrank key start stop 删除指定排名内的元素(从低到高)
            zremrangebyscore key min max 删除指定分数范围内的元素

        集合键操作
            zinter zunion zdiff
            zinterstore zunionstore zdiffstore
     */
}
