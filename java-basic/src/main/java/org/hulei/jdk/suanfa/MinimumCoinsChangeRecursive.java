package org.hulei.jdk.suanfa;

import java.util.Arrays;

public class MinimumCoinsChangeRecursive {
    public static int minCoins(int[] coins, int amount) {
        // 创建一个数组用于保存每个金额所需的最小硬币数量
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, -1); // 初始化数组，初始值设为-1
        dp[0] = 0; // 目标金额为0时，所需硬币数量为0

        return minCoinsRecursive(coins, amount, dp);
    }

    public static int minCoinsRecursive(int[] coins, int amount, int[] dp) {
        // 如果已经计算过当前金额的最小硬币数量，则直接返回结果
        if (dp[amount] != -1) {
            return dp[amount];
        }

        // 如果目标金额为0，则所需硬币数量为0
        if (amount == 0) {
            return 0;
        }

        int min = Integer.MAX_VALUE;

        // 遍历每种面额的硬币，计算凑成目标金额所需的最小硬币数量
        for (int coin : coins) {
            if (amount - coin >= 0) {
                int currentMin = minCoinsRecursive(coins, amount - coin, dp);
                if (currentMin != -1) {
                    min = Math.min(min, currentMin + 1);
                }
            }
        }

        // 如果无法凑成目标金额，则返回-1
        dp[amount] = (min == Integer.MAX_VALUE) ? -1 : min;
        return dp[amount];
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
