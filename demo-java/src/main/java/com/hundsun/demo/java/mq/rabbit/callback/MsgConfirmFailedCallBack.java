package com.hundsun.demo.java.mq.rabbit.callback;

import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq.rabbit.callback
 * @className: MsgConfirmFailedCallBack
 * @description:
 * @author: h1123
 * @createDate: 2023/3/1 22:48
 */

public class MsgConfirmFailedCallBack implements ConfirmCallback {

    @Override
    public void handle(long deliveryTag, boolean multiple) throws IOException {
        // 失败消息入列, 重新发送
        System.out.println("消息发送失败! ");
    }
}
