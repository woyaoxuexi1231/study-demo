package org.hulei.basic.jdk.collection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

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
    集合框架是 Java 中用于存储和操作一组对象的类库。它提供了一套接口和类，用于表示和操作不同类型的集合


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

    public static void main(String[] args) {
        arrayList();
        linkedList();
        copyOnWriteArrayList();
        hashMap();
        concurrentHashMap();

        iterator();
    }

    @SneakyThrows
    public static void arrayList() {

    }

    @SneakyThrows
    public static void linkedList() {

    }

    @SneakyThrows
    public static void copyOnWriteArrayList() {

    }

    @SneakyThrows
    public static void hashMap() {

    }

    @SneakyThrows
    public static void concurrentHashMap() {


    }

    private static void iterator() {

    }
}
