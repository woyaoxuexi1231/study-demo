package com.hundsun.demo.java.jdk;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.collection
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-30 15:47
 */

@Slf4j
public class Collection {

    /*
    Iterable -
        Iterable接口是Java集合框架中的一个顶级接口，它定义了一种表示可迭代对象（即可以按顺序访问元素的对象）的标准方式。
        实现了Iterable接口的类可以使用增强型的for循环（foreach loop）来遍历其中的元素。
    Iterator（迭代器）
        Java集合框架中的一个接口，它提供了一种遍历集合元素的方式。
        Iterator接口定义了访问和操作集合中元素的方法，通过使用迭代器，可以按顺序逐个访问集合中的元素而不需要了解集合内部的结构。

    Java集合主要由两大接口派生而来 - Collection 和 Map

    Collection
        Set - 这个都是无需的
            HashSet
            LinkedHashSet
            TreeSet
        List - 这个都是有序的
            ArrayList
            LinkedList
            CopyOnWriteArrayList
            Vector
        Queue
            ArrayDeque
            PriorityQueue
     Map
        HashMap
        LinkedHashMap
        Hashtable
        ConcurrentHashMap
     */

    /**
     * 线程安全的集合
     * 相当于ArrayList,只是在所有方法上都加上了synchronized关键字
     * 性能低下, 已有ArrayList这样的替代品而导致不在推荐使用
     */
    @Deprecated
    private Vector<?> vector;

    /**
     * 非线程安全
     * 每次扩容到原来的 1.5 倍
     * 扩容后通过 Arrays.copyOf 获得新的数组
     * 删除后通过 System.arraycopy 来移动元素位置, 再把最后一个元素置为 null
     */
    private ArrayList<?> arrayList;

    /**
     * 非线程安全 - 可当作队列使用, 可以当作栈使用
     * 不扩容, 内部是一个双向链表, 每次添加的元素在队伍末尾
     * 删除操作比较方便, 只需要改变节点的连接就行, 但是需要遍历查找指定元素
     */
    private LinkedList<?> linkedList;

    /**
     * 线程安全的 ArrayList
     * 新增和删除都只是加了一个 ReentrantLock
     * 写时复制(使用ReentrantLock加锁) - 新增时直接新建一个数组, 不操作原数组, 最后把新元素加入队伍末尾
     * 读取时获得的是原数组的值
     */
    private CopyOnWriteArrayList<?> copyOnWriteArrayList;

    /**
     * 非线程安全
     * 查找和新增都是通过元素的 hash 值来计算下标
     * 默认初始大小为 1<<4, 扩容因子为 0.75, 每次扩容变大一倍, 且所有元素重新洗牌
     * Node[]下标处的链表超过长度 8 时, 链表变为红黑树
     * 有哪些线程安全问题?
     * 1. JDK7存在扩容死循环风险
     * JDK7扩容时, 会对源Map中的链表进行全部打散, 然后进行头插法, 这里会导致出现环形链表, 从而在后续访问元素出现死循环.
     * 这个BUG在JDK8被修复, JDK采用的方法是, 直接把链表分成两个, 然后再不改变原顺序的前提下, 直接把两个链表插入Map里
     * 2. put元素时, 多线程会导致元素丢失
     * 两个线程同时插入一个经过hash计算后存放在同一个下标的位置, 且该位置的元素为 null, 由于判空操作和插入操作非原子性操作而导致的覆盖行文
     * 3.在扩容的同时, 去get元素, 会导致获取到 null
     */
    private HashMap<?, ?> hashMap;

    /**
     * 非线程安全
     * 基于 HashMap 实现的顺序的 Map
     * 内部的 Entry 继承自 HashMap 的 Node 结点, 结点是一个双向链表结点
     * 在 LinkedHashMap 内部保存着 head结点和 tail结点, 每次新增的时候会把新加的结点进行链接
     */
    private LinkedHashMap<?, ?> linkedHashMap;

    /**
     * 非线程安全
     * 基于红黑树实现, 可以实现自定义排序
     */
    private TreeMap<?, ?> treeMap;

    /**
     * Set系列全部底层全部复用的Map
     */
    private Set<?> set;

    /**
     * 线程安全的 HashMap(数组+链表)
     * 实现方法很粗造, 几乎所有方法加上 synchronized 关键字来保证线程安全, 这导致效率低下
     * 线程安全开销(性能低下), 不支持迭代器快速失败, 有替代品而导致也不再推荐使用
     */
    @Deprecated
    private Hashtable<?, ?> hashtable;

