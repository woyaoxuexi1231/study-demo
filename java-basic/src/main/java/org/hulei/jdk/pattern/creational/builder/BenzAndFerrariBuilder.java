package org.hulei.jdk.pattern.creational.builder;

import org.hulei.jdk.pattern.creational.Car;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.create.builder
 * @className: BenzAndFerrairBuilder
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 18:26
 */

public class BenzAndFerrariBuilder extends CarBuilder {

    @Override
    public void buildWheel() {
        car.setWheel("Benz's Wheel");
    }

    @Override
    public void buildEngine() {
        car.setEngine("Ferrari's Engine");
    }

    public BenzAndFerrariBuilder() {
        this.car = new Car();
        this.setBuildWheel(false);
    }

    /**
     * 创建一个完整的 Car对象
     *
     * @return car
     */
    public Car createCar() {
        // 通过钩子来判断是否需要构建 wheel
        if (isBuildWheel) {
            buildWheel();
        }
        buildEngine();
        return car;
    }
}
