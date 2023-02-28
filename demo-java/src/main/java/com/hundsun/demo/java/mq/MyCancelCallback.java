package com.hundsun.demo.java.mq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq
 * @className: MyCancelCallback
 * @description:
 * @author: h1123
 * @createDate: 2023/2/28 23:17
 */
@Slf4j
public class MyCancelCallback implements CancelCallback {

    private CountDownLatch count;

    /**
     * 消费消息被中断时触发
     * 当一个消费者取消订阅时的回调接口; 取消消费者订阅队列时除了使用{@link Channel#basicCancel}之外的所有方式都会调用该回调方法
     */
    public MyCancelCallback(CountDownLatch count) {
        this.count = count;
    }

    @Override
    public void handle(String s) throws IOException {
        log.info("消费消息被中断!");
        count.countDown();
    }
}
