package com.hundsun.demo.spring.jdk.pattern.structural;


import com.hundsun.demo.spring.jdk.pattern.structural.adapter.Phone;
import com.hundsun.demo.spring.jdk.pattern.structural.adapter.PhoneAdapter;
import com.hundsun.demo.spring.jdk.pattern.structural.decorator.AdapterCutter;
import com.hundsun.demo.spring.jdk.pattern.structural.decorator.AdapterEnhancer;

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

    装饰器模式和代理模式是两种设计模式，它们有一些共同的特点，但也有一些根本上的区别。

    装饰器模式（Decorator Pattern）允许在不改变原始对象接口的情况下，动态地给对象添加新的行为。
    装饰器模式通过创建一个包装对象，将原始对象包装在其中，并在保持原始对象接口的前提下，提供额外的功能。
    通过嵌套多个装饰器，可以为对象添加多个不同的功能。装饰器模式通常用于扩展一个类的功能，而不是对类进行修改。

    代理模式（Proxy Pattern）提供了对另一个对象的访问控制。
    代理模式通过使用一个代理对象，控制对真实对象的访问，并在真实对象执行前后加入额外的逻辑。
    代理模式可以用于实现延迟加载、缓存、权限控制等功能。代理模式通过在客户端和被代理对象之间引入一个中间层，实现了对被代理对象的间接访问。

    因此，装饰器模式注重在不改变接口的情况下对对象进行功能的扩展，而代理模式注重对对象的控制和间接访问。装饰器模式主要用于动态地为对象添加功能，而代理模式则用于控制访问的权限和逻辑。
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
