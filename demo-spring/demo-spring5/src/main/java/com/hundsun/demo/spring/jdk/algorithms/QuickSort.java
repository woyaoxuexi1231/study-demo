package com.hundsun.demo.spring.jdk.algorithms;

import java.util.Arrays;
import java.util.Random;

/**
 * 快速排序
 *
 * @author hulei42031
 * @since 2023-12-04 21:08
 */

public class QuickSort {

    public static void main(String[] args) {
        int[] ints = new int[]{362, 872591, 9534336, 181, 453873};
        new QuickSort().randomizedQuicksort(ints, 0, ints.length - 1);
        System.out.println(Arrays.toString(ints));
        System.out.println(Arrays.toString(Arrays.copyOfRange(ints, 1, 2)));
    }

    public void randomizedQuicksort(int[] nums, int l, int r) {
        // 如果说给定的位置已经是l>=r了,那么说明已经有序了
        if (l < r) {
            // 分割数组, 得到中间指针位置
            int pos = randomizedPartition(nums, l, r);
            // 前半部分继续二分
            randomizedQuicksort(nums, l, pos - 1);
            // 后半部分继续二分
            randomizedQuicksort(nums, pos + 1, r);
        }
    }

    public int randomizedPartition(int[] nums, int l, int r) {
        // 1. 这里随机生成一个当前给定的数组范围内的下标.
        int i = new Random().nextInt(r - l + 1) + l;
        // 2. 把我们选定的主元素和最后一个元素调换位置, 这个不一定是和最后一个元素换位置, 可以是其他操作
        swap(nums, i, r);
        // 3. 分割这个数组
        return partition(nums, l, r);
    }

    /**
     * 分割这个数组, 起始位置是l,终止位置是r
     *
     * @param nums 原始数组
     * @param l    起始位置
     * @param r    终止位置
     * @return 分割后的数组
     */
    public int partition(int[] nums, int l, int r) {
        // 主元素
        int pivot = nums[r];
        // 待交换元素的指针
        int i = l - 1;
        // 循环将从给定的起始位置开始, 到终止位置结束(不包括主元素)
        for (int j = l; j <= r - 1; ++j) {
            // 遇到比主元素小的, 放到整个数组的前方
            if (nums[j] <= pivot) {
                /*
                1. 第一个数字就比主元素小, 那其实这里位置并没有发生变化
                2. 如果一个元素它没有进入这个判断, 那么代表这个元素比主元素大, 并且它代表的位置是在后续找到比主元素小的元素时进行调换的标记
                这里的 i 其实是作为下一次替换位置的指针
                 */
                i = i + 1;
                // 交换
                swap(nums, i, j);
            }
        }
        // 把主元素和这个待交换元素指针位置的元素交换位置(这个元素必定比主元素大)
        swap(nums, i + 1, r);
        // 返回指针位置
        return i + 1;
    }

    /**
     * 交换两个元素的位置
     *
     * @param nums 原始数组
     * @param i    第i个元素
     * @param j    第j个元素
     */
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
