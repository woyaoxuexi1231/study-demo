package org.hulei.jdk.algorithms;

/**
 * 二分查找,非常经典的查找算法
 * 每次查找都以中间的数为基准来进行对比
 * 算法的时间复杂度为 取以2为底n的对数 log2n(每次找到都可以排除当前数量的一半)
 *
 * @author hulei42031
 * @since 2023-11-30 16:15
 */

public class BinarySearch {

    public static void main(String[] args) {
        int[] ints = new int[]{1, 3, 4, 6, 8, 11, 15, 67, 89, 101};
        int num = 4;
        // int index = binarySearch(ints, num);
        // 二分查找的时间复杂度为 logn, 取以2为底n的对数则是二分查找的时间复杂度
        int index = doBinarySearch(ints, num);
        System.out.println(index);
    }

    public static int binarySearch(int[] ints, int num) {
        // 开始下标和结束下标
        int start = 0;
        int end = ints.length - 1;
        return doBinarySearch(ints, start, end, num);
    }

    /**
     * 递归, 但是递归不太好
     * author: hulei42031
     * date: 2023-11-30 16:43
     *
     * @param ints  数组
     * @param start 开始下标
     * @param end   结束下标
     * @param num   目标值
     * @return 对应下标
     */
    public static int doBinarySearch(int[] ints, int start, int end, int num) {
        if (start > end) {
            return -1;
        }
        // 中间值到底如何计算呢???
        int mid = (end + start) / 2;
        // 判断中间数字是否与指定数字相等
        if (ints[mid] == num) {
            return mid;
        }
        // 不相等则继续二分
        // 前半部分已不符合条件
        if (ints[mid] < num) {
            return doBinarySearch(ints, mid + 1, end, num);
        }
        // 后半部分已不符合条件
        if (ints[mid] > num) {
            return doBinarySearch(ints, start, mid - 1, num);
        }
        return -1;
    }

    /**
     * 循环
     * author: hulei42031
     * date: 2023-11-30 17:00
     *
     * @param ints 数组
     * @param num  目标数字
     * @return 下标
     */
    public static int doBinarySearch(int[] ints, int num) {
        // 开始下标和结束下标
        int start = 0;
        int end = ints.length - 1;
        do {
            int mid = (start + end) / 2;
            if (ints[mid] == num) {
                return mid;
            }
            if (ints[mid] < num) {
                start = mid + 1;
            }
            if (ints[mid] > num) {
                end = mid - 1;
            }
        } while (start <= end);
        return -1;
    }
}
