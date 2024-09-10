package org.hulei.keeping.server.spring.circle;

import com.hundsun.demo.commom.core.annotation.DoneTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ClassA {

    // @Autowired
    private ClassB b;
    //
    // @Autowired
    // public void setB(ClassB b) {
    //     this.b = b;
    // }
    //

    /**
     * 注入这个类本身,形成一个循环依赖
     */
    @Autowired
    ClassA classA;

    /**
     * 这里添加了两个注解,旨在使这个类最终生成代理对象
     * 在源码中, AbstractAutowireCapableBeanFactory进行doCreateBean方法内, 在 initializeBean 阶段, 会通过 AnnotationAwareAspectJAutoProxyCreator这个具体实现类生成代理对象
     * 如果需要提前暴露,那么会通过getEarlyBeanReference这个方法生成代理对象,如果不需要提前生成,那么在postProcessBeforeInstantiation就会生成代理对象了
     */
    @DoneTime
    @Transactional
    public void methodA() {
        System.out.println(b);
    }
}