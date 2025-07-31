package org.hulei.springboot.rabbitmq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hulei
 * @since 2024/9/19 19:13
 */

@MapperScan(basePackages = {"org.hulei.springboot.rabbitmq.spring.consumer.mapper"})
@SpringBootApplication
public class RabbitmqApplication {

    public static void main(String[] args) {
        /*
        消息队列(MQ)的出现主要就是为了解决三大问题：
        1. 解耦：降低系统组件之间的依赖关系
        2. 异步：发送方不用等待接收方立即响应就可以继续执行后续任务，接收方只需要在合适的时间处理请求即可
        3. 削峰：通过缓冲机制平滑瞬时高并发请求，避免系统因流量突增而崩溃

        问题2：spring.rabbitmq.publisher-confirm-type 的 simple 和 correlated 到底有啥区别？
         */
        SpringApplication.run(RabbitmqApplication.class, args);
    }
}
