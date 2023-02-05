package com.hundsun.demo.java.pattern.structural.bridge;

import com.hundsun.demo.java.pattern.structural.Adapter;
import com.hundsun.demo.java.pattern.structural.Power;
import com.hundsun.demo.java.pattern.structural.Power20;
import com.hundsun.demo.java.pattern.structural.Power220;
import com.hundsun.demo.java.pattern.structural.PowerStrip;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural.adapter
 * @className: LaptopChargeAdapter
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 20:01
 */

public class LaptopAdapter implements Adapter {

    private final PowerStrip powerStrip = PowerStrip.getInstance();

    @Override
    public Power getPower() {

        // 这里把 220V 转为 20V
        Power220 power220 = powerStrip.offerPower();

        return new Power20(power220.getElectron());
    }
}
