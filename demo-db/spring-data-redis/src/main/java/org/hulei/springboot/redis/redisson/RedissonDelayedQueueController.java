package org.hulei.springboot.redis.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RLock;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2025/8/7 19:37
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/redisson-delayed-queue")
public class RedissonDelayedQueueController {

    final ThreadPoolExecutor commonPool;
    final RedissonClient redissonClient;

    /*
    实现原理: 一个有序集合 一个队列 一个监听channel
    redisson_delay_queue_timeout:{队列名} 有序集合 过期时间作为分数
    redisson_delay_queue:{队列名} 队列list, 按顺序保存所有元素
    redisson_delay_queue_channel:{队列名} 会创建一个监听信道

    redisson内部处理任务时，会加上随机数，保证每个任务都被当作一个唯一的任务

    具体操作：
    RedissonDelayedQueue 被创建时会创建一个 task，task 会负责任务的延迟推送
      1. task 会订阅 redisson_delay_queue_channel 这个信道
      2. 当消息生产者推送消息时，会往 redisson_delay_queue_channel 推送消息
      3. task 收到信道消息后(redis的后台线程在接收)，立马执行一次 pushTaskAsync 方法，此方法包含非常重要的逻辑
      4. pushTaskAsync 按分数排序查询 redisson_delay_queue:{队列名} 的元素，然后把过期元素移动到配置的 destinationQueue
      5. 获取最新一个即将要过期的元素的时间戳，然后根据这个时间戳来创建定时任务，相当于再定时执行 pushTaskAsync 方法的逻辑
     */

    // 目标队列（实际消费的队列）
    private RBlockingQueue<String> targetQueue;
    // 延迟队列（用于添加延迟元素）
    private RDelayedQueue<String> delayedQueue;

    @PostConstruct
    public void init() {
        // 第一个操作非常简单，创建了一个阻塞队列，这就是一个很普通的阻塞队列
        this.targetQueue = redissonClient.getBlockingQueue("targetQueue");
        // 这里是创建一个延时队列，并且配置了目标队列，目标队列是在延时队列内任务到期后，任务会自动推送到目标队列
        // 消费者直接消费目标队列的任务以达到延迟消费的目的
        this.delayedQueue = redissonClient.getDelayedQueue(targetQueue);
    }

    /*
    动态的订阅和取消订阅，简单的实现
     */
    volatile boolean isSub = false;
    final Object lock = new Object();
    Thread currentSubThread;

    @PostMapping("/subscribe")
    public void subscribe() {
        commonPool.execute(() -> {
            synchronized (lock) {
                if (!isSub) {
                    commonPool.execute(() -> {
                        while (isSub) {
                            try {
                                // 这种方式不是很好，会一直占用线程，即使当前已经取消订阅了
                                // String take = targetQueue.take();
                                // 改用 poll + timeout
                                String poll = targetQueue.poll(2, TimeUnit.SECONDS);
                                System.out.println("消息为：" + poll);
                            } catch (Exception e) {
                                log.error("线程被中断，订阅取消！", e);
                            }
                        }
                    });
                    isSub = true;
                    currentSubThread = Thread.currentThread();
                    log.info("订阅成功！");
                } else {
                    log.info("当前已有其他线程订阅！跳过");
                }
            }
        });
    }

    @PostMapping("/unsubscribe")
    public void unsubscribe() {
        synchronized (lock) {
            if (isSub) {
                isSub = false;
                currentSubThread.interrupt();
                currentSubThread = null;
                log.info("已取消订阅！");
            }
        }
    }

    @GetMapping("/addTask")
    public String addTask(@RequestParam("timeout") Integer timeout) {
        String message = "订单超时取消-" + System.currentTimeMillis();
        // 添加延迟任务（10秒后进入目标队列）
        delayedQueue.offer(message, timeout, TimeUnit.SECONDS);
        return "任务已添加: " + message;
    }

    @GetMapping("/test")
    public void test() {
        RQueue<Object> test = redissonClient.getQueue("test");
    }
}
