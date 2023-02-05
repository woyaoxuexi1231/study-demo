package com.hundsun.demo.java.pattern.structural;

import com.hundsun.demo.java.pattern.structural.adapter.Phone;
import com.hundsun.demo.java.pattern.structural.adapter.PhoneAdapter;
import com.hundsun.demo.java.pattern.structural.decorator.AdapterCutter;
import com.hundsun.demo.java.pattern.structural.decorator.AdapterEnhancer;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural
 * @className: DecoratorTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 21:42
 */

public class DecoratorTest {

    /*
    装饰器模式 - 动态地给一个对象增加一些额外的职责
     */

    public static void main(String[] args) {

        // 这里新增一个 Adapter 的增强器和减弱器来装饰 Adapter
        Equipment phone = new Phone();

        // 这里对适配器进行增强和减弱操作
        PhoneAdapter adapter = new PhoneAdapter();
        AdapterEnhancer adapterEnhancer = new AdapterEnhancer(adapter);
        phone.getCharge(adapterEnhancer);
        AdapterCutter adapterCutter = new AdapterCutter(adapterEnhancer);
        phone.getCharge(adapterCutter);

    }
}
