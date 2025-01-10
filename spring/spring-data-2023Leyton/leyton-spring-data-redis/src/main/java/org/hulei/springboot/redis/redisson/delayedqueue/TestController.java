package org.hulei.springboot.redis.redisson.delayedqueue;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("order")
public class TestController {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    AddTaskToDelayQueue addTaskToDelayQueue;

    @GetMapping("/testRedissonDelayQueueTake")
    public void testRedissonDelayQueueTake() {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque("orderQueue");
        // 注意虽然delayedQueue在这个方法里面没有用到，但是这行代码也是必不可少的。
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        while (true) {
            String orderId = null;
            try {
                orderId = blockingDeque.take();
            } catch (Exception e) {
                System.err.println(e.getStackTrace());
                continue;
            }

            if (orderId == null) {
                continue;
            }

            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "延时队列收到:" + orderId);
        }
    }

    @GetMapping("/testRedissonDelayQueueOffer")
    public void testRedissonDelayQueueOffer(@RequestParam(value = "timeout") Integer timeout) {
        addTaskToDelayQueue.addTaskToDelayQueue(timeout);
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(null));
    }
}
