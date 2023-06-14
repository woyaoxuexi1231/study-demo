package com.hundsun.demo.java.jdk.pattern.structural;

import lombok.Data;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural.proxy
 * @className: Power
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 19:38
 */
@Data
public abstract class Power {

    /**
     * 电压
     */
    protected final int voltage;

    /**
     * 电量
     */
    private int electron;

    public Power(int voltage) {
        this.voltage = voltage;
    }
}
