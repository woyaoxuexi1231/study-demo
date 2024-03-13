package com.hundsun.demo.spring.jdk.algorithms;

import java.util.Arrays;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.jdk.algorithms
 * @Description: 选择排序
 * @Author: hulei42031
 * @Date: 2023-12-01 15:45
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class SelectionSort {

    public static void main(String[] args) {
        int[] ints = new int[]{962162281, 3, 623, 677362, 567875, 73169, 187732727, -1835015738, -268303642, 876606851};
        System.out.println(Arrays.toString(selectionSort(ints)));
    }

    public static int[] selectionSort(int[] ints) {
        for (int i = 0; i < ints.length - 1; i++) {
            int minIndex = i;
            int minValue = ints[i];
            for (int j = i + 1; j < ints.length; j++) {
                if (ints[j] < minValue) {
                    minIndex = j;
                    minValue = ints[j];
                }
            }
            int temp = ints[i];
            ints[i] = ints[minIndex];
            ints[minIndex] = temp;
        }
        return ints;
    }
}
