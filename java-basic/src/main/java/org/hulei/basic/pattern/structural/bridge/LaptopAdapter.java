package org.hulei.basic.pattern.structural.bridge;

import org.hulei.basic.pattern.structural.Adapter;
import org.hulei.basic.pattern.structural.Power;
import org.hulei.basic.pattern.structural.Power20;
import org.hulei.basic.pattern.structural.Power220;
import org.hulei.basic.pattern.structural.PowerStrip;

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
