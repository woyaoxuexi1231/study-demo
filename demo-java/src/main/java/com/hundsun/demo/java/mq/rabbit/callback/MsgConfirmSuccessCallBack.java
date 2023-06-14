package com.hundsun.demo.java.mq.rabbit.callback;

import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.mq.rabbit.callback
 * @className: MsgConfirmCallBack
 * @description:
 * @author: h1123
 * @createDate: 2023/3/1 22:47
 */

public class MsgConfirmSuccessCallBack implements ConfirmCallback {

    @Override
    public void handle(long deliveryTag, boolean multiple) throws IOException {
        System.out.println("消息发送成功! ");
    }
}
