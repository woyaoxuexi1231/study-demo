package com.hundsun.demo.spring.jdk.algorithms;

public class CoinMaximizationRecursive {
    public static int maximizeCoins(int[] nums) {
        return maximizeCoins(nums, nums.length - 1);
    }

    private static int maximizeCoins(int[] nums, int idx) {
        if (idx < 0) {
            return 0;
        } else if (idx == 0) {
            return nums[0];
        } else if (idx == 1) {
            return Math.max(nums[0], nums[1]);
        } else {
            // 在偷第idx个房子时，偷下一个房子的最大收益为maximizeCoins(nums, idx - 2)
            // 在不偷第idx个房子时，偷下一个房子的最大收益为maximizeCoins(nums, idx - 1)
            return Math.max(maximizeCoins(nums, idx - 2) + nums[idx], maximizeCoins(nums, idx - 1));
        }
    }

    public static void main(String[] args) {
        int[] nums = {2, 7, 9, 3, 1};
        System.out.println("偷到的最大金额为: " + maximizeCoins(nums)); // 输出: 12 (偷第1、3、5个房子)
    }
}
