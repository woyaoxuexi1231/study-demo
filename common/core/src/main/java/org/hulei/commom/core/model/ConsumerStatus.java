package org.hulei.commom.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.consumer.listener
 * @className: ConsumerStatus
 * @description:
 * @author: h1123
 * @createDate: 2023/3/19 15:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerStatus {

    /**
     * 是否允许消费
     */
    private boolean isEnableConsumer;

    /**
     * 消息是否已经被消费成功过了
     */
    private boolean isFinished;
}