    /**
     * 线程安全的 HashMap - 效率很好
     * key 和 value 都不允许为 null, 会直接抛 NullPointerException
     * JDK1.7中ConcurrentHashMap采用了数组+Segment+分段锁的方式实现。
     * JDK1.8使用 volatile + synchronized + CAS 实现线程安全
     */
    private ConcurrentHashMap<?, ?> concurrentHashMap;

    /**
     * Deque - double ended queue 双端队列, 可以当作队列和栈来使用
     * Stack是JDK早期设计的栈, 由于粗糙的设计以及性能不太好已经被抛弃使用
     * 现在在Java中推荐使用的首选是 ArrayDeque, 其次是 LinkedList, 当然这两个都不是线程安全的
     */
    private ArrayDeque<?> arrayDeque;

    /**
     * 优先队列
     * 保证每次取出的元素都是队列中权值最小的
     * 基于堆实现, 具体为小顶堆
     */
    private PriorityQueue<?> priorityQueue;

    private static void foreach() {
        // 1. 增强for循环在编译后其实使用的是 Iterator(迭代器)来操作
        ArrayList<Object> objects = new ArrayList<>();
        for (Object object : objects) {
            log.info("{}", objects);
            objects.remove(object);
        }
        /*
        编译后变成:
        ArrayList<Object> objects = new ArrayList();
        Iterator var2 = objects.iterator();

        while(var2.hasNext()) {
            Object object = var2.next();
            log.info("{}", objects);
        }
         */

        // 2. 在循环中操作元素
        ArrayList<Integer> integers = new ArrayList<>();
        // ArrayList内部有一个modCount来记录操作的次数, 在这种增强for循环中操作集合会抛错(快速失败)
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        for (Integer integer : integers) {
            log.info("integer : {}", integer);
            // integers.add(2);
        }
        log.info("1: {}", integers);
        // integers.sort();


        // for (int i = 0; i < integers.size(); i++) {
        //     // add操作会导致死循环, 每次循环都加一个元素, i永远小于integers.size
        //     // integers.add(2);
        //     // 虽然不会报错, 但是不推荐, 编译器警告Suspicious 'List.remove()' in loop
        //     // integers.remove(i);
        // }
        log.info("2: {}", integers);

        HashSet<Object> sets = new HashSet<>();
        sets.add(1);
        sets.add(2);
        // 这里会抛出java.util.ConcurrentModificationException
        // for (Object set : sets) {
        //     log.info("循环了一次");
        //     // sets.add(3);
        //     sets.remove(1);
        //     sets.remove(2);
        // }
        log.info("set: {}", sets);

    }

    public static void testCopyOnWriteArrayList() {


        CopyOnWriteArrayList<Integer> copy = new CopyOnWriteArrayList<>();
        copy.add(1);


        Thread one = new Thread(() -> {
            // 这一步结束后, 迭代器将获得数组的快照版本
            Iterator<Integer> iterator = copy.iterator();
            while (iterator.hasNext()) {
                System.out.println(String.format("iterator.next: %s", iterator.next()));
            }
        });

        Thread two = new Thread(() -> {
            System.out.println(String.format("copy.remove: %b", copy.remove(Integer.valueOf(1))));
            // System.out.println(copy.get(0));
        });

        one.start();
        two.start();

        // 在上述情况中, 不论如何都不会产生报错, 创建迭代器的时候获得数组的快照版本
        // 之所以叫快照版本, 是因为CopyOnWriteArrayList是写时复制, 在修改或者删除元素时, 并不会直接操作之前的数组, 而是深克隆之前的数组, 然后操作克隆后的数组 非常巧妙
    }

    public static void main(String[] args) {
        // 循环中操作数组
        foreach();
        // 这个可以获得一个线程安全的数据 - SynchronizedCollection使用了装饰器模式
        java.util.Collection<Object> synchronizedCollection = Collections.synchronizedCollection(new ArrayList<>());
        // 同样的此方法返回一个只读集合, 使用了装饰器模式
        java.util.Collection<Object> unmodifiableCollection = Collections.unmodifiableCollection(new ArrayList<>());

        // Arrays.asList生成的数组为不可变数组, 数组内部为 final E[]
        List<Integer> asList = Arrays.asList(1);
        // asList.add(2); //这里会直接抛出异常 java.lang.UnsupportedOperationException
        log.info("asList: {}", asList);

        testCopyOnWriteArrayList();
    }
}
