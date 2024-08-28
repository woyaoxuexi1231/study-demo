package org.hulei.demo.spring.circle;

import com.hundsun.demo.commom.core.annotation.DoneTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2024/8/28 10:40
 */

@EnableAsync
@Slf4j
@Component
public class ClassA {

    ClassA classA;

    @Autowired
    public void setClassA(ClassA classA) {
        this.classA = classA;
    }

    @Async
    // @DoneTime
    public void test(){

    }
}
