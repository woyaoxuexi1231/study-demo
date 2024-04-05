package com.hundsun.demo.spring.jdk.algorithms.bruteforce;

public class LinearSearch {
    public static int linearSearch(int[] arr, int target) {
        // 遍历数组，逐个比较元素是否等于目标值
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i; // 如果找到目标值，返回它的索引
            }
        }
        return -1; // 如果未找到目标值，返回 -1
    }

    public static void main(String[] args) {
        int[] arr = {12, 45, 32, 78, 21, 5};
        int target = 32;
        
        int index = linearSearch(arr, target);
        if (index != -1) {
            System.out.println("目标值 " + target + " 在数组中的索引为: " + index);
        } else {
            System.out.println("目标值 " + target + " 不在数组中");
        }
    }
}
