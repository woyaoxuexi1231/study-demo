package org.hulei.basic.suanfa;

public class CoinMaximization {
    public static int maximizeCoins(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        
        // 使用动态规划，dp[i] 表示在前 i 个房子中偷到的最大金额
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = nums[0];
        
        for (int i = 2; i <= n; i++) {
            // 对于每个房子，有两种选择：偷或者不偷
            // 如果偷当前房子，则最大金额为 dp[i-2] + 当前房子的金额
            // 如果不偷当前房子，则最大金额为 dp[i-1]
            dp[i] = Math.max(dp[i-2] + nums[i-1], dp[i-1]);
        }
        
        return dp[n];
    }

    public static void main(String[] args) {
        int[] nums = {2, 7, 9, 3, 1};
        System.out.println("偷到的最大金额为: " + maximizeCoins(nums)); // 输出: 12 (偷第1、3、5个房子)
    }
}
