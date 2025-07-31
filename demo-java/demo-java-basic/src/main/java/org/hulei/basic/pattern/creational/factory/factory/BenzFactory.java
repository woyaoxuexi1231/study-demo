package org.hulei.basic.pattern.creational.factory.factory;

import org.hulei.basic.pattern.creational.Benz;
import org.hulei.basic.pattern.creational.Car;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.factory.factory
 * @className: BenzFactory
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 17:47
 */

public class BenzFactory {

    /**
     * 返回特定的 Car对象
     *
     * @return car
     */
    public static Car getCar() {
        return new Benz();
    }
}
