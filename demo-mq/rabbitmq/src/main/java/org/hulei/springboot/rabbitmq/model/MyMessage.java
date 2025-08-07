package org.hulei.springboot.rabbitmq.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.consumer.api.model
 * @className: MQIdempotency
 * @description:
 * @author: h1123
 * @createDate: 2023/3/18 22:38
 */

@Data
public class MyMessage {
    /**
     * uuid
     */
    private String uuid;

    /**
     * 消息体
     */
    private String msg;
}
