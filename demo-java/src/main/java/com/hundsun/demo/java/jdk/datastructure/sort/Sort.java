package com.hundsun.demo.java.jdk.datastructure.sort;

import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: demo
 * @Package: com.hl.java.sort
 * @Description:
 * @Author: hulei42031
 * @Date: 2022/02/22 9:59
 */

public class Sort {

    /**
     * 快速排序
     * <p>
     * 每一趟排序将数组分割成两个部分, 前面部分的所有元素均小于后面部分的元素, 然后对两个部分的元素重复这个过程直到整个数组有序
     * <p>
     * 空间复杂度 : O(n)
     * <p>
     * 时间复杂度 : O(n(log2)n)
     * <p>
     * 不稳定的排序算法
     *
     * @param nums  源数组
     * @param start 排序开始的下标
     * @param end   排序结束的下标
     */
    public void quickSort(List<Integer> nums, int start, int end) {


        int startFlag = start;
        int endFlag = end;


        if (start < end) {

            int flag = nums.get(start);

            while (start < end) {

                while (start < end && flag <= nums.get(end)) {
                    end--;
                }
                if (start < end) {
                    nums.set(start, nums.get(end));
                    nums.set(end, flag);
                }
                while (start < end && flag > nums.get(start)) {
                    start++;
                }
                if (start < end) {
                    nums.set(end, nums.get(start));
                    nums.set(start, flag);
                }
            }

            quickSort(nums, startFlag, start - 1);
            quickSort(nums, start + 1, endFlag);

        }


    }

    /**
     * 冒泡排序
     * <p>
     * 原理 : 每次两两比较, 较大的元素总是往后沉, 最外层循环每次结束可以得到一个本次循环的最大数字, 并排在最后
     * <p>
     * 空间复杂度 : O(1)
     * <p>
     * 时间复杂度 : O(n^2)
     * <p>
     * 稳定排序算法
     *
     * @param nums 源数组
     */
    public void buSort(List<Integer> nums) {
        for (int i = 0; i < nums.size(); i++) {
            for (int j = i; j < nums.size(); j++) {
                if (nums.get(j) < nums.get(i)) {
                    int temp = nums.get(i);
                    nums.set(i, nums.get(j));
                    nums.set(j, temp);
                }
            }
        }

    }
}
