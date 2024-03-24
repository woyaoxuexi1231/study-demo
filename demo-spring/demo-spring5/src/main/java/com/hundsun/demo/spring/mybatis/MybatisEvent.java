package com.hundsun.demo.spring.mybatis;

import com.hundsun.demo.spring.db.dynamicdb.DynamicDataSourceType;
import org.springframework.context.ApplicationEvent;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.init.listener
 * @className: MybatisEvent
 * @description:
 * @author: h1123
 * @createDate: 2023/2/18 19:45
 */

public class MybatisEvent extends ApplicationEvent {

    private final MyBatisOperationType myBatisOperationType;

    private final DynamicDataSourceType DATA_SOURCE_TYPE;

    public MybatisEvent(MyBatisOperationType myBatisOperationType, DynamicDataSourceType source) {
        super(myBatisOperationType);
        this.myBatisOperationType = myBatisOperationType;
        this.DATA_SOURCE_TYPE = source;
    }

    public MyBatisOperationType getMyBatisOperationType() {
        return myBatisOperationType;
    }

    public DynamicDataSourceType getDataSourceType() {
        return DATA_SOURCE_TYPE;
    }
}
