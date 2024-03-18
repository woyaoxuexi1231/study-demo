package com.hundsun.demo.spring.jdk.algorithms;

import java.util.Arrays;

/**
 * 选择排序
 * 在给定的数组中,每次选择一个最小的元素放到队列的队首
 *
 * @author hulei42031
 * @since 2023-12-01 15:45
 */

public class SelectionSort {

    public static void main(String[] args) {
        int[] ints = new int[]{962162281, 3, 623, 677362, 567875, 73169, 187732727, -1835015738, -268303642, 876606851};
        System.out.println(Arrays.toString(selectionSort(ints)));
    }

    public static int[] selectionSort(int[] ints) {
        for (int i = 0; i < ints.length - 1; i++) {

            // 选择当前循环的第一个元素作为基准值
            int minIndex = i;
            int minValue = ints[i];

            // 循环当前元素后面的所有元素,只要找到比他小的,那么就把基准元素换成那个小的元素
            for (int j = i + 1; j < ints.length; j++) {
                if (ints[j] < minValue) {
                    minIndex = j;
                    minValue = ints[j];
                }
            }

            // 最后把找到的最小的元素和当前的第i个元素交换位置
            int temp = ints[i];
            ints[i] = ints[minIndex];
            ints[minIndex] = temp;
        }
        return ints;
    }
}
