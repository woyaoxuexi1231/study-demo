package com.hundsun.demo.java.sort;

import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: demo
 * @Package: com.hl.java.sort
 * @Description:
 * @Author: hulei42031
 * @Date: 2022/02/22 9:59
 * @Version: 1.0
 * <p>
 * Copyright © 2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class Sort {

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
