package com.hundsun.demo.java.pattern.create.builder;

import com.hundsun.demo.java.pattern.create.Car;
import lombok.Data;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.create.builder
 * @className: Builder
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 18:20
 */
@Data
public abstract class CarBuilder {

    // 这是一个钩子
    protected boolean isBuildWheel = true;

    /**
     * 需要构建的对象
     */
    protected Car car;

    /**
     * 构建 Car对象的 wheel
     */
    public abstract void buildWheel();

    /**
     * 构建 Car对象的 engine
     */
    public abstract void buildEngine();

}
