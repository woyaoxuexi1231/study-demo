package com.hundsun.demo.java.algorithms;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: demo
 * @Package: com.hl.java.algorithms
 * @Description:
 * @Author: hulei42031
 * @Date: 2022/02/22 10:06
 * @Version: 1.0
 * <p>
 * Copyright © 2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Slf4j
public class DynamicProgramming {

    public static void main(String[] args) {
        // log.info("?");
        // System.out.println(min2(6, new int[]{1, 3, 4}));
        // System.out.println(fib2(8));
        System.out.println(coinRow2(new int[]{5, 1, 2, 10, 6, 2}));
    }

    /**
     * 斐波那契数 - 递归
     *
     * @param n 第 n 个数, 从 0 开始
     * @return 斐波那契数
     */
    public static int fib(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }

    /**
     * 斐波那契数 - 数组
     *
     * @param n 第 n 个数, 从 0 开始
     * @return 斐波那契数
     */
    public static int fib2(int n) {
        int f0 = 0;
        if (n == 0) {
            return f0;
        }
        int f1 = 1;
        if (n == 1) {
            return f1;
        }
        int fn = 0;
        for (int i = 2; i <= n; i++) {
            fn = f0 + f1;
            f0 = f1;
            f1 = fn;
        }

        return fn;
    }

    /**
     * 币值最大化问题 - 递归
     *
     * @param n 给定硬币
     * @return 最大的币值
     */
    public static int coinRow(int[] n) {
        if (n.length - 1 == 0) {
            return n[0];
        }
        if (n.length - 2 == 0) {
            return Math.max(n[0], n[1]);
        }

        // 要么拿最后一个要么不拿最后一个
        return Math.max((n[n.length - 1] + coinRow(Arrays.copyOf(n, n.length - 2))), coinRow(Arrays.copyOf(n, n.length - 1)));
    }

    /**
     * 币值最大化问题 - 递归
     *
     * @param n 给定硬币
     * @return 最大的币值
     */
    public static int coinRow2(int[] n) {
        int[] fn = new int[n.length + 1];
        fn[0] = 0;
        fn[1] = n[0];
        if (n.length - 1 == 0) {
            return fn[1];
        }
        for (int i = 2; i <= n.length; i++) {
            fn[i] = Math.max(n[i - 1] + fn[i - 2], fn[i - 1]);
        }
        return fn[n.length];
    }

    /**
     * 最小找零问题 - 递归方式
     *
     * @param n 需要找零的钱
     * @param m 对应的零钱数组
     * @return 最小的找零数目
     */
    public static int min(int n, int[] m) {

        if (n == 0) {
            System.out.println("操作+1");
            return 0;
        }

        int fm = n;

        for (int j : m) {

            if (n - j < 0) {
                continue;
            }
            System.out.println("操作+1");
            fm = Math.min(min(n - j, m) + 1, fm);
        }
        return fm;
    }

    /**
     * 最小找零问题 - 数组+循环
     *
     * @param n 需要找零的钱
     * @param m 对应的零钱数组
     * @return 最小的找零数目
     */
    public static int min2(int n, int[] m) {

        // 创建一个大小为 n 的数组, 这个数组的第 i 个元素就是为 i 元找零最小的数量
        int[] fn = new int[n + 1];
        fn[0] = 0;
        // 最后返回的是 fn[n]

        for (int i = 1; i <= n; i++) {
            fn[i] = n;
            for (int j = 0; j < m.length; j++) {
                // 找不出, 零钱比需要找零的钱还大
                if (m[j] > i) {
                    continue;
                }
                // 还有得找
                // f(i) = f(i-mj) + 1
                fn[i] = Math.min(fn[i - m[j]], fn[i]);
            }
            fn[i] += 1;
        }
        return fn[n];
    }
}
