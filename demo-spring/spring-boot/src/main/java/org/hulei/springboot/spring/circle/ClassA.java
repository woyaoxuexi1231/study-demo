package org.hulei.springboot.spring.circle;

import org.hulei.common.autoconfigure.annotation.DoneTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassA {

    private ClassB classB;

    @Autowired
    public void setClassB(ClassB classB) {
        this.classB = classB;
    }

    /**
     * 这里添加了两个注解,旨在使这个类最终生成代理对象
     * 在源码中, AbstractAutowireCapableBeanFactory进行doCreateBean方法内, 在 initializeBean 阶段, 会通过 AnnotationAwareAspectJAutoProxyCreator这个具体实现类生成代理对象
     * 如果需要提前暴露,那么会通过getEarlyBeanReference这个方法生成代理对象,如果不需要提前生成,那么在postProcessBeforeInstantiation就会生成代理对象了
     */
    @DoneTime
    public void methodA() {
        System.out.println(classB);
    }
}