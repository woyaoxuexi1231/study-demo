package com.hundsun.demo.spring.db.dynamicdb;

import com.hundsun.demo.spring.db.dynamicdb.DynamicDataSourceType;
import org.springframework.context.ApplicationEvent;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.init.listener
 * @className: MultipleDataSourceTestEvent
 * @description: 一个测试多数据源切换的简单的监听事件
 * @author: h1123
 * @createDate: 2023/2/18 16:41
 */

public class MultipleDataSourceTestEvent extends ApplicationEvent {

    private final DynamicDataSourceType DATA_SOURCE_TYPE;

    public MultipleDataSourceTestEvent(DynamicDataSourceType source) {
        super(source);
        DATA_SOURCE_TYPE = source;
    }

    public DynamicDataSourceType getDataSourceType() {
        return DATA_SOURCE_TYPE;
    }
}
