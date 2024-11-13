package org.hulei.common.mapper.entity.pojo;

import lombok.Data;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.commom.core.model
 * @className: RabbitmqLogDO
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 23:58
 */

@Data
@Table(name = "rabbitmq_log")
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
