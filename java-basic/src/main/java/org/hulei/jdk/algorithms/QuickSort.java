package org.hulei.jdk.algorithms;

import java.util.Arrays;
import java.util.Random;

/**
 * 快速排序
 * 对于快排,我之前一直想过一个问题就是,如果数组元素过多是否会导致栈溢出的问题.不过问了一下chatgpt,茅塞顿开,分治法会让递归的深度在logn级别,那么即使是有40亿个元素,栈的深度也不过才32
 *
 * @author hulei42031
 * @since 2023-12-04 21:08
 */

public class QuickSort {

    public static void main(String[] args) {
        int[] ints = new int[]{362, 872591, 181, 9534336, 453873};
        // new QuickSort().randomizedQuicksort(ints, 0, ints.length - 1);
        quickSort(ints, 0, ints.length - 1);
        System.out.println(Arrays.toString(ints));
        // System.out.println(Arrays.toString(Arrays.copyOfRange(ints, 1, 2)));
    }

    public static void randomizedQuicksort(int[] nums, int l, int r) {
        // 首先快速排序他的思想是分治法,把一个大的数组每次以一个基准值给分成两半,左边比这个基准值小,右边比这个基准值大
        // 所以如果当前需要分割的元素个数只有一个的时候(l=r),或者这种让我们分割的要求并不合理的时候(l>r),我们就认为此时给定的范围已经有序了
        if (l < r) {
            // 1.分割数组, 得到中间指针位置
            int pos = randomizedPartition(nums, l, r);
            // 2.前半部分继续刚才的操作
            randomizedQuicksort(nums, l, pos - 1);
            // 3.后半部分继续刚才的操作
            randomizedQuicksort(nums, pos + 1, r);
        }
    }

    public static int randomizedPartition(int[] nums, int l, int r) {
        // 我们这里随机的把范围内的一个数字作为基准元素,并且把这个基准元素放到范围内的最后去
        // 随机选择一个可以使得我们选择数字尽量是一个中间的数字
        swap(nums, new Random().nextInt(r - l + 1) + l, r);
        // 然后把这个数字拿出来,作为下面比较操作的一个基准值
        int pivot = nums[r];

        // 定义一个指针(i),这个指针代表的含义就是在这个指针之前的所有数字都一定比我们的基准元素小(不会包含这个指针指向的元素)
        // 这里初始定义l,那么代表当前并没有任何一个元素比我们的基准元素要小
        int i = l;
        // 下面将进行一个循环.循环将从给定的起始位置开始, 到终止位置结束(不包括主元素)
        for (int j = l; j <= r - 1; ++j) {
            // 这里只做一件事情,就是只要遇到一个元素比基准元素要小,那么这个元素就需要与我们之前定义的指针位置的元素交换位置
            if (nums[j] <= pivot) {
                // 交换
                swap(nums, i, j);
                // 交换之后把指针向右移动一位,以便下一次找到一个比基准元素小的元素时,跟指针指向的元素交换位置
                i++;
            }
            // 如果当前这次的元素并没有比基准元素小,那么也就说明指针指向的元素一定是一个大于等于基准元素的值,那么后续在交换位置的时候,即使是被交换到后面,也不影响大于基准值的元素放在基准元素的右边的原则
        }
        // 当我们整个循环结束之后,我们指针指向的那个元素一定比我们的基准值大(或者是相等)
        // 所以我们把我们的基准值与指针指向的元素交换位置,就可以得到基准值左边的都比基准值小,基准值右边的都比基准值大
        swap(nums, i, r);
        // 然后我们返回指针所在的位置,这个位置就是我们基准值的位置,也就是整个数组以下标i为界限划分
        return i;
    }

    /**
     * 交换两个元素的位置
     *
     * @param nums 原始数组
     * @param i    第i个元素
     * @param j    第j个元素
     */
    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }


    public static void quickSort(int[] arr, int low, int high) {

        // ==========================考虑边界行为===========================
        // 基本判断,这两种情况都是直接返回
        if (arr == null || arr.length == 0) {
            return;
        }
        if (low >= high) {
            return;
        }

        // ============================找到一个分治的依据===========================
        // 这里直接选择给定范围的中间元素作为基准元素,这个地方由于采用双指针,基准元素不再作为最后调换位置的元素.
        int middle = low + (high - low) / 2;
        int pivot = arr[middle];


        // 与上面的方法不同的是,这里使用的是双指针.上面使用的是单指针
        int i = low;
        int j = high;
        // 首先我低位指针一定不能大于高位指针,如果大于了,那么只能说明这个划分已经结束,不用再继续操作了.
        while (i <= j) {
            // 从低位指针开始,直到遇到一个不小于基准值的元素出现,停止.
            // 停止意味着,低位指针遇到了一个比基准元素大的值,他等待一个比基准元素小的值出现来换位置
            while (arr[i] < pivot) {
                i++;
            }
            // 高位指针开始工作,知道遇到一个不大于基准元素出现,停止.
            // 停止意味着,高位指针遇到了一个比基准元素小的值,他等待一个比基准元素大的值出现来换位置
            while (arr[j] > pivot) {
                j--;
            }

            // 如果说此时遇到的这两个元素是正常的一个情况.(要么是两个不同的元素,要么两个指针发生碰撞了,两种情况都交换位置)
            if (i <= j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                // 注意,交换位置之后.那么也就意味着这两个元素的相对位置已经定下来了.
                i++;
                j--;
            } else {
                // 我应该去关注这种边界情况吗? 还是说我其实仅仅只需要关注 i <= j 就行了.因为对于我来说其实只有 i<=j才是一个合法的情况
                System.out.println(111);
            }
        }
        // 这种情况一直会持续到指针发生碰撞位置,指针发生碰撞

        if (low < j) {
            quickSort(arr, low, j);
        }
        if (high > i) {
            quickSort(arr, i, high);
        }
    }

    /**
     * 这是一个非递归的方式去实现一个快速排序，但是依旧需要模拟一个栈
     *
     * @param arr rsp
     */
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
