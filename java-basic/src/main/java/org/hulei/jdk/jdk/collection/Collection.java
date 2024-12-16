package org.hulei.jdk.jdk.collection;

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
        // 数组列表 底层使用数组实现 Object[] 对象数组,
        ArrayList<String> list = new ArrayList<>();
        // 默认情况下,我们这里没有指定大小的话,小于10第一次扩容会直接扩容到10
        // 一般来说会扩容 2 倍(这是原来的数组大小左移1位 + 原来的数组大小得到的, 为原来的三倍大小), 也有特殊情况就是在 2 倍的基础下,依旧不够,那么会直接扩容为合并后的大小
        // 当然最大的容量也就 整形的最大值了
        list.add("first");
        list.addAll(CollectionUtil.newArrayList("second", "third"));
        // 数组是多线程不安全的,内部没有任何的同步控制措施
        // 究其原因是 size++并非一个原子操作,多个线程同时插入的时候,下标可能会冲突
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                list.add("thread1-" + i);
            }
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                list.add("thread2-" + i);
            }
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
        // 这里可以看到,真实的数量和预期的数量其实是不一致的(一致为小概率事件)
        log.info("数组的大小本来应该是 2003, 但是数组大小为: {}", list.size());

        // arrayList优点: 获取元素时间复杂度O(1),插入/删除元素事件复杂度是O(n)
        // 在不触发扩容的前提下,插入元素其实是O(1),但是触发扩容那么时间复杂度就变了,扩容的过程涉及了数组元素的整体复制迁移

        // 比如这里按照下标来进行删除元素,那么内部的处理逻辑就是,把这个下标位置后面所有的元素往前移动
        list.remove(2);
        // 按照元素来移除就更麻烦,需要先遍历数组完事之后再进行元素的移动
        list.remove("first");
        // 添加元素也是一样的,在指定位置添加一个元素那么需要把指定位置的元素以及后面所有的元素往后移动
        list.add(2, "fourth");

        // 如果按照下标来获取元素就非常方便,直接获取就行
        log.info("按照下标获取元素: {}", list.get(1));
        // 这个操作又会便利一边数组,O(n)
        log.info("是否包含元素: {}", list.contains("second"));
    }

    @SneakyThrows
    public static void linkedList() {

        // 链表,内部是一个双向链表,节点结构是Node,双向节点,保存上一个节点和下一个节点
        // 内部只保存头节点和尾节点,既是链表则没有扩容问题
        LinkedList<String> linkedList = new LinkedList<>();
        // 添加元素默认是尾插法
        linkedList.add("first");
        linkedList.add(null);
        linkedList.addAll(CollectionUtil.newArrayList("second", "third"));
        // 也可以主动采用头插法
        linkedList.addFirst("begin");

        // 同样,也是一个非线程安全的集合,在多线程并发的情况下也会产生并发问题
        // 这里的根本原因是不管是头插法还是尾插法,在插入元素和更换新的头/尾节点的过程是没有并发控制的,多个线程同时操作那么可能元素就丢了

        // 获取元素如果是获取头尾元素,那么性能很快,但是如果根据下标获取,需要费点时间,他这里还采用了一次二分法,稍微快一点
        log.info("linkedList直接获取头尾元素, {}", linkedList.getLast());
        log.info("linkedList采用下标获取元素: {}", linkedList.get(3));
    }

    @SneakyThrows
    public static void copyOnWriteArrayList() {
        // CopyOnWriteArrayList(写时复制数组),首先是基于数组,其次线程安全
        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        // 1. 控制并发的工具采用的是 ReentrantLock, 而没有采用 synchronized, ReentrantLock可以提供更细粒度的多控制
        // 2. 顾名思义,写时复制,这里添加元素的时候会直接把原来的元素复制为 原数组大小+1 的新数组, 然后把元素插入新数据的末尾,在把原数组直接替换为新数组
        copyOnWriteArrayList.add("first");
        // addAll也是一样的,复制后的数组直接就是两个数组的总和,新的数组元素在原数组元素的后面
        copyOnWriteArrayList.addAll(CollectionUtil.newArrayList("second", "third"));

        // 而获取元素的整个过程都是不加锁的,所以这里要考虑最终一致性问题,可能获取元素的下一秒数组刚好更新,所以可能拿到的并不是最新的数据
        log.info("CopyOnWriteArrayList获取元素: {}", copyOnWriteArrayList.get(1));

        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                copyOnWriteArrayList.add("thread1-" + i);
            }
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                copyOnWriteArrayList.add("thread2-" + i);
            }
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();

        log.info("数组的大小本来应该是 20003, 但是数组大小为: {}", copyOnWriteArrayList.size());
        // tip: 内部有大量的创建数组,复制数组的操作 按理说是比较消耗cpu的
    }

    @SneakyThrows
    public static void hashMap() {
        // 首先内部结构是基于数组 + Node(单向链表) + TreeNode(红黑树节点), 非线程安全的集合
        // 这里没有显式的指定数组的大小,那么在第一次put元素的时候会进行默认初始化大小构建,默认大小是16,扩容因子是0.75,也就是说当 hash表(注意是hash表的空位)的大小超过 表大小的3/4,即触发扩容,每次扩容为原来的两倍
        // 链表长度和具体元素的个数并不影响扩容机制的触发

        // 而从链表转变为红黑树的契机是链表长度已经达到8个或者超过8个立马触发结构改变(为什么是8个呢,一般来说hash算法足够好的前提下,同一个位置的元素是不会超过8个的,这个概率是很小的,所以在考虑到可能采用了糟糕的算法的情况下,把8作为阈值)
        // 扩容还存在元素的迁移问题,对于那些hash桶上只有一个元素的位置,这些元素直接计算新的桶位置,对于红黑树和链表,把元素拆分成两部分,一部分高位桶,一部分低位桶
        // 扩容前：index = hash & (n - 1) 扩容后：index = hash & (2n - 1) 由于扩容的元素大小也只是比原来多了一个二进制位,也就是说只有元素的hash值正好与扩容后的那一位进行与运算发生变化才迁移到高位桶,否则还在低位桶
        // 比如 110 & 15 和 110 & 31 的结果是一样的  113 & 15 和 113 & 31是不一致的
        HashMap<String, String> hashMap = new HashMap<>();
        // 插入操作都是通过计算 key 的hashcode来确定放入hash表的具体位置,如果发生hash冲突,那么元素会放入该位置上的链表的最后一个
        // null值在hashMap中是被允许存储的, null作为元素的hashcode将是0
        hashMap.put("first", "first");
        hashMap.put("second", "second");
        hashMap.put(null, null);

        // 线程究其根本原因主要存在于: 1.插入元素的非原子性,可能导致多个hashcode相同的元素在同一时间插入同一个位置,导致元素丢失 2.扩容的同时进行读取元素会导致迁移的
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                hashMap.put("thread1-" + i, "thread1-" + i);
            }
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                hashMap.put("thread2-" + i, "thread2-" + i);
            }
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();

        log.info("hashMap的大小本来应该是 20003, 但是数组大小为: {}", hashMap.size());
    }

    @SneakyThrows
    public static void concurrentHashMap() {
        /*
         * 线程安全的 HashMap - 效率很好
         // * key 和 value 都不允许为 null, 会直接抛 NullPointerException
         * JDK1.7中 ConcurrentHashMap 采用了数组+ Segment +分段锁的方式实现。
         * JDK1.8使用 volatile + synchronized + CAS 实现线程安全
         */
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();

        // 插入元素的值不允许为 null
        // 插入的详细过程:
        //      1. 如果插入位置是没有元素的,那么使用cas来插入元素,采用无锁的形式
        //      2. 如果插入的位置有元素了,那么插入到链表/红黑树的过程用 synchronized 包起来,对象锁是那个桶位置的第一个元素
        concurrentHashMap.put("first", "first");
        // 读取操作是不加锁的
        log.info("concurrentHashMap: {}", concurrentHashMap.get("second"));

    }

    private static void iterator() {
        ArrayList<String> list = new ArrayList<>(CollectionUtil.newArrayList("first", "second", "third"));
        // iterator专门用于遍历元素的类
        // 1.首先它并不提供线程安全保证,只是提供了快速失败机制,在发现遍历期间有元素变动,立马抛出异常
        // 2.遍历元素通过其内部的游标 cursor 来完成, cursor记录下一个访问的元素的下标
        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            // 最开始我还没理解为什么需要搞这么一个类,直到看到这个方法我理解了.
            // Iterator主要是提供一个remove方法能够更方便我们在遍历中删除元素而不用担心会出现问题,必须在next之后才能删除元素
            // 最重要的就是他并不提供并发控制
            iterator.remove();
        }

        list.addAll(CollectionUtil.newArrayList("first", "second", "third"));
        // 对于for语法糖,可以从编译后的class文件中看到,其实使用的也是iterator
        for (String string : list) {
            System.out.println(string);
        }
    }
}
