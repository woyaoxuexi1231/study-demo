package com.hundsun.demo.spring.jdk.algorithms;

import java.util.Arrays;

public class IterativeQuickSort {
    public static void main(String[] args) {
        int[] arr = {7, 2, 1, 6, 8, 5, 3, 4};
        iterativeQuickSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void iterativeQuickSort(int[] arr) {
        int n = arr.length;
        int[] stack = new int[n * 2];  // 模拟栈
        int top = -1;
        int low = 0;
        int high = n - 1;

        stack[++top] = low;
        stack[++top] = high;

        while (top >= 0) {
            high = stack[top--];
            low = stack[top--];

            int pivotIndex = partition(arr, low, high);

            if (pivotIndex - 1 > low) {
                stack[++top] = low;
                stack[++top] = pivotIndex - 1;
            }

            if (pivotIndex + 1 < high) {
                stack[++top] = pivotIndex + 1;
                stack[++top] = high;
            }
        }
    }

    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}
