package com.hundsun.demo.spring.jdk.algorithms;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.jdk.algorithms
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-12-04 20:49
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class RecursionTest {


    public static void main(String[] args) {
        int[] ints = new int[]{9355487, 8510115, 8};
        // System.out.println(sum(ints, 0));
        System.out.println(max(ints, 0));
    }

    public static int sum(int[] ints, int current) {
        if (current == ints.length - 1) {
            return ints[current];
        }
        return ints[current] + sum(ints, ++current);
    }

    public static int max(int[] ints, int current) {
        if (current == ints.length - 1) {
            return ints[current];
        }
        return Math.max(ints[current], max(ints, ++current));
    }
}
