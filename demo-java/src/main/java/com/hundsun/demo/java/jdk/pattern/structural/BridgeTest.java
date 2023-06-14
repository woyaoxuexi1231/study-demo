package com.hundsun.demo.java.jdk.pattern.structural;

import com.hundsun.demo.java.jdk.pattern.structural.bridge.LapTop;
import com.hundsun.demo.java.jdk.pattern.structural.bridge.LaptopAdapter;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.structural
 * @className: BridgeTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 20:53
 */

public class BridgeTest {

    /*
    桥接模式 - 将抽象部分和它的实现部分接口, 使得两者都能独立变化

    又被成为柄体模式或接口模式
     */

    public static void main(String[] args) {

        /*
        这里抽象了一个 Adapter 接口, 和一个 Equipment 抽象类
        Equipment 使用 Adapter 来进行充电
         */
        Equipment laptop = new LapTop();
        laptop.getCharge(new LaptopAdapter());
    }
}
