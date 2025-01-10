package org.hulei.basic.jdk.collection;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author hulei
 * @since 2024/12/20 17:13
 */

@Slf4j
public class IteratorTest {

    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>(CollectionUtil.newArrayList("first", "second", "third"));
        /*
        iterator专门用于遍历元素的类
            1.首先它并不提供线程安全保证, 只是提供了快速失败机制, 在发现遍历期间有元素变动, 立马抛出异常
            2.遍历元素通过其内部的游标 cursor 来完成, cursor记录下一个访问的元素的下标
         */
        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            /*
            最开始我还没理解为什么需要搞这么一个类, 直到看到这个方法我理解了.
            Iterator 主要是提供一个 remove 方法能够更方便我们在遍历中删除元素而不用担心会出现问题, 必须在 next之后才能删除元素
            就 ArrayList 内部的 Iterator 而言：
                1. cursor会被设置为当前删除的这个元素的位置，因为后面的元素会往前移
                2. 内部会维持 ArrayList 的 modCount，避免下一次遍历元素出现问题
                从这里就能看出来，如果仅仅使用 ArrayList在循环中删除元素，那么由于访问下标一直在递增，但是列表元素已经没有那么多了，访问到后面一定会报下标位置没有元素的错误，也就是IndexOutOfBoundsException
            最重要的就是他并不提供并发控制
             */
            iterator.remove();
        }

        list.addAll(CollectionUtil.newArrayList("first", "second", "third"));
        // 对于 for 语法糖, 可以从编译后的 class 文件中看到, 其实使用的也是 iterator
        for (String string : list) {
            System.out.println(string);
        }
    }
}
