package com.hundsun.demo.spring.jdk.pattern.structural;

import com.hundsun.demo.spring.jdk.pattern.structural.adapter.Phone;
import com.hundsun.demo.spring.jdk.pattern.structural.adapter.PhoneAdapter;
import com.hundsun.demo.spring.jdk.pattern.structural.bridge.LaptopAdapter;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural
 * @className: AdapterTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 20:02
 */

public class AdapterTest {

    /*
    适配器模式 - 将一个类的接口转换成客户希望的另一个接口, 适配器让那些原本不兼容的类可以一起工作
     */

    public static void main(String[] args) {

        // 这里模拟一个手机充电的场景
        Equipment phone = new Phone();

        // 这里有两种适配器, 一种返回 5V, 一种返回 20V
        phone.getCharge(new LaptopAdapter());
        phone.getCharge(new PhoneAdapter());

    }
}
