package org.hulei.jdk.pattern.behavioral;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.behavioral
 * @className: IteratorTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 22:21
 */

public class IteratorTest {

    /*
    迭代器模式 - 提供一种方法顺序访问一个聚合对象中的各个元素, 而又不用暴露该对象的内部表示

    比如Java集合框架广泛运用到了迭代器模式
     */

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();

        list.add("hello");
        list.add("iterator");

        // 获取迭代器, 并用迭代器访问 list的内部元素
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }
}
