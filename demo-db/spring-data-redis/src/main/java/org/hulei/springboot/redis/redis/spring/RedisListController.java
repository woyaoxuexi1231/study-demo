package org.hulei.springboot.redis.redis.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * redis消息队列,这是比较原始的方式,直接我们自己写实现
 * <p>
 * 使用redis的list这种数据结构,使用队列的左侧加入,右侧阻塞弹出来实现
 */

@Slf4j
@RestController("/redisListMQ")
public class RedisListController {

    /*
    插入:
        rpush key element [element ...] 从右侧插入元素
        lpush key element [element ...] 从左侧插入元素
        linsert key BEFORE|AFTER pivot element , pivot是具体的元素值, 在pivot的前面或者后面插入element
    查询:
        lrange key start stop 这是一个重量级操作,需要注意
        lindex key index 查询指定key的指定位置的元素
        llen key 获取列表的长度
    删除:
        lpop key [count] 左侧出列, count指定数量
        rpop key [count] 右侧出列, count指定数量
        lrem key count element 在列表中找到指定的元素之后移除, count指定数量
        ltrim key start stop 修剪队列,只保留start 到 end的值
    修改:
        lset key index element 修改指定位置的元素为新值
    阻塞操作:
        brpop key [key ...] timeout
        blpop key [key ...] timeout
        1. 多个key时只要有一个key有元素可以弹出就会立马返回
        2. timeout=0的时候会无限阻塞,直到有元素可以获取

    队列没有一个固定的查询队里内所有元素的api,但是可以通过 lrange 0 -1来实现
     */

    /**
     * 队列名,其实是个key
     */
    private static final String QUEUE_NAME = "redis-list-mq";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            log.info("开始使用 brpop 阻塞的从 redis-list-queue-consume 队列中获取消息...");
            while (true) {
                try {
                    // 其实这里是 brpop key timout
                    log.info("这是一条使用 redis list 实现的消息队列收到的消息, {}", redisTemplate.opsForList().rightPop(QUEUE_NAME, 0, TimeUnit.SECONDS));
                } catch (Exception e) {
                    log.error("消息接收异常,线程将停止", e);
                    break;
                }
            }
        }, "redis-list-queue-consume").start();
    }

    @GetMapping("/produce")
    public void produceAsync(String message) {
        // lpush key element
        redisTemplate.opsForList().leftPush(QUEUE_NAME, message);
    }
}

