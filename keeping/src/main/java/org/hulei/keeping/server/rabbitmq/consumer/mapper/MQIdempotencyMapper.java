package org.hulei.keeping.server.rabbitmq.consumer.mapper;

import com.hundsun.demo.commom.core.model.MQIdempotency;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.consumer.mapper
 * @className: MQIdempotencyMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/3/18 22:51
 */

public interface MQIdempotencyMapper extends BaseMapper<MQIdempotency> {

    /**
     * 根据过期时间和消息ID来判断消息是否超时
     *
     * @param validation
     * @return
     */
    int deleteByTime(MQIdempotency validation);

    /**
     * 更新消费状态
     *
     * @param validation
     */
    void updateByTime(MQIdempotency validation);
}
