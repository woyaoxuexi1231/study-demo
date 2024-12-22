package org.hulei.basic.jdk.collection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author hulei
 * @since 2024/12/20 17:12
 */

@Slf4j
public class CopyOnWriteArrayListTest {

    @SneakyThrows
    public static void main(String[] args) {

        /*
        CopyOnWriteArrayList(写时复制数组) 1.这是一个有序的数组列表 2.在写入的时候进行复制
        光看名字可以知道，这个数组列表在插入元素时做文章
        这个数组的实现线程安全的原理就是插入元素时上锁且不操作原来的数组，把原来的数组复制成一个新的数组(这个新的数组大小就是原数组+新插入元素的大小)，然后把元素插入这个新的数组
        不操作原数组的原因应该是为了不影响读操作，读操作在数组替换前都可以访问原来数组的元素。这种方式也促使这个数组并不是一个强一致性的数组，是一个最终一致性的数组。

        add方法内，在不同 jdk 版本也有不同区别。
        jdk1.8 使用 ReentrantLock 进行并发控制，jdk17 已经使用 synchronized进行控制了，这也标志着 jvm 进行了非常多的优化处理。

        *CopyOnWriteArrayList没有复杂的扩容机制，插入元素才进行复制操作。
         */

        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add("first");
        // addAll也是一样的, 复制后的数组直接就是两个数组的总和, 新的数组元素在原数组元素的后面
        copyOnWriteArrayList.addAll(CollectionUtil.newArrayList("second", "third"));

        // 而获取元素的整个过程都是不加锁的, 所以这里要考虑最终一致性问题, 可能获取元素的下一秒数组刚好更新, 所以可能拿到的并不是最新的数据
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
}
