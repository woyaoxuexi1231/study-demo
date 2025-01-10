package org.hulei.basic.jdk.collection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @author hulei
 * @since 2024/12/20 17:12
 */

@Slf4j
public class LinkedListTest {

    public static void main(String[] args) {

        /*
        LinkedList 有序链表列表

        内部结构为双向链表 节点结构是Node, 双向节点, 保存上一个节点和下一个节点
        内部只保存头节点和尾节点, 既是链表则没有扩容问题

        LinkedList相较于ArrayList的优势在于插入和删除元素（如果在中间某个位置插入，也比较麻烦，这里特指头插和尾插）
        劣势就是获取元素比较麻烦，相较于ArrayList可以直接通过下标获取元素，LinkedList只能遍历然后寻找指定的元素

        LinkedList也不是一个线程安全的列表，在多线程环境下存在线程安全问题。
        根本原因：插入/删除元素的过程中由于链表的特性需要更换新的头/尾节点，这个过程中没有进行并发控制而导致的并发问题
         */
        LinkedList<String> linkedList = new LinkedList<>();

        // 添加元素默认是尾插法
        linkedList.add("first");
        linkedList.add(null);
        linkedList.addAll(CollectionUtil.newArrayList("second", "third"));

        // 也可以主动采用头插法
        linkedList.addFirst("begin");

        // 获取元素如果是获取头尾元素, 那么性能很快, 但是如果根据下标获取, 需要费点时间, 他这里还采用了一次二分法, 稍微快一点
        log.info("linkedList直接获取头尾元素, {}", linkedList.getLast());
        log.info("linkedList采用下标获取元素: {}", linkedList.get(3));
    }
}
