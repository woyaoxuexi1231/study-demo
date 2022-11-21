package com.hundsun.demo.spring.event;

import org.springframework.context.ApplicationEvent;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.event
 * @className: SimpleEvent
 * @description:
 * @author: h1123
 * @createDate: 2022/11/20 19:26
 * @updateUser: h1123
 * @updateDate: 2022/11/20 19:26
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class SimpleEvent extends ApplicationEvent {

    public SimpleEvent(Object source) {
        super(source);
    }
}
