package com.hundsun.demo.java.juc;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.juc
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-12-26 14:28
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class CopyOnWriteArrayListTest {

    /**
     * CopyOnWriteArrayList 使用写时复制, 更新操作不直接修改原数组, 而是操作副本再复制到新数组进行操作
     * 这导致弱一致性, 迭代器和读取元素时都可能不能拿到第一手数据, 因为读取的是原数组
     */
}
