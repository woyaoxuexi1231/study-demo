package org.hulei.basic.jdk.collection;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hulei
 * @since 2024/12/20 17:13
 */

@Slf4j
public class ConcurrentHashMapTest {

    public static void main(String[] args) {
        /*
        线程安全的 HashMap - 效率很好
        key 和 value 都不允许为 null, 会直接抛 NullPointerException

        JDK1.7中 ConcurrentHashMap 采用了数组 + Segment + 分段锁的方式实现。
        JDK1.8使用 volatile + synchronized + CAS 实现线程安全
         */

        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        /*
        插入元素的值不允许为 null
        插入的详细过程:
            1. 如果插入位置是没有元素的, 那么使用 cas来插入元素, 采用无锁的形式
            2. 如果插入的位置有元素了, 那么插入到链表/红黑树的过程用 synchronized 包起来, 对象锁是那个桶位置的第一个元素
         */
        concurrentHashMap.put("first", "first");
        // 读取操作是不加锁的
        log.info("concurrentHashMap: {}", concurrentHashMap.get("second"));
    }
}
