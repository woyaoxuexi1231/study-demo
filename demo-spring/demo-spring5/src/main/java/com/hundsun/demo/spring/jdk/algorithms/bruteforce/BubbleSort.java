package com.hundsun.demo.spring.jdk.algorithms.bruteforce;

/**
 * 冒泡排序
 * 排序的主要思路就是像水滴冒泡一样，越大的数据就会越往后，每一次循环都执行这个操作
 */

public class BubbleSort {
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // 如果当前元素比下一个元素大，则交换它们
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("原始数组:");
        printArray(arr);

        bubbleSort(arr);
        System.out.println("排序后的数组:");
        printArray(arr);
    }

    // 打印数组
    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
