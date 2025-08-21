package org.hulei.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(
        topic = "demo-topic",         // 要监听的 Topic 名称
        selectorExpression = "*",     // 可以指定 tag，比如 "tagA"，或者 "*" 表示所有 tag
        consumerGroup = "my-consumer-group"  // 必须和 application.yml 中的一致或协调
)
public class DemoRocketMQConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("🟡 [Consumer] 收到消息: " + message);
    }
}