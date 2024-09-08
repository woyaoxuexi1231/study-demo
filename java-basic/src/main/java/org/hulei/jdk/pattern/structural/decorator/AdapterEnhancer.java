package org.hulei.jdk.pattern.structural.decorator;

import org.hulei.jdk.pattern.structural.Adapter;
import org.hulei.jdk.pattern.structural.Power;
import org.hulei.jdk.pattern.structural.Power20;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural.decorator
 * @className: AdpterEnhancer
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 21:17
 */

public class AdapterEnhancer extends AdapterDecorator {

    Power power;

    public AdapterEnhancer(Adapter adapter) {

        // 升压操作
        int voltage = adapter.getPower().getVoltage();
        if (voltage == 5) {
            this.power = new Power20(adapter.getPower().getElectron());
        } else if (voltage > 20) {
            this.power = new Power20(adapter.getPower().getElectron());
        } else {
            this.power = adapter.getPower();
        }
    }

    @Override
    public Power getPower() {
        return power;
    }
}
