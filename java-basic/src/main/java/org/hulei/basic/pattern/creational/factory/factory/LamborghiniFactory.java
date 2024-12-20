package org.hulei.basic.pattern.creational.factory.factory;

import org.hulei.basic.pattern.creational.Car;
import org.hulei.basic.pattern.creational.Lamborghini;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.factory.factory
 * @className: LamborghiniFactory
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 17:47
 */

public class LamborghiniFactory {

    /**
     * 返回特定的 Car对象
     *
     * @return car
     */
    public static Car getCar() {
        return new Lamborghini();
    }
}
