package org.hulei.springboot.redis.datatype;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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


    /*
    Redis 的 List 类型基于双向链表实现（Redis 3.2+ 后支持 quicklist优化存储），支持O(1)时间复杂度的头部/尾部插入/删除操作
    天然适合实现队列（FIFO）、栈（LIFO）或有序列表。以下是其核心使用场景及实战中需注意的细节，结合具体案例说明：

    1. 异步任务队列（生产者-消费者模型）
        场景：高并发场景下，将耗时操作（如短信发送、邮件通知、图片压缩）从主流程剥离，通过List实现异步处理，提升系统吞吐量。
        实现：
          生产者：用 LPUSH（左插入）或 RPUSH（右插入）将任务序列化后存入List；
          消费者：用 BRPOP（阻塞式右弹出）或 BLPOP（阻塞式左弹出）监听List，无任务时阻塞等待（避免轮询空转）。
    2. 消息队列（简单版Pub/Sub）
        需要「至少一次」可靠投递的消息场景（如订单状态变更通知），List配合ACK机制可实现比Pub/Sub更可靠的消息传递（Pub/Sub是广播模式，无持久化）。
        实现：
          生产者：RPUSH消息到List；
          消费者：LPOP取出消息后，先处理业务逻辑，再通过 LTRIM或 RPOP确认消费（避免重复）；
          异常处理：若消费者崩溃，未确认的消息仍留在List中，可被其他消费者重新拉取。
    3. 最新动态列表（如“最近N条”记录）
        场景：需要展示用户最近操作记录（如最近5条评论、最近10条浏览历史），List的有序性（按插入顺序）天然适配。
        实现：
          插入新记录：用 LPUSH（左插入，最新的在最前）或 RPUSH（右插入，最新的在最后）；
          限制长度：用 LTRIM key start end截断超出长度的部分（如保留最近10条）。
    4. 历史操作栈（撤销/重做功能）
        场景：需要支持撤销/重做的功能（如文档编辑、画图工具），List的LIFO特性可作为简单栈使用。
        实现：
          记录操作：用 RPUSH将操作步骤压入栈（如“添加文字”“删除图片”）；
          撤销操作：用 LPOP弹出最近一步操作并反向执行（如“撤销删除图片”即重新插入图片）；
          重做操作：用 RPOP弹出已撤销的操作（需额外维护一个“已撤销栈”）。

    🚨 并发消费：避免消息丢失或重复，没有ack机制的
    🚨 操作复杂度：避免高频 LRANGE扫描

     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * 表示当前是否已经有监听线程
     */
    private volatile boolean status = false;
    /**
     * 表示当前是否需要停止监听
     */
    private volatile boolean stop = false;

    @GetMapping("/start-listen")
    public void startListen() {
        if (!status) {
            executorService.execute(() -> {
                log.info("开始使用 brpop 阻塞的从 springdataredis:list:queue 队列中获取消息...");
                while (!stop) {
                    try {
                        // 其实这里是 brpop key timout
                        log.info("这是一条使用 redis list 实现的消息队列收到的消息, {}",
                                redisTemplate.opsForList().rightPop("springdataredis:list:queue", 10, TimeUnit.SECONDS));
                    } catch (Exception e) {
                        log.error("消息接收异常,线程将停止", e);
                        break;
                    }
                }
            });
            status = true;
        }
    }

    @GetMapping("/end-listen")
    public void endListen() {
        stop = true;
    }

    @GetMapping("/produce")
    public void produceAsync(String message) {
        // lpush key element
        redisTemplate.opsForList().leftPush("springdataredis:list:queue", message);
    }
}

