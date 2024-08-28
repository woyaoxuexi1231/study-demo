package org.hulei.demo.spring.circle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hulei
 * @since 2024/8/28 10:40
 */

@Slf4j
// @Component
public class ClassA1 {

    private ClassA b;

    private ClassA classA;

    /**
     * @param b
     */
    // @Lazy
    @Autowired
    public void setB(ClassA b) {
        // log.info("a 注入了 b: {}", b);
        this.b = b;
        classA = b;
    }

    public ClassA getbClass() {
        return classA;
    }

    public ClassA getB() {
        return b;
    }
}
