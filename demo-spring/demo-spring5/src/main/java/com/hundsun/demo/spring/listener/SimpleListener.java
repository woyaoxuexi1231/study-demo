package com.hundsun.demo.spring.listener;

import com.hundsun.demo.spring.event.SimpleEvent;
import org.springframework.context.ApplicationListener;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.listener
 * @className: SimpleListener
 * @description:
 * @author: h1123
 * @createDate: 2022/11/20 19:27
 * @updateUser: h1123
 * @updateDate: 2022/11/20 19:27
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class SimpleListener implements ApplicationListener<SimpleEvent> {

    @Override
    public void onApplicationEvent(SimpleEvent simpleEvent) {
        System.out.println("事件进来咯");
    }
}
