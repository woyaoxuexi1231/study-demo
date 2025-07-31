package org.hulei.spring.xml.init.listener;

import org.springframework.context.ApplicationEvent;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.event
 * @className: SimpleEvent
 * @description: 一个简单的事件
 * @author: h1123
 * @createDate: 2022/11/20 19:26
 */

public class SimpleEvent extends ApplicationEvent {

    public SimpleEvent(String eventName) {
        super(eventName);
    }
}
