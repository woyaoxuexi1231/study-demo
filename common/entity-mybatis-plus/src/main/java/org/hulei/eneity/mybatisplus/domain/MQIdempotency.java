package org.hulei.eneity.mybatisplus.domain;

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
@TableName(value = "rabbitmq_test")
public class MQIdempotency {

    /**
     * uuid
     */
    private String uuid;

    /**
     * 消息体
     */
    private String msg;

    /**
     * 消息发生的时间,
     * 这个主要用于示例里用mysql来保证消息幂等性来使用, 作为消息是否消费过期的依据
     */
    private String time;

    /**
     * 消费状态
     * 这个主要用于示例里用mysql来保证消息幂等性来使用, 作为消息是否消费过期的依据
     */
    private String status;
}
