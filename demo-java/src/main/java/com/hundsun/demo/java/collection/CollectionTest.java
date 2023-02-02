package com.hundsun.demo.java.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.collection
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-30 15:47
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class CollectionTest {

    /**
     * 非线程安全
     * 每次扩容到原来的 1.5 倍
     * 扩容后通过 Arrays.copyOf 获得新的数组
     * 删除后通过 System.arraycopy 来移动元素位置, 再把最后一个元素置为 null
     */
    private ArrayList<?> arrayList;

    /**
     * 非线程安全
     * 不扩容, 内部是一个双向链表, 每次添加的元素在队伍末尾
     * 删除操作比较方便, 只需要改变节点的连接就行, 但是需要遍历查找指定元素
     */
    private LinkedList<?> linkedList;

    /**
     * 非线程安全
     * 默认初始大小为 1<<4, 扩容因子为 0.75, 每次扩容变大一倍, 且所有元素重新洗牌
     * Node[]下标处的链表超过长度8时, 链表变为红黑树
     */
    private HashMap<?,?> hashMap;
}
