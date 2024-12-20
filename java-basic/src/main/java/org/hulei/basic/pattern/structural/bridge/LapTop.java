package org.hulei.basic.pattern.structural.bridge;


import org.hulei.basic.pattern.structural.Adapter;
import org.hulei.basic.pattern.structural.Equipment;
import org.hulei.basic.pattern.structural.Power;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural.bridge
 * @className: LapTop
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 20:48
 */

public class LapTop extends Equipment {

    public void getCharge(Adapter adapter) {
        // 通过 adapter 获得电量
        Power power = adapter.getPower();
        if (power.getVoltage() > 20) {
            System.out.println("charge is not supported.");
        } else {
            electricity += power.getElectron() * power.getVoltage() * 4;
            System.out.println("electricity : " + electricity);
        }
    }
}
