package org.hulei.springboot.rabbitmq.basic.work;

import com.rabbitmq.client.Connection;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.mq.rabbit.work
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-12-22 18:43
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public abstract class MsgConsumer implements Runnable {

    protected final Connection connection;

    protected final String queueName;

    public MsgConsumer(Connection connection, String queueName) {
        this.connection = connection;
        this.queueName = queueName;
    }
}
