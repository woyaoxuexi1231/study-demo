package com.hundsun.demo.java.jdk;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.jdk
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-11-30 09:45
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class SwitchCaseTest {

    public static void main(String[] args) {
        SwitchClass switchClass = new SwitchClass();
        // compile failed
        // Incompatible types. Found: 'com.hundsun.demo.java.jdk.SwitchCaseTest.SwitchClass', required: 'char, byte, short, int, Character, Byte, Short, Integer, String, or an enum'
        // 只接受基础类型, case 标签必须是常量表达式，意味着它必须在编译时就能确定，并且不能重复
        // switch (switchClass) {
        //
        // }
    }

    interface SwitchInterface {

    }

    static class SwitchClass implements SwitchInterface {

    }
}
