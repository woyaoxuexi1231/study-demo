package org.hulei.jdk.suanfa;

import java.util.Arrays;

public class MinimumCoinsChange {

    public static int minCoins(int[] coins, int amount) {

        // 创建一个数组来保存每个金额所需的最小硬币数量
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, Integer.MAX_VALUE); // 初始化数组，初始值设为最大值
        dp[0] = 0; // 目标金额为0时，所需硬币数量为0

        // 遍历计算每个金额的最小硬币数量
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i - coin >= 0 && dp[i - coin] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }

        // 返回目标金额的最小硬币数量，如果无法凑成目标金额则返回-1
        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }

    public static void main(String[] args) {
        int[] coins = {1, 5, 10, 25};
        int amount = 32;

        int minCoins = minCoins(coins, amount);
        if (minCoins != -1) {
            System.out.println("最小找零硬币数量为: " + minCoins);
        } else {
            System.out.println("无法凑成目标金额");
        }
    }
}
