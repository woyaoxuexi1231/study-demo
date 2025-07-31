package org.hulei.spring.xml.init.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.listener
 * @className: SimpleListener
 * @description:
 * @author: h1123
 * @createDate: 2022/11/20 19:27
 */

@Slf4j
public class SimpleListener implements ApplicationListener<SimpleEvent> {

    /*
    ApplicationContext事件机制是观察者设计模式的实现, 通过 ApplicationEvent 类和 ApplicationListener 接口, 可以实现 ApplicationContext 事件处理
    如果容器中有一个 ApplicationListener Bean, 每当 ApplicationContext 发布 ApplicationEvent 时, ApplicationListener Bean将自动被触发, 这种事件机制都必须需要程序显示的触发
    其中spring有一些内置的事件, 当完成某种操作时会发出某些事件动作, 比如监听ContextRefreshedEvent事件, 当所有的bean都初始化完成并被成功装载后会触发该事件, 实现ApplicationListener<ContextRefreshedEvent>接口可以收到监听动作, 然后可以写自己的逻辑

    这里实现一个简单的事件监听
     */
    @Override
    public void onApplicationEvent(SimpleEvent event) {
        log.info("收到一个spring的监听消息: {}", event.getSource());
    }
}
