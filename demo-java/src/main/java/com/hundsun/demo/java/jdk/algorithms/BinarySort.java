package com.hundsun.demo.java.jdk.algorithms;

import java.util.Arrays;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.jdk.algorithms
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-12-04 21:08
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class BinarySort {

    public static void main(String[] args) {
        int[] ints = new int[]{188, 872591, 9534336, 362, 453873};
        System.out.println(Arrays.toString(doBinarySort(ints)));
        System.out.println(Arrays.toString(Arrays.copyOfRange(ints, 1, 2)));
    }

    public static int[] doBinarySort(int[] ints) {
        // 基线条件
        if (ints.length <= 1) {
            return ints;
        }
        // 设定一个基准值
        int midIndex = 0;
        int midValue = ints[0];
        // 这个基准值将作为整个数组的中间值, 左边所有的值都比他小, 右边所有的值都比他大
        // 这里采用的策略是, 依次比较这些数字与基准值的大小, 如果比他大, 位置不变, 如果比他小, 那么与基准值调换位置(使用此策略的前提是我以第一个值为基准值)
        // 结束的条件为完整遍历一次整个数组
        for (int i = 1; i < ints.length; i++) {
            if (ints[i] < midValue) {
                ints[midIndex] = ints[i];
                ints[i] = midValue;
                midValue = ints[i];
                midIndex = i;
            }
        }

        return ints;
    }
}
