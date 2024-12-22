package org.hulei.basic.jdk.collection;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author hulei
 * @since 2024/12/20 17:13
 */

@Slf4j
public class HashMapTest {

    @SneakyThrows
    public static void main(String[] args) {

        /*
        哈希表，非常常用的一种集合类型。
        内部结构：数组 + Node(单向链表) + TreeNode(红黑树节点)

        扩容机制：
            [初始大小] - 默认大小是 16
            [扩容因子] - 0.75，也就是说当 hash 表(注意是 hash 表的空位)的大小超过 表大小的 3/4, 即触发扩容，链表的长度并不会触发扩容
            [扩容大小] - newCap = oldCap << 1 也就是每次扩容为原来的两倍
            哈希表的扩容机制很复杂，其中存在数据的迁移，以及对于数据结构的改变。
            对于那些 hash桶上只有一个元素的位置, 这些元素直接计算新的桶位置, 对于红黑树和链表, 把元素拆分成两部分, 一部分高位桶, 一部分低位桶
            这里用一个例子来说一下高位桶和低位桶的操作：
                扩容前：index = hash & (n - 1)
                扩容后：index = hash & (2n - 1)
                由于扩容的元素大小也只是比原来多了一个二进制位, 也就是说只有元素的 hash值正好与扩容后的那一位进行与运算发生变化才迁移到高位桶, 否则还在低位桶
                比如 110 & 15 和 110 & 31 的结果是一样的  113 & 15 和 113 & 31 是不一致的

        链表 -> 红黑树
            从链表转变为红黑树的契机是链表长度已经达到 8个 或者超过 8个立马触发结构改变
            为什么是 8个呢, 一般来说 hash算法足够好的前提下, 同一个位置的元素是不会超过 8个的, 这个概率是很小的, 所以在考虑到可能采用了糟糕的算法的情况下, 把 8作为阈值

        线程安全：哈希表同样并不是一个线程安全的集合。
            1.插入元素的非原子性, 可能导致多个 hashcode相同的元素在同一时间插入同一个位置, 导致元素丢失
            2.扩容的同时进行读取元素会导致迁移的 TODO
            3.会产生死循环 TODO
         */
        HashMap<String, String> hashMap = new HashMap<>();
        // 插入操作都是通过计算 key 的 hashcode来确定放入 hash表的具体位置, 如果发生 hash冲突, 那么元素会放入该位置上的链表的最后一个
        // null 值在 hashMap 中是被允许存储的, null 作为元素的 hashcode 将是 0
        hashMap.put("first", "first");
        hashMap.put("second", "second");
        hashMap.put(null, null);

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
}
