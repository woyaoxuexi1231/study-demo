package org.hulei.basic.pattern.structural.decorator;

import org.hulei.basic.pattern.structural.Adapter;
import org.hulei.basic.pattern.structural.Power;
import org.hulei.basic.pattern.structural.Power20;
import org.hulei.basic.pattern.structural.Power5;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural.decorator
 * @className: AdapterCutter
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 21:21
 */

public class AdapterCutter extends AdapterDecorator {

    Power power;

    public AdapterCutter(Adapter adapter) {

        // 降压操作
        int voltage = adapter.getPower().getVoltage();
        if (voltage == 5) {
            this.power = adapter.getPower();
        } else if (voltage == 20) {
            this.power = new Power5(adapter.getPower().getElectron());
        } else {
            this.power = new Power20(adapter.getPower().getElectron());
        }
    }

    @Override
    public Power getPower() {
        return power;
    }
}
