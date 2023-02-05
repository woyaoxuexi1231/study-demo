package com.hundsun.demo.java.pattern.structural.adapter;

import com.hundsun.demo.java.pattern.structural.Adapter;
import com.hundsun.demo.java.pattern.structural.Power;
import com.hundsun.demo.java.pattern.structural.Power220;
import com.hundsun.demo.java.pattern.structural.Power5;
import com.hundsun.demo.java.pattern.structural.PowerStrip;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural.adapter
 * @className: PhoneChageAdapter
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 19:43
 */

public class PhoneAdapter implements Adapter {

    private final PowerStrip powerStrip = PowerStrip.getInstance();

    @Override
    public Power getPower() {
        // 这里把 220V 转为 5V
        Power220 power220 = powerStrip.offerPower();

        return new Power5(power220.getElectron());
    }
}
