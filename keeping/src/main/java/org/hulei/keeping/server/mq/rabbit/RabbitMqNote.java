package org.hulei.keeping.server.mq.rabbit;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.mq.rabbit
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-12-19 15:10
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class RabbitMqNote {
    /*
    channel error; protocol method: #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - inequivalent arg 'x-dead-letter-exchange' for queue 'topic-queue-master' in vhost '/': received the value 'dead-exchange' of type 'longstr' but current is none, class-id=50, method-id=10)
    队列 ‘topic-queue-master’ 的参数 ‘x-dead-letter-exchange’ 发生了不满足预期的变化。具体来说，当前队列参数 ‘x-dead-letter-exchange’ 的类型是 ‘none’，但是收到的值却是 ‘dead-exchange’，而且被解释为了 ‘longstr’ 类型，导致了不一致的错误。
     */
}
