package com.hundsun.demo.java.jdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.collection
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-30 15:47
 */

public class Collection {

    /**
     * 非线程安全
     * <p>
     * 每次扩容到原来的 1.5 倍
     * <p>
     * 扩容后通过 Arrays.copyOf 获得新的数组
     * <p>
     * 删除后通过 System.arraycopy 来移动元素位置, 再把最后一个元素置为 null
     */
    private ArrayList<?> arrayList;

    /**
     * 非线程安全
     * <p>
     * 不扩容, 内部是一个双向链表, 每次添加的元素在队伍末尾
     * <p>
     * 删除操作比较方便, 只需要改变节点的连接就行, 但是需要遍历查找指定元素
     */
    private LinkedList<?> linkedList;

    /**
     * 线程安全的 ArrayList
     * <p>
     * 新增和删除都只是加了一个 ReentrantLock
     * <p>
     * 写时复制 - 新增时直接新建一个数组, 不操作原数组, 最后把新元素加入队伍末尾
     */
    private CopyOnWriteArrayList<?> copyOnWriteArrayList;

    /**
     * 非线程安全
     * <p>
     * 查找和新增都是通过元素的 hash 值来计算下标
     * <p>
     * 默认初始大小为 1<<4, 扩容因子为 0.75, 每次扩容变大一倍, 且所有元素重新洗牌
     * <p>
     * Node[]下标处的链表超过长度 8 时, 链表变为红黑树
     * <p>
     * <p>
     * 有哪些线程安全问题?
     * <p>
     * 1. JDK7存在扩容死循环风险
     * JDK7扩容时, 会对源Map中的链表进行全部打散, 然后进行头插法, 这里会导致出现环形链表, 从而在后续访问元素出现死循环.
     * 这个BUG在JDK8被修复, JDK采用的方法是, 直接把链表分成两个, 然后再不改变原顺序的前提下, 直接把两个链表插入Map里
     * <p>
     * 2. put元素时, 多线程会导致元素丢失
     * 两个线程同时插入一个经过hash计算后存放在同一个下标的位置, 且该位置的元素为 null, 由于判空操作和插入操作非原子性操作而导致的覆盖行文
     * <p>
     * 3.在扩容的同时, 去get元素, 会导致获取到 null
     */
    private HashMap<?, ?> hashMap;

    /**
     * 非线程安全
     * <p>
     * 基于 HashMap 实现的顺序的 Map
     * <p>
     * 内部的 Entry 继承自 HashMap 的 Node 结点, 结点是一个双向链表结点
     * <p>
     * 在 LinkedHashMap 内部保存着 head结点和 tail结点, 每次新增的时候会把新加的结点进行链接
     */
    private LinkedHashMap<?, ?> linkedHashMap;

    /**
     * 非线程安全
     * <p>
     * 基于红黑树实现, 可以实现自定义排序
     */
    private TreeMap<?, ?> treeMap;

    /**
     * Set系列全部底层全部复用的Map
     */
    private Set<?> set;
}
