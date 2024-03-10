package com.hundsun.demo.commom.core.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.commom.core.model
 * @className: RabbitmqLogDO
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 23:58
 */

@Data
@Table(name = "rabbitmq_test")
public class RabbitmqLogDO {
    /**
     * uuid
     */
    @Id
    private String uuid;

    /**
     * 消息体
     */
    private String msg;

    /**
     * 消息发生的时间
     */
    private String time;
}
