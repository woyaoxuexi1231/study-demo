package org.hulei.basic.jdk.collection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author hulei
 * @since 2024/12/20 17:12
 */

@Slf4j
public class ArrayListTest {

    @SneakyThrows
    public static void main(String[] args) {
        /*
        ArrayList 数组列表 底层使用数组实现 Object[] 对象数组

        arrayList优点: 获取元素时间复杂度O(1), 插入/删除元素事件复杂度是O(n)
        在不触发扩容的前提下,插入元素其实是O(1), 但是触发扩容那么时间复杂度就变了, 扩容的过程涉及了数组元素的整体复制迁移

        扩容机制：
            [初始大小] - 默认情况下, 我们这里没有指定大小的话, 小于 10 第一次扩容会直接扩容到 10, 初始容量为 10的设计在性能和内存使用之间找到了一个平衡，10这个值被认为是合理的默认初始容量，既能应对一般情况，又不会浪费太多内存。
            [扩容因子] - ArrayList没有扩容因子，都是真正不够的时候扩容
            []
         */

        ArrayList<String> list = new ArrayList<>();
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
}
