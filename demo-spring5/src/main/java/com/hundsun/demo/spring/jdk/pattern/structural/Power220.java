package com.hundsun.demo.spring.jdk.pattern.structural;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural
 * @className: Power220
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 19:37
 */

public class Power220 extends Power {

    public Power220(int voltage) {
        super(220);
        this.setElectron(voltage);
    }
}
