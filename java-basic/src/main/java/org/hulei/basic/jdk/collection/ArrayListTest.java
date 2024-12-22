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

        arrayList优点:
            获取元素时间复杂度 O(1)
        ArrayList缺点：
            1. 插入：
                在不触发扩容的前提下, 插入元素是 O(1)。
                但是触发扩容时，扩容过程涉及的元素的整体复制迁移所以插入的时间复杂度总和来看是 O(n)
            2. 删除元素：
                删除元素复杂度是O(n)，如果在中间位置删除，需要将后面的元素整体向前移动。
            ArrayList插入和删除慢的根本原因是 ArrayList 需要维护每个元素的有序排列(这个有序仅代表插入元素时的顺序)

        由于数组的特殊性质，数组在被初始化的时候需要指定数组的大小(不管是使用哪种方式初始化，比如指定元素、或者指定大小)
        但是使用数组时并不知道后续数组的大小具体是多少(只有极少数情况，在需求明确的情况下，我们会知道数组一定只有多少，大部分情况来说数组的大小都是动态的)
        所以在数组不足够容纳元素是会触发扩容机制：
            [初始大小] - 默认情况下, 我们这里没有指定大小的话, 小于 10 第一次扩容会直接扩容到 10, 初始容量为 10的设计在性能和内存使用之间找到了一个平衡，10这个值被认为是合理的默认初始容量，既能应对一般情况，又不会浪费太多内存。
            [扩容因子] - ArrayList没有扩容因子，都是真正不够的时候扩容
            [扩容大小] - 正常情况下扩容大小为 原来的数组大小+原来数组的一半 也就是说扩容后的数组大小为原来的 1.5 倍。但是这样扩容免不了容易超出最大限制(数组大小最大也不能超过整形的最大值)
                        所以当 原来的数组大小+原来数组的一半 这个超过了整形最大值的时候，会退而求其次的按照需要的大小进行扩容，需要多大就扩容到多大
         */
    }

    public void basic(){

        // 初始化大小 10
        ArrayList<String> list = new ArrayList<>();

        // 扩容机制在不同 jdk 略微有所不同，但是大致逻辑一致，都会判断使用 原来的数组大小+原来数组的一半 和 原来数组大小+需要的实际大小 这两者谁更大，谁更大就使用谁
        list.add("first");
        list.addAll(CollectionUtil.newArrayList("second", "third"));

        // 删除元素, 内部的处理逻辑会把这个下标位置后面所有的元素往前移动
        list.remove(2);
        // 按照元素来移除就更麻烦, 需要先遍历数组完事之后再进行元素的移动
        list.remove("first");
        // 添加元素也是一样的,在指定位置添加一个元素那么需要把指定位置的元素以及后面所有的元素往后移动
        list.add(2, "fourth");

        // 如果按照下标来获取元素就非常方便,直接获取就行
        log.info("按照下标获取元素: {}", list.get(1));
        // 这个操作又会便利一边数组,O(n)
        log.info("是否包含元素: {}", list.contains("second"));
    }


    /**
     * ArrayList 并非线程安全
     */
    @SneakyThrows
    public void concurrent(){

        ArrayList<String> list = new ArrayList<>();

        // 数组是多线程不安全的, 内部没有任何的同步控制措施
        // 究其原因是 size++(size是ArrayList内部表示当前一共有多少个元素的整型，size用于插入元素时正好插入末尾) 并非一个原子操作, 多个线程同时插入的时候, 下标可能会冲突
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
    }
}
