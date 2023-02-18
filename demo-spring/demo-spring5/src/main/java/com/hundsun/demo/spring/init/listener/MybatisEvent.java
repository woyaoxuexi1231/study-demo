package com.hundsun.demo.spring.init.listener;

import com.hundsun.demo.spring.mybatis.MyBatisOperationType;
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

    public MybatisEvent(MyBatisOperationType myBatisOperationType) {
        super(myBatisOperationType);
        this.myBatisOperationType = myBatisOperationType;
    }

    public MyBatisOperationType getMyBatisOperationType() {
        return myBatisOperationType;
    }
}
