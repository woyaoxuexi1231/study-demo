package com.hundsun.demo.java.pattern.structural.adapter;

import com.hundsun.demo.java.pattern.structural.Adapter;
import com.hundsun.demo.java.pattern.structural.Equipment;
import com.hundsun.demo.java.pattern.structural.Power;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural.adapter
 * @className: Phone
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 19:45
 */

public class Phone extends Equipment {

    public void getCharge(Adapter adapter) {
        // 通过 adapter 获得电量
        Power power = adapter.getPower();
        if (power.getVoltage() > 20) {
            System.out.println("charge is not supported.");
        } else {
            electricity += power.getElectron() * power.getVoltage() * 2;
            System.out.println("electricity : " + electricity);
        }
    }
}
